package com.terturl.MMO.Effects;

import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import com.terturl.MMO.Effects.Util.EffectInformation;
import com.terturl.MMO.Util.SoundInformation;

import lombok.Getter;
import lombok.Setter;

/**
 * An Effect that will shoot a straight line at an instant to the target point
 * the player is looking at x blocks away determined by the EffectInformations range
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class LineEffect extends Effect {

	@Getter
	@Setter
	private boolean penetrate = false;

	public LineEffect(EffectInformation info) {
		super(info);
	}

	@Override
	public void run() {
		EffectInformation ei = (EffectInformation) getEffectInformation().clone();
		for (SoundInformation s : ei.getSounds()) {
			ei.getPlayer().playSound(ei.getPlayer().getLocation(), s.getSound(), s.getVolume(), s.getPitch());
		}
		
		// Get a location if there is an offset
		Location loc = ei.getPlayer().getLocation().add(0, 1, 0).add(ei.getXOff(), ei.getYOff(), ei.getZOff()).clone();
		
		// Get the target, if it is point it will be where the player is looking
		Location target = ei.getLoc();
		
		// get the direction from loc to target
		Vector link = target.toVector().subtract(loc.toVector());
		
		// Get the length of link
		Double length = link.length();
		
		// Set link to 1
		link.normalize();
		
		// Get how much particles should be in each "length" of the line
		Double ratio = length / ei.getParticleAmount();
		
		// Get a new vector by multiplying the link by the ratio
		Vector v = link.multiply(ratio);
		
		// Create a new location by subtracting the Vector
		Location loc2 = loc.clone().subtract(v);
		
		for (int i = 0; i < ei.getParticleAmount(); i++) {
			
			// If there is damage, damage any nearby entities near to the beam created that has not been hurt before
			if (ei.getDamage() > 0) {
				for (Entity e : ei.getPlayer().getNearbyEntities(10, 10, 10)) {
					if (ei.getDamaged().contains(e))
						continue;
					if (loc2.getBlock().getLocation().equals(e.getLocation().getBlock().getLocation())) {
						if (!(e instanceof Damageable))
							continue;
						Damageable dam = (Damageable) e;
						dam.damage(ei.getDamage());
						ei.getDamaged().add(e);
						
						// If it should stop after hitting one entity, stop
						if (!penetrate)
							return;
					}
				}
			}
			
			// Finally, add the vector to loc2 and spawn the particle at loc2
			loc2.add(v);
			loc2.getWorld().spawnParticle(ei.getParticle(), loc2, 0);
		}
	}

}