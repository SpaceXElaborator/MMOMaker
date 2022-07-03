package com.terturl.MMO.Effects.PlayerEffects;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.json.simple.JSONObject;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Effects.Effect;

import lombok.Getter;

public class ProjectileEffect extends Effect {

	@Getter 
	private double damage;
	
	@Getter
	private double speed;
	
	@Getter
	private String projectile = "";

	public void run(Player p) {
		try {
			Class<? extends Projectile> projToSpawn = Class.forName("org.bukkit.entity." + getProjectile())
					.asSubclass(Projectile.class);
			
			Projectile proj = p.launchProjectile(projToSpawn,
					p.getLocation().getDirection().normalize().multiply(getSpeed()));
			
			MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p).getProjectileMapping().put(proj.getUniqueId(),
					getDamage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void load(JSONObject jo) {
		projectile = jo.get("Projectile").toString();
		speed = Double.parseDouble(jo.get("Speed").toString());
		damage = Double.parseDouble(jo.get("Damage").toString());
	}
	
}