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
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Player.MMOClasses.MMOClass;
import com.terturl.MMO.Util.Events.PickUpMMOItemEvent;

public class HerbalismInteract implements Listener {

	@EventHandler
	public void onInteractBlock(PlayerInteractEvent e) {
		if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		if(!e.getHand().equals(EquipmentSlot.HAND)) return;
		Material mat = e.getClickedBlock().getType();
		HerbalismManager hm = MinecraftMMO.getInstance().getHerbalismManager();
		if(!hm.containsMaterial(mat)) return;
		Player p = e.getPlayer();
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
		MMOClass mc = mp.getMmoClasses().get(mp.getCurrentCharacter());
		if(!mc.containsSkill("Herbalism")) return;
		if(mp.getBlocksNotViewing().contains(e.getClickedBlock().getLocation())) return;
		HerbalismGatherItems hgi = hm.getItem(mat);
		mc.addXp("Herbalism", hgi.getXp());
		hgi.getItems().forEach((k, v) -> {
			if(v.getsItem()) {
				Integer amount = v.getAmount().getAmount();
				
				PickUpMMOItemEvent pumie = new PickUpMMOItemEvent(p, k, amount);
				if(!pumie.isCancelled()) Bukkit.getPluginManager().callEvent(pumie);
				
				ItemStack is = k.makeItem(amount);
				
				p.getInventory().addItem(is);
			}
		});
		
		mp.getBlocksNotViewing().add(e.getClickedBlock().getLocation());
		
		Runnable r2 = new Runnable() {
			public void run() {
				p.sendBlockChange(e.getClickedBlock().getLocation(), Material.AIR.createBlockData());
			}
		};
		MinecraftMMO.getInstance().getServer().getScheduler().runTaskLater(MinecraftMMO.getInstance(), r2, 1L);
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