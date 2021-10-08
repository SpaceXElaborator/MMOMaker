package com.terturl.MMO.Util.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {
	
	@EventHandler
	public void handleDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}
	
}