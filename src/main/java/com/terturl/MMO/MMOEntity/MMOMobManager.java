package com.terturl.MMO.MMOEntity;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.terturl.MMO.MinecraftMMO;

import lombok.Getter;

public class MMOMobManager {
	
	@Getter
	private List<MMOMobEntity> mobEntities = new ArrayList<>();
	
	@Getter
	private List<MMOMob> aliveMobs = new ArrayList<>();
	
	public MMOMobManager() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MinecraftMMO.getInstance(), new Runnable() {
			@Override
			public void run() {
				for(MMOMob m : aliveMobs) {
					m.tick();
				}
			}
		}, 0, 1L);
	}
	
	public void registerMobEntity(MMOMobEntity me) {
		mobEntities.add(me);
	}
	
	public Boolean containsName(String s) {
		for(MMOMobEntity me : mobEntities) {
			if(me.getName().equalsIgnoreCase(s)) return true;
		}
		return false;
	}
	
	public Boolean spawnMob(String s, Location loc) {
		if(!containsName(s)) {
			return false;
		}
		MMOMobEntity me = getMobEntity(s);
		MMOMob m = new MMOMob(me.getName(), me);
		m.setLocation(loc);
		m.generateASParts();
		for(Player p : Bukkit.getOnlinePlayers()) {
			m.spawnEntity(p);
		}
		aliveMobs.add(m);
		return true;
	}
	
	private MMOMobEntity getMobEntity(String s) {
		return mobEntities.stream().filter(e -> e.getName().equalsIgnoreCase(s)).findFirst().orElse(null);
	}
	
}