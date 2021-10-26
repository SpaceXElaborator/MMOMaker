package com.terturl.MMO.API.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.terturl.MMO.Entity.MMOEntity;

import lombok.Getter;

/**
 * 
 * @author Sean Rahman
 * @since 0.37.0
 * 
 */
public class MMOEntityDeathEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();

	@Getter
	private final MMOEntity MMOEntity;

	@Getter
	private final Player player;

	private boolean cancelled;

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

	/**
	 * Gets if the event was cancelled for if it should call or not
	 * 
	 * @return
	 */
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * Set the cancelled status of the event
	 * 
	 * @param arg0 Boolean to set cancelled to
	 */
	@Override
	public void setCancelled(boolean arg0) {
		cancelled = arg0;
	}

}