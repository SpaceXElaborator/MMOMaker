package com.terturl.MMO.CustomArmorStandMobs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.terturl.MMO.CustomArmorStandMobs.Boss.MagmaBoss;

public class ArmorStandMobController {

	private List<ArmorStandMob> mobs = new ArrayList<>();
	
	public void clear() {
		mobs.stream().forEach(e -> {
			e.Destroy();
		});
		mobs.clear();
	}

	public void teleport(Location loc) {
		mobs.get(0).teleport(loc);
	}
	
	public void runWalk() {
		mobs.get(0).startWalk();
	}
	
	public void stopWalk() {
		mobs.get(0).stopWalk();
	}
	
	public void spawnMagmaBoss(Location location) {
		MagmaBoss mb = new MagmaBoss(location);
		mobs.add(mb);
	}

	public void rotate() {
		mobs.get(0).runCircle();
	}
	
}