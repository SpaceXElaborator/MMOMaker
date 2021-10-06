package com.terturl.MMO.Entity;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;

public class MMOEntityManager {

	private Map<String, MMOEntity> MMOEntities = new HashMap<>();
	
	public MMOEntityManager() {
		MMOEntity me = new MMOEntity(EntityConversion.ZOMBIE.getType(), ((CraftWorld)Bukkit.getWorlds().get(0)).getHandle(), "TestZombie");
		MMOEntities.put("TestZombie", me);
	}

	public MMOEntity getEntity(String name) {
		if(!MMOEntities.containsKey(name)) return null;
		return MMOEntities.get(name);
	}
	
}