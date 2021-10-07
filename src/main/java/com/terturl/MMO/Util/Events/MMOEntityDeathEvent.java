package com.terturl.MMO.Util.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.terturl.MMO.Entity.MMOEntity;

import lombok.Getter;

public class MMOEntityDeathEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	
	@Getter
	private final MMOEntity MMOEntity;
	
	@Getter
	private final Player player;
	
	private boolean cancelled;
	
	public MMOEntityDeathEvent(MMOEntity mo, Player p) {
		MMOEntity = mo;
		player = p;
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