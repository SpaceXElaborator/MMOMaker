package com.terturl.MMO.API.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.terturl.MMO.Util.Items.CustomItem;

import lombok.Getter;
import lombok.Setter;

public class MMOEquipArmorEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	
	@Getter
	private CustomItem mmoItem;
	
	@Getter
	private final Player player;

	@Getter @Setter
	private boolean cancelled = false;
	
	public MMOEquipArmorEvent(CustomItem ci, Player p) {
		player = p;
		mmoItem = ci;
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