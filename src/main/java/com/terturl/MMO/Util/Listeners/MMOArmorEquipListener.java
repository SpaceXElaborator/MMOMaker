package com.terturl.MMO.Util.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.terturl.MMO.API.Events.MMOEquipArmorEvent;

public class MMOArmorEquipListener implements Listener {

	@EventHandler
	public void onMMOArmorEquip(MMOEquipArmorEvent e) {
		e.getPlayer().sendMessage("Test");
	}
	
}