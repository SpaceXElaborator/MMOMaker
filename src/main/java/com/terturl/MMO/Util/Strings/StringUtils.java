package com.terturl.MMO.Util.Strings;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public class StringUtils {

	public static String message(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public static Color getColorFromString(String s) {
		String[] colDigits = s.split(":");
		int r = isInt(colDigits[0]) ? Integer.valueOf(colDigits[0]): 0;
		int g = isInt(colDigits[1]) ? Integer.valueOf(colDigits[1]): 0;
		int b = isInt(colDigits[2]) ? Integer.valueOf(colDigits[2]): 0;
		Color c = Color.fromBGR(r, g, b);
		return c;
	}

	public static boolean isInteger(String s, int radix) {
		if (s.isEmpty())
			return false;
		for (int i = 0; i < s.length(); i++) {
			if (i == 0 && s.charAt(i) == '-') {
				if (s.length() == 1)
					return false;
				else
					continue;
			}
			if (Character.digit(s.charAt(i), radix) < 0)
				return false;
		}
		return true;
	}
	
	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

}