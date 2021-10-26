package com.terturl.MMO.Effects;

import org.bukkit.util.Vector;

import com.terturl.MMO.Effects.Util.EffectInformation;
import com.terturl.MMO.Util.ParticleManager;
import com.terturl.MMO.Util.SoundInformation;

import lombok.Getter;
import lombok.Setter;

/**
 * An effect that spawns a sphere with the offset away from the player
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class Sphere extends Effect {

	public Sphere(EffectInformation info) {
		super(info);
	}

	@Getter
	@Setter
	private double radius;

	@Override
	public void run() {
		EffectInformation ei = getEffectInformation().clone();
		for (SoundInformation s : ei.getSounds()) {
			ei.getPlayer().playSound(ei.getPlayer().getLocation(), s.getSound(), s.getVolume(), s.getPitch());
		}
		ei.setLoc(ei.getLoc().add(ei.getXOff(), ei.getYOff(), ei.getZOff()));
		for (long i = 0; i < ei.getParticleAmount(); i++) {
			Vector v = ParticleManager.getRandomVector().multiply(getRadius());
			ei.getLoc().add(v);
			ei.getPlayer().getLocation().getWorld().spawnParticle(ei.getParticle(), ei.getLoc(), 0);
			ei.getLoc().subtract(v);
		}
	}

}