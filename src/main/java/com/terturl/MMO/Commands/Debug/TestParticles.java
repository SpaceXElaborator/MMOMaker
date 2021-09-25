package com.terturl.MMO.Commands.Debug;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.terturl.MMO.Framework.CraftCommand;

public class TestParticles extends CraftCommand {

	public TestParticles() {
		super("particle");
	}
	
	public void handleCommand(Player p, String[] args) {
		if(args[0].equalsIgnoreCase("circle")) {
			Location loc = p.getLocation().clone();
			for(Vector vec : getArcCircle(p, 270.0, 4)) {
				Vector v = p.getLocation().getDirection().normalize().add(vec);
				loc.add(v);
				loc.setY(p.getLocation().getY() + 1);
				p.getWorld().spawnParticle(Particle.FLAME, loc, 0);
				loc.subtract(v);
			}
		}
	}
	
	public List<Vector> getArcCircle(Player p, Double degree, Integer times) {
		// This list is really just a test. It's so I can see the particles above of where the arc is
		List<Vector> locs = new ArrayList<>();
		int radius = 2;
		// For some reason, my math is wrong, so if I put 4 times, it will spawn 5 arrows. So I have a lazy fix of just subtracting one. Works like a charm
		times = times - 1;
		double yaw = p.getLocation().getYaw() + (90 - (degree/2.0));
		Location loc = p.getLocation().clone();
		// a and b are used to tell me how often to spawn a projectile
		int a = (int) (degree/times);
		int b = 0;
		for(double theta = 0; theta <= degree; theta++) {
			// Create an X and Z based being two units away from the player
			double x = radius * Math.cos(Math.toRadians(yaw + theta));
			double z = radius * Math.sin(Math.toRadians(yaw + theta));
			Vector v = new Vector(x, 0, z);
			// If b hits the point as defined by a (or is the start of the cycle) spawn a projectile
			if(b == a || b == 0) {
				Vector vec = p.getLocation().getDirection().normalize().add(v);
				loc.add(vec);
				loc.setY(p.getLocation().getY() + 1);
				p.launchProjectile(Arrow.class, vec);
				loc.subtract(vec);
				b = 1;
			}
			locs.add(v);
			b++;
		}
		return locs;
	}
	
}