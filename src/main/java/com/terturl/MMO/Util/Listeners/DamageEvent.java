package com.terturl.MMO.Util.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.terturl.MMO.MinecraftMMO;
import com.terturl.MMO.Entity.MMOEntity;
import com.terturl.MMO.Player.MMOPlayer;
import com.terturl.MMO.Util.SoundInformation;

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
		
		if(e.getDamager() instanceof Player) {
			Player p = (Player)e.getDamager();
			if(!MinecraftMMO.getInstance().getPlayerHandler().PlayerExists(p)) return;
			MMOPlayer mp = MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p);
			mp.updateAndTakeDamage();
			
			if(MinecraftMMO.getInstance().getEntityManager().containsUUID(e.getEntity().getUniqueId())) {
				MMOEntity me = MinecraftMMO.getInstance().getEntityManager().getEntity(e.getEntity().getUniqueId());
				SoundInformation si = me.getMMOSoundHurt();
				if(si == null) return;
				p.playSound(e.getEntity().getLocation(), si.getSound(), si.getVolume(), si.getPitch());
			}
			
		}
	}
	
}