package com.terturl.MMO.Util;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class ParticleManager {
	
	public static void spawnCylinder(Location loc, Integer r) {
		for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
			double radius = r;
			double y = Math.cos(i);
			for (double a = 0; a < Math.PI * 2; a += Math.PI / 10) {
				double x = Math.cos(a) * radius;
				double z = Math.sin(a) * radius;
				loc.add(x, y, z);
				loc.getWorld().spawnParticle(Particle.FLAME, loc, 0);
				loc.subtract(x, y, z);
			}
		}
	}
	
	public static void spawnPool(Location loc, Integer r) {
		int rad = 0;
		boolean goingUp = true;
		for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
			double y = Math.cos(i);
			for (double a = 0; a < Math.PI * 2; a += Math.PI / 10) {
				double x = Math.cos(a) * rad;
				double z = Math.sin(a) * rad;
				loc.add(x, y, z);
				loc.getWorld().spawnParticle(Particle.FLAME, loc, 0);
				loc.subtract(x, y, z);
			}
			if(goingUp)
				rad += 1;
			else
				rad -= 1;
			if(rad == r) goingUp = false;
		}
	}
	
	public static void spawnSphere(Location loc, Particle p, int particleAmount, Double r) {
		for(int i = 0; i < particleAmount; i++) {
			Vector v = getRandomVector().multiply(r);
			loc.add(v);
			loc.getWorld().spawnParticle(p, loc, 0);
			loc.subtract(v);
		}
	}
	
	public static Vector getRandomVector() {
		Random r = new Random(System.nanoTime());
		double x = r.nextDouble() * 2 - 1;
		double y = r.nextDouble() * 2 - 1;
		double z = r.nextDouble() * 2 - 1;
		
		return new Vector(x, y, z).normalize();
	}

}