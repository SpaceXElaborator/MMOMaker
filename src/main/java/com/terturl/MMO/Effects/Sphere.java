package com.terturl.MMO.Effects;

import org.bukkit.util.Vector;

import com.terturl.MMO.Effects.Util.EffectInformation;
import com.terturl.MMO.Util.ParticleManager;

import lombok.Getter;
import lombok.Setter;

public class Sphere extends Effect {

	public Sphere(EffectInformation info) {
		super(info);
	}

	@Getter @Setter
	private double radius;
	
	@Override
	public void run() {
		EffectInformation ei = getEffectInformation().clone();
		ei.setLoc(ei.getLoc().add(ei.getXOff(), ei.getYOff(), ei.getZOff()));
		for(long i = 0; i < ei.getParticleAmount(); i++) {
			Vector v = ParticleManager.getRandomVector().multiply(getRadius());
			ei.getLoc().add(v);
			ei.getPlayer().getLocation().getWorld().spawnParticle(ei.getParticle(), ei.getLoc(), 0);
			ei.getLoc().subtract(v);
		}
	}
	
}