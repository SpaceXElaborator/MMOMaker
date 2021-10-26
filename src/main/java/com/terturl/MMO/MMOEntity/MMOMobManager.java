package com.terturl.MMO.MMOEntity;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.terturl.MMO.MinecraftMMO;

import lombok.Getter;

/**
 * Handles the creation of MMOMobs and tracking of alive MMOMobs
 * 
 * @author Sean Rahman
 * @since 0.60.0
 *
 */
public class MMOMobManager {

	@Getter
	private List<MMOMobEntity> mobEntities = new ArrayList<>();

	@Getter
	private List<MMOMob> aliveMobs = new ArrayList<>();

	/**
	 * Creates the runnable that will tick every mob alive
	 */
	public MMOMobManager() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MinecraftMMO.getInstance(), new Runnable() {
			@Override
			public void run() {
				if (!aliveMobs.isEmpty()) {
					for (MMOMob m : aliveMobs) {
						m.tick();
					}
				}
			}
		}, 0, 1L);
	}

	/**
	 * Registers a MMOMobEntity that can be pulled from later for creating a new
	 * MMOMob
	 * 
	 * @param me MMOMobEntity to register
	 */
	public void registerMobEntity(MMOMobEntity me) {
		mobEntities.add(me);
	}

	/**
	 * Checks if the given name is the name of a spawnable MMOMob
	 * 
	 * @param s Name of MMOMob
	 * @return If mob exists or not
	 */
	public Boolean containsName(String s) {
		for (MMOMobEntity me : mobEntities) {
			if (me.getName().equalsIgnoreCase(s))
				return true;
		}
		return false;
	}

	/**
	 * Spawns an MMOMob with the given name at the given Location
	 * 
	 * @param s   Name of MMOMob from mobEntities
	 * @param loc Location to spawn MMOMob
	 * @return If the MMOMob was spawned or not
	 */
	public Boolean spawnMob(String s, Location loc) {
		if (!containsName(s)) {
			return false;
		}
		MMOMobEntity me = getMobEntity(s);
		MMOMob m = new MMOMob(me.getName(), me);
		m.setLocation(loc);
		m.generateASParts();
		for (Player p : Bukkit.getOnlinePlayers()) {
			m.spawnEntity(p);
		}
		aliveMobs.add(m);
		return true;
	}

	private MMOMobEntity getMobEntity(String s) {
		return mobEntities.stream().filter(e -> e.getName().equalsIgnoreCase(s)).findFirst().orElse(null);
	}

}