 package com.terturl.MMO.API.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.terturl.MMO.Entity.NPC.NPC;

import lombok.Getter;
import lombok.Setter;

/**
 * Handles when a player clicks on an NPC that provides a MMOClass
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class ClickClassNPCEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();

	@Getter
	private final NPC npc;
	@Getter
	private final Player p;
	@Getter
	private final Boolean IsCrouching;
	
	@Getter @Setter
	private boolean cancelled = false;

	/**
	 * @param n           NPC that was clicked on
	 * @param player      Player that interacted with the NPC
	 * @param isCrouching If the player was crouching or not
	 */
	public ClickClassNPCEvent(NPC n, Player player, Boolean isCrouching) {
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

}