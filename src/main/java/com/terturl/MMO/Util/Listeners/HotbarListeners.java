package com.terturl.MMO.Util.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

import com.terturl.MMO.MinecraftMMO;

/**
 * Handles when the player changes their hotbar active item
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class HotbarListeners implements Listener {

	@EventHandler
	public void checkItem(PlayerItemHeldEvent e) {
		if (!MinecraftMMO.getInstance().getPlayerHandler().PlayerExists(e.getPlayer()))
			return;
		Bukkit.getScheduler().scheduleSyncDelayedTask(MinecraftMMO.getInstance(), new Runnable() {
			@Override
			public void run() {
				if (MinecraftMMO.getInstance().getPlayerHandler().getPlayer(e.getPlayer()).isInCombat()) {
					int i = e.getPlayer().getInventory().getHeldItemSlot();
					if (i == 2 || i == 3 || i == 4) {
						e.getPlayer().getInventory().setHeldItemSlot(0);
					}
				}
			}
		}, 1);
	}

}