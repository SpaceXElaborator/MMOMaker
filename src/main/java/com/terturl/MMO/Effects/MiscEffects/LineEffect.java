package com.terturl.MMO.Effects.MiscEffects;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;

import com.terturl.MMO.Effects.Effect;

import lombok.Getter;

public class LineEffect extends Effect {

	@Getter
	private Particle particle;
	
	@Getter
	private double damage;
	
	@Getter
	private int range;
	
	@Getter
	private double particleAmount = 200.0;
	
	@Getter
	private double YOffset = 0.0;
	
	@Getter
	private double ZOffset = 0.0;
	
	@Getter
	private double XOffset = 0.0;
	
	@Getter
	private boolean penetratable = false;
	
	public void run(Player p) {
		// Get a location if there is an offset
		Location loc = p.getLocation().add(0, 1, 0).add(getXOffset(), getYOffset(), getZOffset()).clone();

		// Get the target, if it is point it will be where the player is looking
		Location target = p.getTargetBlock((Set<Material>) null, getRange()).getLocation().add(0.5, 0.5, 0.5).clone();

		// get the direction from loc to target
		Vector link = target.toVector().subtract(loc.toVector());

		// Get the length of link
		Double length = link.length();

		// Set link to 1
		link.normalize();

		// Get how much particles should be in each "length" of the line
		Double ratio = length / getParticleAmount();

		// Get a new vector by multiplying the link by the ratio
		Vector v = link.multiply(ratio);

		// Create a new location by subtracting the Vector
		Location loc2 = loc.clone().subtract(v);

		for (int i = 0; i < getParticleAmount(); i++) {

			// If there is damage, damage any nearby entities near to the beam created that
			// has not been hurt before
			if (getDamage() > 0) {
				for (Entity e : p.getNearbyEntities(10, 10, 10)) {
					if (hasDamaged(p, e))
						continue;
					if (loc2.getBlock().getLocation().equals(e.getLocation().getBlock().getLocation())) {
						if (!(e instanceof Damageable))
							continue;
						Damageable dam = (Damageable) e;
						dam.damage(getDamage());
						addToDamaged(p, e);

						// If it should stop after hitting one entity, stop
						if (!isPenetratable())
							return;
					}
				}
			}

			// Finally, add the vector to loc2 and spawn the particle at loc2
			loc2.add(v);
			loc2.getWorld().spawnParticle(getParticle(), loc2, 0);
		}
	}

	public void load(JSONObject jo) {
		particle = Particle.valueOf(jo.get("Particle").toString().toUpperCase());
		range = Integer.parseInt(jo.get("Range").toString());
		particleAmount = Double.parseDouble(jo.get("ParticleAmount").toString());
		damage = Double.parseDouble(jo.get("Damage").toString());
		penetratable = Boolean.parseBoolean(jo.get("Penetrate").toString());
		
		YOffset = jo.containsKey("YOffset") ? Double.parseDouble(jo.get("YOffset").toString()) : 0.0;
		XOffset = jo.containsKey("XOffset") ? Double.parseDouble(jo.get("XOffset").toString()) : 0.0;
		ZOffset = jo.containsKey("ZOffset") ? Double.parseDouble(jo.get("ZOffset").toString()) : 0.0;
	}

}