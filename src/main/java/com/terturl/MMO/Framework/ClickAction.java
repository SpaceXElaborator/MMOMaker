package com.terturl.MMO.Framework;

import org.bukkit.event.inventory.ClickType;

/**
 * Helper class for InventoryUI to check if the player left clicked or right
 * clicked on an InventoryButton
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public enum ClickAction {
	RIGHT_CLICK, LEFT_CLICK;

	public static ClickAction from(ClickType action) {
		switch (action) {
		case RIGHT:
		case SHIFT_RIGHT:
			return ClickAction.RIGHT_CLICK;
		default:
			return ClickAction.LEFT_CLICK;
		}
	}
}