package com.terturl.MMO.Util.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.terturl.MMO.Util.Items.CustomItem;

import lombok.Getter;

public class PickUpMMOItemEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();

	@Getter
	private final CustomItem customItem;
	
	@Getter
	private final Integer amount;
	
	@Getter
	private final Player player;
	
	private Boolean cancelled = false;

	public PickUpMMOItemEvent(Player p, CustomItem ci, Integer a) {
		player = p;
		customItem = ci;
		amount = a;
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