package com.terturl.MMO.Framework;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import com.terturl.MMO.MinecraftMMO;

public class InventoryUI implements Listener {

	protected final Set<UUID> players = new HashSet<UUID>();
	protected final Map<Integer, InventoryButton> buttons = new HashMap<Integer, InventoryButton>();
	protected Set<Integer> updatedSlots = new HashSet<Integer>();
	protected final Inventory inv;
	protected boolean registered = false;
	protected final String title;
	
	public InventoryUI(InventoryType type, String title) {
		inv = Bukkit.createInventory(null, type, title);
		this.title = title;
	}
	
	public String getName() {
		return title;
	}
	
	public InventoryUI(int size, String title) {
		inv = Bukkit.createInventory(null, size, title);
		this.title = title;
	}
	
	public void open(Player p) {
		if(players.contains(p.getUniqueId())) return;
		players.add(p.getUniqueId());
		if(players.size() == 1 && !registered) MinecraftMMO.getInstance().registerListener(this);
		registered = true;
		p.openInventory(inv);
	}
	
	public void open(Iterable<Player> pls) {
		for(Player p : pls) open(p);
	}
	
	public void close(Iterable<Player> pls) {
		for(Player p : pls) close(p);
	}
	
	public void close(Player p) {
		if(!players.contains(p.getUniqueId())) return;
		players.remove(p.getUniqueId());
		if(players.size() == 0 && this.registered) {
			HandlerList.unregisterAll(this);
			registered = false;
		}
		p.closeInventory();
		onClose(p);
	}
	
	public Set<UUID> getCurrentPlayers() {
		return players;
	}
	
	public void addButton(InventoryButton but) {
		Integer nextOpenSlot = getNextOpenSlot();
		if(nextOpenSlot == null) throw new IllegalStateException("Unable to place the button! No room!");
		addButton(but, nextOpenSlot);
	}
	
	public void addButton(InventoryButton but, int slot) {
		buttons.put(slot, but);
	}
	
	public void removeButton(InventoryButton but) {
		clearSlot(getSlotFor(but));
	}
	
	public void clearSlot(int slot) {
		buttons.remove(slot);
		markForUpdate(slot);
	}
	
	public void moveButton(InventoryButton but, int slot) {
		removeButton(but);
		addButton(but, slot);
	}
	
	public void markForUpdate(InventoryButton button) {
		markForUpdate(getSlotFor(button));
	}
	
	public void markForUpdate(int slot) {
		updatedSlots.add(slot);
	}
	
	public Integer getSlotFor(InventoryButton but) {
		for(Entry<Integer, InventoryButton> buttonEnt : buttons.entrySet()) {
			if(((InventoryButton)buttonEnt.getValue()).equals(but)) return buttonEnt.getKey();
		}
		return Integer.valueOf(-1);
	}
	
	public void onClose(Player p) {}
	
	public boolean isFilled(int slot) {
		return buttons.containsKey(slot);
	}
	
	public void updateInventory() {
		for(int x = 0; x < inv.getSize(); x++) {
			InventoryButton but = buttons.get(Integer.valueOf(x));
			if(but == null && inv.getItem(x) != null) inv.setItem(x, null);
			else if((inv.getItem(x) == null && but != null) || updatedSlots.contains(Integer.valueOf(x))) {
				assert but != null;
				inv.setItem(x, but.getItem());
			}
		}
		for(UUID u : players) {
			Player p = Bukkit.getPlayer(u);
			p.updateInventory();
		}
		updatedSlots = new HashSet<Integer>();
	}
	
	public Integer getNextOpenSlot() {
		Integer nextSlot = Integer.valueOf(0);
		for(Integer ints : buttons.keySet()) {
			if(ints.equals(nextSlot)) nextSlot = Integer.valueOf(ints.intValue() + 1);
		}
		return (nextSlot.intValue() >= inv.getSize()) ? null : nextSlot;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public final void onPlayerLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(players.contains(p.getUniqueId())) players.remove(p.getUniqueId());
	}
	
	@EventHandler
	public final void onInvClose(InventoryCloseEvent e) {
		if(!(e.getPlayer() instanceof Player)) return;
		if(!e.getInventory().equals(inv)) return;
		Player p = (Player)e.getPlayer();
		players.remove(p.getUniqueId());
		onClose(p);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onInventoryClick(InventoryClickEvent e) {
		if(e.getClickedInventory() == null) return;
		if((e.getClickedInventory().getHolder() instanceof Player)) {
			e.setCancelled(false);
		} else {
			if(!(e.getWhoClicked() instanceof Player)) return;
			InventoryView view = e.getView();
			if(!view.getTitle().equals(title)) return;
			if(!players.contains(e.getWhoClicked().getUniqueId())) return;
			Player p = (Player)e.getWhoClicked();
			InventoryButton but = buttons.get(Integer.valueOf(e.getSlot()));
			if(but == null) return;
			try {
				but.onPlayerClick(p, ClickAction.from(e.getClick()));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlace(InventoryMoveItemEvent e) {
		if(e.getDestination().equals(inv)) e.setCancelled(true);
	}
	
	public final void onPlayerInventoryMove(InventoryMoveItemEvent e) {
		if(!(e.getDestination().getHolder() instanceof InventoryUI)) return;
		e.setCancelled(true);
	}
	
	public Collection<InventoryButton> getButtons() {
		return buttons.values();
	}
	
	public Map<Integer, InventoryButton> getAllButtons() {
		return buttons;
	}
	
}