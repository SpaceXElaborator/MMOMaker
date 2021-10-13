package com.terturl.MMO.Effects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Effects.Util.EffectInformation;
import com.terturl.MMO.Util.SoundInformation;

import lombok.Getter;
import lombok.Setter;

public class WaveEffect extends Effect {

	@Getter @Setter
	private Double degree;
	
	private int taskId;
	
	public WaveEffect(EffectInformation ei) {
		super(ei);
	}
	
	public void run() {
		EffectInformation ei = getEffectInformation().clone();
		for(SoundInformation s : ei.getSounds()) {
			ei.getPlayer().playSound(ei.getPlayer().getLocation(), s.getSound(), s.getVolume(), s.getPitch());
		}

		double yaw = ei.getPlayer().getLocation().getYaw();
		double range = ei.getRange();
		double step = degree/(range*5.0);
		
		Location loc = ei.getPlayer().getLocation().clone();
		Vector v = ei.getPlayer().getLocation().getDirection().normalize();
		
		Runnable r = new Runnable() {
			double d_degree = step;
			double currentArc = yaw + (90 - (d_degree/2.0));
			double radius = 0.2;
			@Override
			public void run() {
				for(double theta = 0; theta <= d_degree; theta++) {
					double x = radius * Math.cos(Math.toRadians(currentArc + theta));
					double z = radius * Math.sin(Math.toRadians(currentArc + theta));
					Vector vec = new Vector(x, 0, z);
					Vector v_2 = v.clone().add(vec);
					v_2.setY(0.2);
					loc.add(v_2);
					
					if(ei.getDamage() > 0) {
						for(Entity e : ei.getPlayer().getNearbyEntities(ei.getRange(),ei.getRange(),ei.getRange())) {
							if(ei.getDamaged().contains(e)) continue;
							if(loc.getBlock().getLocation().equals(e.getLocation().getBlock().getLocation())) {
								if(!(e instanceof Damageable)) continue;
								Damageable dam = (Damageable)e;
								dam.damage(ei.getDamage());
								ei.getDamaged().add(e);
							}
						}
					}
					
					ei.getPlayer().getWorld().spawnParticle(ei.getParticle(), loc, 0);
					loc.subtract(v_2);
				}
				
				radius += 0.2;
				d_degree += step;
				currentArc = yaw + (90 - (d_degree/2.0));
				
				if(radius > range) {
					stop();
				}
			}
		};
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MinecraftMMO.getInstance(), r, 0L, 1L);
	}
	
	public void stop() {
		Bukkit.getScheduler().cancelTask(taskId);
	}
	
}