package com.terturl.MMO.Effects.MiscEffects;
//package com.terturl.MMO.NewEffects.MiscEffects;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.bukkit.Bukkit;
//import org.bukkit.Location;
//import org.bukkit.entity.Player;
//import org.bukkit.entity.Projectile;
//import org.bukkit.util.Vector;
//
//import com.terturl.MMO.MinecraftMMO;
//import com.terturl.MMO.Effects.Effect;
//import com.terturl.MMO.Effects.FireProjectile;
//import com.terturl.MMO.Effects.Util.EffectInformation;
//import com.terturl.MMO.Util.ProjectileClassFinder;
//import com.terturl.MMO.Util.SoundInformation;
//
//import lombok.Getter;
//import lombok.Setter;
//import net.md_5.bungee.api.ChatColor;
//
///**
// * A special kind of effect that fires multiple effects in a cone around the
// * front of the player determined by the degree set by the EffectInformation
// * 
// * @author Sean Rahman
// * @since 0.25.0
// *
// */
//public class ConeEffect extends Effect {
//
//	@Getter
//	private List<Effect> effects = new ArrayList<>();
//
//	@Getter
//	@Setter
//	private Double degree;
//
//	public ConeEffect(EffectInformation ei) {
//		super(ei);
//	}
//
//	public void run() {
//		EffectInformation ei = (EffectInformation) getEffectInformation().clone();
//		for (SoundInformation s : ei.getSounds()) {
//			ei.getPlayer().playSound(ei.getPlayer().getLocation(), s.getSound(), s.getVolume(), s.getPitch());
//		}
//		double radius = ei.getRange();
//		int times = (effects.size() - 1);
//
//		// Sets the displacement from the left of the player so that the center of the
//		// arc is in line with the center of the players vision
//		double yaw = ei.getPlayer().getLocation().getYaw() + (90 - (degree / 2.0));
//
//		int a = 0;
//		boolean single = false;
//
//		// Account for if some reason you want to have a single item thrown
//		if (times == 1) {
//			a = (int) (degree / 2);
//			single = true;
//		} else {
//			a = (int) (degree / times);
//		}
//
//		int b = 0;
//		int effectNum = 0;
//
//		for (double theta = 0; theta <= degree; theta++) {
//			// Create an X and Z based being two units away from the player
//			double x = radius * Math.cos(Math.toRadians(yaw + theta));
//			double z = radius * Math.sin(Math.toRadians(yaw + theta));
//			Vector v = new Vector(x, 0, z);
//
//			if (b == a || (b == 0 && single == false)) {
//				Effect e = effects.get(effectNum);
//				EffectInformation ei2 = e.getEffectInformation().clone();
//				Player p = ei.getPlayer();
//
//				if (e instanceof FireProjectile) {
//					// If it is a projectile, create a new instance of the projectile and launch it
//					// from the player based on the vector from the arc
//					FireProjectile fp = (FireProjectile) e;
//					Class<? extends Projectile> projToSpawn = ProjectileClassFinder.findProjectile(fp.getProjectile());
//					
//					if(projToSpawn == null) {
//						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Unable to find Projectile: " + fp.getProjectile());
//						break;
//					}
//					
//					Projectile proj = p.launchProjectile(projToSpawn,
//							p.getLocation().getDirection().normalize().add(v).normalize().multiply(ei2.getRange()));
//					
//					// Add the projectile to the players projectile list to identified where the projectile came from
//					MinecraftMMO.getInstance().getPlayerHandler().getPlayer(p).getProjectileMapping()
//							.put(proj.getUniqueId(), ei2.getDamage());
//				} else {
//					// Perform like normal if not a projectile
//					ei2.setPlayer(ei.getPlayer());
//					Location loc = p.getEyeLocation().clone();
//					Vector inFrontOf = loc.getDirection().normalize().add(v).normalize().multiply(ei2.getRange());
//					inFrontOf.setY(0);
//					ei2.setLoc(loc.add(inFrontOf));
//					e.setEffectInformation(ei2);
//					e.run();
//					ei2.setLoc(ei2.getLoc().subtract(v));
//					e.setEffectInformation(ei2);
//				}
//
//				b = 1;
//				effectNum++;
//			}
//			b++;
//		}
//	}
//
//}