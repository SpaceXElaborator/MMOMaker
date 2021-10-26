package com.terturl.MMO.Util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Will eventually hold more information, but will contain all information
 * relating to entities in relation to another entity
 * 
 * @author Sean Rahman
 * @since 0.51.0
 *
 */
public class EntityUtils {

	/**
	 * This will check the players eye location and rotation with another entities
	 * eye location to see if the player is looking at that entity
	 * 
	 * @param p Player that is looking
	 * @param e Entity the player is supposedly looking at
	 * @return If the player is within 0.2 away from entities eye location
	 */
	public static boolean playerIsLookingAtEntity(Player p, Entity e) {
		Vector eyeDir = p.getEyeLocation().getDirection();
		Vector eyeLoc = p.getEyeLocation().toVector();
		Vector eLoc = e.getLocation().toVector();
		Vector pEVec = eLoc.subtract(eyeLoc);
		float angle = eyeDir.angle(pEVec);
		return angle <= 0.2f;
	}

}