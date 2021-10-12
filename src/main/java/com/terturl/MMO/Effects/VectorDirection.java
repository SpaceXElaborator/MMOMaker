package com.terturl.MMO.Effects;

import org.bukkit.util.Vector;

import com.terturl.MMO.Effects.Util.EffectInformation;

public class VectorDirection extends Effect {

	public VectorDirection(EffectInformation info) {
		super(info);
	}

	@Override
	public void run() {
		EffectInformation ei = getEffectInformation().clone();
		Vector v = ei.getPlayer().getLocation().getDirection().normalize();
		ei.getPlayer().setVelocity(v.multiply(ei.getRange()));
	}

}