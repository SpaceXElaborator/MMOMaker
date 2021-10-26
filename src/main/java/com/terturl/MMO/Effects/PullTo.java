package com.terturl.MMO.Effects;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.terturl.MMO.Effects.Util.EffectInformation;
import com.terturl.MMO.Util.EntityUtils;
import com.terturl.MMO.Util.SoundInformation;

/**
 * Ability that will pull an entity the player is looking at to near their
 * location
 * 
 * @author Sean Rahman
 * @since 0.51.0
 *
 */
public class PullTo extends Effect {

	public PullTo(EffectInformation ei) {
		super(ei);
	}

	public void run() {
		EffectInformation ei = getEffectInformation().clone();
		for (SoundInformation s : ei.getSounds()) {
			ei.getPlayer().playSound(ei.getPlayer().getLocation(), s.getSound(), s.getVolume(), s.getPitch());
		}
		Player p = ei.getPlayer();

		// Check for all entities near the player by a range set in the
		// EffectInformation
		for (Entity e : p.getNearbyEntities(ei.getRange(), ei.getRange(), ei.getRange())) {
			// Check to see if the player is looking at that entity
			if (EntityUtils.playerIsLookingAtEntity(p, e)) {

				// Calculate where the entity needs to fly to where the player is. Multiplying
				// by 0.35 helps with making sure that the entity will be as close as possible
				// without going over, this will fail after going beyond 11 blocks and can cause
				// the entity to fly over.
				
				// This can also fail if the player jumps causing the entity to fly over them
				Vector toGo = p.getLocation().toVector().subtract(e.getLocation().toVector()).normalize();
				double force = p.getLocation().distance(e.getLocation()) * 0.35;
				e.setVelocity(toGo.multiply(force));
				if (ei.getDamage() >= 0.1) {
					Damageable dam = (Damageable) e;
					dam.damage(ei.getDamage());
				}
			}
		}
	}

}
