package com.terturl.MMO.Effects;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.terturl.MMO.Effects.Util.EffectInformation;
import com.terturl.MMO.Util.EntityUtils;
import com.terturl.MMO.Util.SoundInformation;

/**
 * Ability that will cause the player to jump to near where the entity they are
 * looking at is
 * 
 * @author Sean Rahman
 * @since 0.51.0
 *
 */
public class JumpTo extends Effect {

	public JumpTo(EffectInformation ei) {
		super(ei);
	}

	public void run() {
		EffectInformation ei = getEffectInformation().clone();
		for (SoundInformation s : ei.getSounds()) {
			ei.getPlayer().playSound(ei.getPlayer().getLocation(), s.getSound(), s.getVolume(), s.getPitch());
		}
		Player p = ei.getPlayer();

		// Get all entities within range of EffectInformation's getRange()
		for (Entity e : p.getNearbyEntities(ei.getRange(), ei.getRange(), ei.getRange())) {
			// Checks for if the player is looking at the entity
			if (EntityUtils.playerIsLookingAtEntity(p, e)) {

				// Calculate where the player needs to go to based on their location and the
				// entities location
				Vector toGo = p.getLocation().toVector().subtract(e.getLocation().toVector()).normalize();
				double force = p.getLocation().distance(e.getLocation()) * 0.35;
				p.setVelocity(toGo.multiply(-force));
				if (ei.getDamage() >= 0.1) {
					Damageable dam = (Damageable) e;
					dam.damage(ei.getDamage());
				}
			}
		}
	}

}