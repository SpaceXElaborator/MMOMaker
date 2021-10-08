package com.terturl.MMO.Util.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.terturl.MMO.Entity.NPC.NPC;

import lombok.Getter;

public class ClickPlayerClassNPCEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	
	@Getter
	private final NPC npc;
	@Getter
	private final Player p;
	@Getter
	private final Boolean IsCrouching;
	private Boolean cancelled = false;
	
	public ClickPlayerClassNPCEvent(NPC n, Player player, Boolean isCrouching) {
		npc = n;
		p = player;
		IsCrouching = isCrouching;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		cancelled = arg0;
	}

}
