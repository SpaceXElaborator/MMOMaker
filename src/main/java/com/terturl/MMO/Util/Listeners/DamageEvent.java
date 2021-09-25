package com.terturl.MMO.Util.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Player.MMOPlayer;

public class DamageEvent implements Listener {

	@EventHandler
	public void playerDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player)e.getEntity();
			if(!MinecraftMMO.getInstance().getPlayerHandler().PlayerExists(p)) return;
			MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
			mp.updateAndTakeDamage();
		}
	}
	
	@EventHandler
	public void effectProjectileHit(ProjectileHitEvent e) {
		Projectile proj = e.getEntity();
		MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getProjectile(proj);
		if(mp == null) return;
		proj.remove();
	}
	
	@EventHandler
	public void entityDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Projectile) {
			Projectile proj = (Projectile) e.getDamager();
			MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getProjectile(proj);
			if(mp == null) return;
			e.setDamage(mp.getProjectileMapping().get(proj.getUniqueId()));
			mp.getProjectileMapping().remove(proj.getUniqueId());
		}
	}
	
}