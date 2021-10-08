package com.terturl.MMO.Util.JSONHelpers;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtils {

	public static String locationSerializer(Location loc) {
		return String.format("%s:%f:%f:%f", loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ());
	}
	
	public static Location locationDeSerializer(String s) {
		String[] ss = s.split(":");
		return new Location(Bukkit.getWorld(ss[0]), Double.valueOf(ss[1]), Double.valueOf(ss[2]), Double.valueOf(ss[3]));
	}
	
}