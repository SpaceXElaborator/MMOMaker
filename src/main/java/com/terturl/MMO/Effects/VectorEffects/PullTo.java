package com.terturl.MMO.Effects.VectorEffects;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;

import com.terturl.MMO.Effects.Effect;
import com.terturl.MMO.Util.EntityUtils;

import lombok.Getter;

public class PullTo extends Effect {

	@Getter
	private Double range;
	
	@Getter
	private Double damage;
	
	public void run(Player p) {
		for (Entity e : p.getNearbyEntities(getRange(), getRange(), getRange())) {
			// Check to see if the player is looking at that entity
			if (EntityUtils.playerIsLookingAtEntity(p, e)) {

				// Calculate where the entity needs to fly to where the player is. Multiplying
				// by 0.35 helps with making sure that the entity will be as close as possible
				// without going over, this will fail after going beyond 11 blocks and can cause
				// the entity to fly over.
				
				// This can also fail if the player jumps causing the entity to fly over them
				Vector toGo = p.getLocation().toVector().subtract(e.getLocation().toVector()).normalize();
				double force = p.getLocation().distance(e.getLocation()) * 0.35;
				e.setVelocity(toGo.multiply(force));
				if (getDamage() >= 0.1) {
					Damageable dam = (Damageable) e;
					dam.damage(getDamage());
				}
			}
		}
	}

	public void load(JSONObject jo) {
		range = Double.parseDouble(jo.get("Range").toString());
		damage = Double.parseDouble(jo.get("Damage").toString());
	}
	
}