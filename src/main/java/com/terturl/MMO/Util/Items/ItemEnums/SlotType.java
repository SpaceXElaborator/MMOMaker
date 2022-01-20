package com.terturl.MMO.Util.Items.ItemEnums;

public enum SlotType {
	MAIN_HAND, OFF_HAND, HELMET, CHEST, LEGS, BOOTS, TRINKET, DROP, ITEM;

	public static boolean exists(String s) {

		for (SlotType v : values()) {
			if (s.equalsIgnoreCase(v.toString())) {
				return true;
			}
		}

		return false;
	}
}