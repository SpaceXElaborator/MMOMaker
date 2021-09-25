package com.terturl.MMO.CustomArmorStandMobs;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.CustomArmorStandMobs.Utils.ArmorStandContainer;

import lombok.Getter;
import lombok.Setter;

public abstract class ArmorStandMob {

	protected HashMap<String, ArmorStandContainer> parts = new HashMap<>();
	protected BukkitRunnable currentAnimation;
	
	@Getter
	protected Location loc;

	@Getter
	@Setter
	protected ArmorStand central;

	public ArmorStandMob(Location loc) {
		this.loc = loc;
	}
	
	public void teleport(Location loc) {
		this.loc = loc;
		for(ArmorStandContainer asc : parts.values()) {
			asc.teleport(loc.clone());
		}
	}
	
	public abstract void startWalk();
	public abstract void stopWalk();
	
	public void runCircle() {
		BukkitRunnable br = new BukkitRunnable() {
			private float yaw = 0;
			public void run() {
				if(yaw >= 720) cancel();
				Location loc2 = loc.clone();
				loc2.setYaw(yaw);
				teleport(loc2);
				yaw+=5;
			}
		};
		br.runTaskTimer(MinecraftMMO.getInstance(), 0, 1);
	}

	public void Destroy() {
		for (ArmorStandContainer as : parts.values()) {
			as.getStand().remove();
		}
		parts.clear();
	}

}