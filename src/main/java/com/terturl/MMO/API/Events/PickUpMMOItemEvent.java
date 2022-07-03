package com.terturl.MMO.API.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.terturl.MMO.Util.Items.CustomItem;

import lombok.Getter;
import lombok.Setter;

/**
 * Listener class to handle when a player picks up CustomItem
 * 
 * @author Sean Rahman
 * @since 0.48.0
 * @see com.terturl.MMO.Util.Items.CustomItem
 *
 */
public class PickUpMMOItemEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();

	@Getter
	private final CustomItem customItem;

	@Getter
	private final Integer amount;

	@Getter
	private final Player player;

	@Getter @Setter
	private boolean cancelled = false;

	/**
	 * @param p  The player that picked up the CustomItem
	 * @param ci The CustomItem the player picked up
	 * @param a  The amount of the CustomItem the player picked up
	 */
	public PickUpMMOItemEvent(Player p, CustomItem ci, Integer a) {
		player = p;
		customItem = ci;
		amount = a;
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