package com.terturl.MMO.Util.Items.ItemEnums;

import net.md_5.bungee.api.ChatColor;

public enum CraftRarity {
	CRUDE(ChatColor.DARK_GRAY, "Crude", 0.0), WELL_MADE(ChatColor.GREEN, "Well-Made", 0.1),
	SUPER_CRAFTED(ChatColor.LIGHT_PURPLE, "Super-Crafted", 0.2),
	MASTER_CRAFTED(ChatColor.YELLOW, "Master-Crafted", 0.3);

	CraftRarity(ChatColor g, String s, double d) {
		this.g = g;
		this.s = s;
		this.d = d;
	}

	private final ChatColor g;
	private final String s;
	private final double d;

	public String getFriendlyName() {
		return s;
	}

	public ChatColor getChatColor() {
		return g;
	}

	public Double getMultiplier() {
		return d;
	}

	public static boolean exists(String s) {

		for (CraftRarity v : values()) {
			if (s.equalsIgnoreCase(v.toString())) {
				return true;
			}
		}

		return false;
	}
}