package com.terturl.MMO.Effects;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.terturl.MMO.Effects.Util.EffectInformation;
import com.terturl.MMO.Util.Math.RelativeLocation;

public class VectorPush extends Effect {

	public VectorPush(EffectInformation info) {
		super(info);
	}

	@Override
	public void run() {
		EffectInformation ei = getEffectInformation().clone();
		RelativeLocation rl = new RelativeLocation(ei.getYOff(), ei.getXOff(), ei.getZOff());
		Location loc = rl.getFromRelative(ei.getPlayer().getLocation());
		Vector v = loc.toVector().subtract(ei.getPlayer().getLocation().toVector()).normalize();
		ei.getPlayer().setVelocity(v.multiply(ei.getRange()));
	}

}