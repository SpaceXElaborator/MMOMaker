package com.terturl.MMO.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import lombok.Getter;
import net.minecraft.server.level.WorldServer;

public class MMOEntityManager {

	private Map<String, MMOEntity> MMOEntities = new HashMap<>();
	
	@Getter
	private List<MMOEntity> aliveEntities = new ArrayList<>();
	
	public MMOEntityManager() {
		MMOEntity me = new MMOEntity(EntityConversion.ZOMBIE.getType(), ((CraftWorld)Bukkit.getWorlds().get(0)).getHandle(), "TestZombie");
		MMOEntities.put("TestZombie", me);
	}

	public MMOEntity getEntity(String name) {
		if(!MMOEntities.containsKey(name)) return null;
		return MMOEntities.get(name);
	}
	
	public void spawnEntity(Location loc, MMOEntity me) {
		WorldServer world = ((CraftWorld)loc.getWorld()).getHandle();
		MMOEntity mmoE = me.clone();
		world.addEntity(mmoE);
		mmoE.safeTeleport(loc.getX(), loc.getY(), loc.getZ(), false, TeleportCause.COMMAND);
		aliveEntities.add(mmoE);
	}
	
}