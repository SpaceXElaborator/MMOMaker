package com.terturl.MMO.Util;

import org.bukkit.entity.Projectile;

/**
 * Only handles the nasty try/catch and Java Reflection of attempting to find a
 * Spigot Projectile
 * 
 * @author Sean Rahman
 * @since 0.67.0
 *
 */
public class ProjectileClassFinder {

	/**
	 * Find class from org.bukkit.entity based on name of projectile
	 * 
	 * @param proj Projectile to find
	 * @return Class<? extends Projectile> or Null
	 */
	public static Class<? extends Projectile> findProjectile(String proj) {
		Class<? extends Projectile> projToSpawn = null;
		try {
			projToSpawn = Class.forName("org.bukkit.entity." + proj).asSubclass(Projectile.class);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		return projToSpawn;
	}

}