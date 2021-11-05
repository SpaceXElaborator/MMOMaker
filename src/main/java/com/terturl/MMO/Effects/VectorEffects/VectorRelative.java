package com.terturl.MMO.Effects.VectorEffects;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;

import com.terturl.MMO.Effects.Effect;

import lombok.Getter;

public class VectorRelative extends Effect {

	@Getter
	private Double damage;
	
	@Getter
	private Double range;
	
	public void run(Player p) {
		if(getDamage() > 0) {
			for(Entity e : p.getNearbyEntities(2, 2, 2)) {
				if(hasDamaged(p, e)) continue;
				if(!(e instanceof Damageable)) continue;
				Damageable dam = (Damageable)e;
				dam.damage(getDamage());
				addToDamaged(p, e);
			}
		}
		
		Vector v = p.getVelocity();
		p.setVelocity(v.multiply(getRange()));
	}

	public void load(JSONObject jo) {
		damage = Double.parseDouble(jo.get("Damage").toString());
		range = Double.parseDouble(jo.get("Range").toString());
	}
	
}