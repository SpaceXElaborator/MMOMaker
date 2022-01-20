package com.terturl.MMO.Util.Items;

public enum MMOModifiers {
	HEALTH("Health"), DAMAGE("Damage"), LOOT("Loot"), SPEED("Speed"), DEFENSE("Defense"), MANA("Mana"),
	RAGE("Rage");

	MMOModifiers(String s) {
		this.n = s;
	}

	private final String n;

	public String getFriendlyName() {
		return n;
	}

	public static boolean exists(String s) {

		for (MMOModifiers v : values()) {
			if (s.equalsIgnoreCase(v.toString())) {
				return true;
			}
		}

		return false;
	}
}