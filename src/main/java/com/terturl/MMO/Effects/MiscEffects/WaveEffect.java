package com.terturl.MMO.Effects.MiscEffects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Effects.Effect;

import lombok.Getter;

public class WaveEffect extends Effect {

	@Getter
	private Double range;
	
	@Getter
	private Double degree;
	
	@Getter
	private Double damage;
	
	@Getter
	private Particle particle;
	
	private Integer taskId;
	
	public void run(Player p) {
		// Get the direction the player is looking
		double yaw = p.getLocation().getYaw();
		double range = getRange();

		// Create a step amount to increase the radius of the arc that will be created
		double step = getDegree() / (range * 5.0);

		Location loc = p.getLocation().clone();
		Vector v = p.getLocation().getDirection().normalize();

		Runnable r = new Runnable() {
			// set parameters for the arc to be created
			double d_degree = step;
			// offset the arc so that it the center of the arc is directly in front of the
			// player
			double currentArc = yaw + (90 - (d_degree / 2.0));
			double radius = 0.2;

			@Override
			public void run() {
				// Get where the x/z cords need to be placed in relation to the current arc,
				// radius, and theta
				for (double theta = 0; theta <= d_degree; theta++) {
					double x = radius * Math.cos(Math.toRadians(currentArc + theta));
					double z = radius * Math.sin(Math.toRadians(currentArc + theta));

					// Create the new vector and make sure that it'll stay low to the ground
					Vector vec = new Vector(x, 0, z);
					Vector v_2 = v.clone().add(vec);
					v_2.setY(0.2);
					loc.add(v_2);

					if (getDamage() > 0) {
						for (Entity e : p.getNearbyEntities(getRange(), getRange(), getRange())) {
							if (hasDamaged(p, e))
								continue;
							if (loc.getBlock().getLocation().equals(e.getLocation().getBlock().getLocation())) {
								if (!(e instanceof Damageable))
									continue;
								Damageable dam = (Damageable) e;
								dam.damage(getDamage());
								addToDamaged(p, e);
							}
						}
					}

					// Make sure to spawn the particle and subtract the vector so the location is
					// back to what it was
					p.getWorld().spawnParticle(getParticle(), loc, 0);
					loc.subtract(v_2);
				}

				// Increase the radius, step, and reset the currentArc
				radius += 0.2;
				d_degree += step;
				currentArc = yaw + (90 - (d_degree / 2.0));

				if (radius > range) {
					stop();
				}
			}
		};
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MinecraftMMO.getInstance(), r, 0L, 1L);
	}
	
	private void stop() {
		Bukkit.getScheduler().cancelTask(taskId);
	}

	public void load(JSONObject jo) {
		damage = Double.parseDouble(jo.get("Damage").toString());
		range = Double.parseDouble(jo.get("Range").toString());
		degree = Double.parseDouble(jo.get("Degree").toString());
		
		particle = Particle.valueOf(jo.get("Particle").toString().toUpperCase());
	}

}