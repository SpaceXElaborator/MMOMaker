package com.terturl.MMO.API.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.terturl.MMO.Util.Items.CustomItem;

import lombok.Getter;
import lombok.Setter;

/**
 * Listener class to handle when a player crafts a CustomItem
 * 
 * @author Sean Rahman
 * @since 0.43.0
 * @see com.terturl.MMO.Util.Items.CustomItem
 *
 */
public class MMOItemCraftEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();

	@Getter
	private final CustomItem customItem;

	@Getter
	private final Player player;

	@Getter @Setter
	private boolean cancelled = false;

	/**
	 * 
	 * @param ci CustomItem that was crafted
	 * @param p  Player that crafted the CustomItem
	 */
	public MMOItemCraftEvent(CustomItem ci, Player p) {
		customItem = ci;
		player = p;
	}

	/**
	 * Required class from Spigot/Bukkit
	 * 
	 * @return
	 */
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	/**
	 * Required class from Spigot/Bukkit
	 * 
	 * @return
	 */
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

}