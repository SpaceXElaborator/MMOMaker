package com.terturl.MMO.Util.Strings;

import org.bukkit.Color;

/**
 * String helper class that can convert strings into objects, this class is soon
 * to be deprecated as I do a restructure coming soon
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class StringUtils {

	/**
	 * Get the RGB color value from a string
	 * 
	 * @param s String to get RBG value from
	 * @return Color
	 */
	public static Color getColorFromString(String s) {
		String[] colDigits = s.split(":");
		int r = isInt(colDigits[0]) ? Integer.valueOf(colDigits[0]) : 0;
		int g = isInt(colDigits[1]) ? Integer.valueOf(colDigits[1]) : 0;
		int b = isInt(colDigits[2]) ? Integer.valueOf(colDigits[2]) : 0;
		Color c = Color.fromBGR(r, g, b);
		return c;
	}

	/**
	 * Checks to see if a string value is an integer from a base 10 radix
	 * 
	 * @param s String to parse
	 * @return If s is int
	 */
	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

}