package com.terturl.MMO.Util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class EntityUtils {

	public static boolean playerIsLookingAtEntity(Player p, Entity e) {
		Vector eyeDir = p.getEyeLocation().getDirection();
		Vector eyeLoc = p.getEyeLocation().toVector();
		Vector eLoc = e.getLocation().toVector();
		Vector pEVec = eLoc.subtract(eyeLoc);
		float angle = eyeDir.angle(pEVec);
		return angle <= 0.2f;
	}
	
}