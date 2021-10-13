package com.terturl.MMO.Effects;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import com.terturl.MMO.Effects.Util.EffectInformation;
import com.terturl.MMO.Util.SoundInformation;

public class VectorRelative extends Effect {

	public VectorRelative(EffectInformation info) {
		super(info);
	}

	@Override
	public void run() {
		EffectInformation ei = getEffectInformation().clone();
		for(SoundInformation s : ei.getSounds()) {
			ei.getPlayer().playSound(ei.getPlayer().getLocation(), s.getSound(), s.getVolume(), s.getPitch());
		}
		
		for(Entity e : ei.getPlayer().getNearbyEntities(2, 2, 2)) {
			if(ei.getDamaged().contains(e)) continue;
			if(!(e instanceof Damageable)) continue;
			Damageable dam = (Damageable)e;
			dam.damage(getEffectInformation().getDamage());
			ei.getDamaged().add(e);
		}
		
		Vector v = ei.getPlayer().getVelocity();
		ei.getPlayer().setVelocity(v.multiply(ei.getRange()));
	}

}