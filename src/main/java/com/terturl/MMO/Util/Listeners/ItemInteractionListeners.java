package com.terturl.MMO.Util.Listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Inventories.QuestInventory;

public class ItemInteractionListeners implements Listener {

	@EventHandler
	public void interact(PlayerInteractEvent e) {
		if(!e.getHand().equals(EquipmentSlot.HAND)) return;
		if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BOOK)) {
			QuestInventory qi = new QuestInventory(MinecraftMMO.getInstance().getPlayerHandler().getPlayer(e.getPlayer()));
			qi.open(e.getPlayer());
		}
	}
	
}