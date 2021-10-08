package com.terturl.MMO.Effects;

import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import com.terturl.MMO.Effects.Util.EffectInformation;
import com.terturl.MMO.Util.SoundInformation;

import lombok.Getter;
import lombok.Setter;

public class LineEffect extends Effect {
	
	@Getter @Setter
	private boolean penetrate = false;
	
	public LineEffect(EffectInformation info) {
		super(info);
	}

	@Override
	public void run() {
		EffectInformation ei = (EffectInformation) getEffectInformation().clone();
		for(SoundInformation s : ei.getSounds()) {
			ei.getPlayer().playSound(ei.getPlayer().getLocation(), s.getSound(), s.getVolume(), s.getPitch());
		}
		Location loc = ei.getPlayer().getLocation().add(0, 1, 0).add(ei.getXOff(), ei.getYOff(), ei.getZOff()).clone();
		Location target = ei.getLoc();
		Vector link = target.toVector().subtract(loc.toVector());
		float length = (float)link.length();
		link.normalize();
		float ratio = length / ei.getParticleAmount();
		Vector v = link.multiply(ratio);
		Location loc2 = loc.clone().subtract(v);
		for(int i = 0; i < ei.getParticleAmount(); i++) {
			if(ei.getDamage() > 0) {
				for(Entity e : ei.getPlayer().getNearbyEntities(10, 10, 10)) {
					if(ei.getDamaged().contains(e)) continue;
					if(loc2.getBlock().getLocation().equals(e.getLocation().getBlock().getLocation())) {
						if(!(e instanceof Damageable)) continue;
						Damageable dam = (Damageable)e;
						dam.damage(getEffectInformation().getDamage());
						ei.getDamaged().add(e);
						if(!penetrate) return;
					}
				}
			}
			loc2.add(v);
			loc2.getWorld().spawnParticle(ei.getParticle(), loc2, 0);
		}
	}
	
}