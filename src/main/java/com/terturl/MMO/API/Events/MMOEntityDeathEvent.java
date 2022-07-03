package com.terturl.MMO.API.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.terturl.MMO.Entity.MMOEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * Event to handle when a player kills a named MMOEntity
 * 
 * @author Sean Rahman
 * @since 0.37.0
 * @see com.terturl.MMO.Entity.MMOEntity
 * 
 */
public class MMOEntityDeathEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();

	@Getter
	private final MMOEntity MMOEntity;

	@Getter
	private final Player player;

	@Getter @Setter
	private boolean cancelled = false;

	/**
	 * @param mo The MMOEntity that died
	 * @param p  The player that killed the MMOEntity
	 */
	public MMOEntityDeathEvent(MMOEntity mo, Player p) {
		MMOEntity = mo;
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