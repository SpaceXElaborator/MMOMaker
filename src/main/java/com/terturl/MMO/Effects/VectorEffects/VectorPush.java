package com.terturl.MMO.Effects.VectorEffects;

import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.google.gson.JsonObject;
import com.terturl.MMO.Effects.Effect;
import com.terturl.MMO.Util.Math.RelativeLocation;

import lombok.Getter;

public class VectorPush extends Effect {

	@Getter
	private double range;
	
	@Getter
	private double damage;
	
	@Getter
	private double YOffset;
	
	@Getter
	private double ZOffset;
	
	@Getter
	private double XOffset;
	
	@Override
	public void run(Player p) {
		if (getDamage() > 0) {
			for (Entity e : p.getNearbyEntities(2, 2, 2)) {
				if (hasDamaged(p, e))
					continue;
				if (!(e instanceof Damageable))
					continue;
				Damageable dam = (Damageable) e;
				dam.damage(getDamage());
				addToDamaged(p, e);
			}
		}

		// Get the relative location away from a player with the EffectInformation offset
		RelativeLocation rl = new RelativeLocation(getYOffset(), getXOffset(), getZOffset());
		// Create the location from the relative location
		Location loc = rl.getFromRelative(p.getLocation());
		// Get the velocity to the location and normalize it (set it to 1)
		Vector v = loc.toVector().subtract(p.getLocation().toVector()).normalize();
		// Send the player in that direction multiplied by the range of EffectInformation
		p.setVelocity(v.multiply(getRange()));
	}

	@Override
	public void load(JsonObject jo) {
		damage = jo.get("Damage").getAsDouble();
		range = jo.get("Range").getAsDouble();
		
		YOffset = jo.get("YOffset").getAsDouble();
		XOffset = jo.get("XOffset").getAsDouble();
		ZOffset = jo.get("ZOffset").getAsDouble();
	}
	
}