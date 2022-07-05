package com.terturl.MMO.Effects.VectorEffects;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.google.gson.JsonObject;
import com.terturl.MMO.Effects.Effect;
import com.terturl.MMO.Util.EntityUtils;

import lombok.Getter;

public class JumpTo extends Effect {

	@Getter
	private double range;
	
	@Getter
	private double damage;
	
	public void run(Player p) {
		for (Entity e : p.getNearbyEntities(getRange(), getRange(), getRange())) {
			// Checks for if the player is looking at the entity
			if (EntityUtils.playerIsLookingAtEntity(p, e)) {

				// Calculate where the player needs to go to based on their location and the
				// entities location
				Vector toGo = p.getLocation().toVector().subtract(e.getLocation().toVector()).normalize();
				double force = p.getLocation().distance(e.getLocation()) * 0.35;
				p.setVelocity(toGo.multiply(-force));
				if (getDamage() >= 0.1) {
					Damageable dam = (Damageable) e;
					dam.damage(getDamage());
				}
			}
		}
	}

	public void load(JsonObject jo) {
		range = jo.get("Range").getAsDouble();
		damage = jo.get("Damage").getAsDouble();
	}
	
}