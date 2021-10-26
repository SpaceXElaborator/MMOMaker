package com.terturl.MMO.Util.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Class only serves one function, to stop the player from dropping an item from
 * their inventory
 * 
 * @author Sean Rahman
 * @since 0.39.1
 *
 */
public class PlayerDropItemListener implements Listener {

	@EventHandler
	public void handleDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}

}