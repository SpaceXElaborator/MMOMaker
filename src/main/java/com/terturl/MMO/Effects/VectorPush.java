package com.terturl.MMO.Effects;

import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import com.terturl.MMO.Effects.Util.EffectInformation;
import com.terturl.MMO.Util.SoundInformation;
import com.terturl.MMO.Util.Math.RelativeLocation;

/**
 * A velocity based ability that will push the player to a target offset from
 * the player
 * 
 * @author Sean Rahman
 * @since 0.49.0
 *
 */
public class VectorPush extends Effect {

	public VectorPush(EffectInformation info) {
		super(info);
	}

	@Override
	public void run() {
		EffectInformation ei = getEffectInformation().clone();
		for (SoundInformation s : ei.getSounds()) {
			ei.getPlayer().playSound(ei.getPlayer().getLocation(), s.getSound(), s.getVolume(), s.getPitch());
		}

		if (ei.getDamage() > 0) {
			for (Entity e : ei.getPlayer().getNearbyEntities(2, 2, 2)) {
				if (ei.getDamaged().contains(e))
					continue;
				if (!(e instanceof Damageable))
					continue;
				Damageable dam = (Damageable) e;
				dam.damage(ei.getDamage());
				ei.getDamaged().add(e);
			}
		}

		// Get the relative location away from a player with the EffectInformation offset
		RelativeLocation rl = new RelativeLocation(ei.getYOff(), ei.getXOff(), ei.getZOff());
		// Create the location from the relative location
		Location loc = rl.getFromRelative(ei.getPlayer().getLocation());
		// Get the velocity to the location and normalize it (set it to 1)
		Vector v = loc.toVector().subtract(ei.getPlayer().getLocation().toVector()).normalize();
		// Send the player in that direction multiplied by the range of EffectInformation
		ei.getPlayer().setVelocity(v.multiply(ei.getRange()));
	}

}