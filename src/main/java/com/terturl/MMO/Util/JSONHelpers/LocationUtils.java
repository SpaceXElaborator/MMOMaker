package com.terturl.MMO.Util.JSONHelpers;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Used to save and load locations from a string
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class LocationUtils {

	/**
	 * Returns a string representation of a location
	 * 
	 * @param loc Location to save
	 * @return String representation
	 */
	public static String locationSerializer(Location loc) {
		return String.format("%s:%f:%f:%f", loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ());
	}

	/**
	 * Returns a location representation of a string
	 * 
	 * @param s String to load location from
	 * @return Location
	 */
	public static Location locationDeSerializer(String s) {
		String[] ss = s.split(":");
		return new Location(Bukkit.getWorld(ss[0]), Double.valueOf(ss[1]), Double.valueOf(ss[2]),
				Double.valueOf(ss[3]));
	}

}