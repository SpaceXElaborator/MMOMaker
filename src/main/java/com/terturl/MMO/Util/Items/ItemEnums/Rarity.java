package com.terturl.MMO.Util.Items.ItemEnums;

import net.md_5.bungee.api.ChatColor;

public enum Rarity {
	COMMON(ChatColor.DARK_GRAY), UNCOMMON(ChatColor.DARK_GREEN), RARE(ChatColor.DARK_RED),
	LEGENDARY(ChatColor.DARK_PURPLE), MYTHIC(ChatColor.GOLD);

	Rarity(ChatColor g) {
		this.color = g;
	}

	private final ChatColor color;

	public ChatColor getChatColor() {
		return color;
	}

	public static boolean exists(String s) {

		for (Rarity v : values()) {
			if (s.equalsIgnoreCase(v.toString())) {
				return true;
			}
		}

		return false;
	}

}