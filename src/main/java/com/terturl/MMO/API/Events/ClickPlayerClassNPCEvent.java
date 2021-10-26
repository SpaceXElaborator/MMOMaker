package com.terturl.MMO.API.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.terturl.MMO.Entity.NPC.NPC;

import lombok.Getter;

/**
 * Handles when a player clicks on an NPC that contains their class information
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class ClickPlayerClassNPCEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();

	@Getter
	private final NPC npc;
	@Getter
	private final Player p;
	@Getter
	private final Boolean IsCrouching;
	private Boolean cancelled = false;

	/**
	 * @param n           NPC that was clicked on
	 * @param player      Player that clicked on that NPC
	 * @param isCrouching If the player was crouching or not
	 */
	public ClickPlayerClassNPCEvent(NPC n, Player player, Boolean isCrouching) {
		npc = n;
		p = player;
		IsCrouching = isCrouching;
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
