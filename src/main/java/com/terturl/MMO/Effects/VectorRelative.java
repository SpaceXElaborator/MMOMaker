package com.terturl.MMO.Effects;

import org.bukkit.util.Vector;

import com.terturl.MMO.Effects.Util.EffectInformation;

public class VectorRelative extends Effect {

	public VectorRelative(EffectInformation info) {
		super(info);
	}

	@Override
	public void run() {
		EffectInformation ei = getEffectInformation().clone();
		Vector v = ei.getPlayer().getVelocity();
		ei.getPlayer().setVelocity(v.multiply(ei.getRange()));
	}

}