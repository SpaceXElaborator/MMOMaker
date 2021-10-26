package com.terturl.MMO.Player.Skills.Herbalism;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.API.Events.PickUpMMOItemEvent;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Player.MMOClasses.MMOClass;

/**
 * Handles the interaction of a player gathering items for a herbalism block
 * @author Sean Rahman
 * @since 0.47.0
 *
 */
public class HerbalismInteract implements Listener {

	@EventHandler
	public void onInteractBlock(PlayerInteractEvent e) {
		if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		if(!e.getHand().equals(EquipmentSlot.HAND)) return;
		
		// Get material and check if it is a valid item in the HerbalismManager
		Material mat = e.getClickedBlock().getType();
		HerbalismManager hm = MinecraftMMO.getInstance().getHerbalismManager();
		if(!hm.containsMaterial(mat)) return;
		
		Player p = e.getPlayer();
		
		// Get the players MMOClass from the players MMOPlayer's currentCharacter
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		
		// Make sure they have the skill and they can visually see the block
		if(!mc.containsSkill("Herbalism")) return;
		if(mp.getBlocksNotViewing().contains(e.getClickedBlock().getLocation())) return;
		
		// "Gather" the item and apply the random amount they will be receiving if they get the item
		HerbalismGatherItems hgi = hm.getItem(mat);
		mc.addXpToSkill("Herbalism", hgi.getXp());
		hgi.getItems().forEach((k, v) -> {
			if(v.getsItem()) {
				Integer amount = v.getAmount().getAmount();
				
				// Create a new PickUpMMOItemEvent since they are technically "Picking up" the item
				PickUpMMOItemEvent pumie = new PickUpMMOItemEvent(p, k, amount);
				if(!pumie.isCancelled()) Bukkit.getPluginManager().callEvent(pumie);
				
				ItemStack is = k.makeItem(amount);
				
				p.getInventory().addItem(is);
			}
		});
		
		// Hide the block from the players view so they no longer see it ClientSide and no longer gather it for a period of time
		mp.getBlocksNotViewing().add(e.getClickedBlock().getLocation());
		Runnable r2 = new Runnable() {
			public void run() {
				p.sendBlockChange(e.getClickedBlock().getLocation(), Material.AIR.createBlockData());
			}
		};
		
		// Spigot does require sending a block chance to happen a tick after it takes place
		MinecraftMMO.getInstance().getServer().getScheduler().runTaskLater(MinecraftMMO.getInstance(), r2, 1L);
		
		// After the amount of time, return the block to the players view so they can gather it again
		Runnable r = new Runnable() {
			@Override
			public void run() {
				p.sendBlockChange(e.getClickedBlock().getLocation(), mat.createBlockData());
				mp.getBlocksNotViewing().remove(e.getClickedBlock().getLocation());
			}
		};
		MinecraftMMO.getInstance().getServer().getScheduler().runTaskLater(MinecraftMMO.getInstance(), r, 40L);
	}
	
}