package com.terturl.MMO.MMOEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import com.terturl.MMO.MMOEntity.ArmorStand.ArmorStandPart;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOBone;

import lombok.Getter;
import lombok.Setter;

public class MMOMob {

	@Getter @Setter
	private UUID uuid;
	
	@Getter
	private String entityName;
	
	@Getter
	private Map<String, BBOBone> bones = new HashMap<>();

	@Getter
	private List<ArmorStandPart> parts = new ArrayList<>();
	
	@Getter
	private Map<String, Integer> textureIds;
	
	@Getter @Setter
	private Location location;
	
	@Getter @Setter
	private Double headAngle = 0.0D;
	
	public MMOMob(String s, MMOMobEntity entity) {
		entityName = s;
		uuid = UUID.randomUUID();
		entity.getBones().forEach((k, v) -> {
			bones.put(k, v);
		});
		textureIds = entity.getTextureMapping();
	}
	
	public void tick() {
		Player p = getClosestPlayer(10.0, 10.0, 10.0);
		if(p != null) {
			ArmorStandPart asp = parts.stream().filter(e -> e.isHead() == true).findFirst().orElse(null);
			if(asp != null) {
				// TODO: Need to figure out the math for the head rotation to get the nearest Player and look at it when walking aroud.
				asp.getStand().getEnt().setHeadRotation(0);
			}
		}
		// TODO: Start working on entity AI. When will it move? When will it target a player? Does it even target a player? How does the walking animation player? Things like that
	}
	
	public List<Entity> getNearEntities(Double x, Double y, Double z) {
		List<net.minecraft.world.entity.Entity> entities = parts.get(0).getStand().getEnt().t.getEntities(parts.get(0).getStand().getEnt(), parts.get(0).getStand().getEnt().getBoundingBox().grow(x, y, z));
		List<Entity> bukkitEntities = new ArrayList<>(entities.size());
		for(net.minecraft.world.entity.Entity e : entities) {
			bukkitEntities.add(e.getBukkitEntity());
		}
		return bukkitEntities;
	}
	
	public Player getClosestPlayer(Double x, Double y, Double z) {
		List<Entity> nearby = getNearEntities(x, y, z);
		List<Entity> nearbyP = nearby.stream().filter(e -> e instanceof Player).collect(Collectors.toList());
		if(nearbyP.size() <= 0) return null;
		Entity p = null;
		for(Entity e2 : nearbyP) {
			if(e2.getLocation().distanceSquared(location) < p.getLocation().distanceSquared(location)) {
				p = e2;
			}
		}
		return (Player)p;
	}
	
	public Entity getClosestEntity(Double x, Double y, Double z) {
		Entity e = null;
		List<Entity> nearby = getNearEntities(x, y, z);
		if(nearby.size() <= 0) return null;
		e = nearby.get(0);
		for(Entity e2 : nearby) {
			if(e2.getLocation().distanceSquared(location) < e.getLocation().distanceSquared(location)) {
				e = e2;
			}
		}
		return e;
	}
	
	public void spawnEntity(Player p) {
		parts.forEach(e -> {
			e.showModel(p);
		});
	}
	
	public BBOBone getBone(String name) {
		BBOBone bone = bones.get(name);
		if(bone == null) {
			for(BBOBone parents : bones.values()) {
				BBOBone b = parents.getChildren().get(name);
				if(b == null) continue;
				bone = b;
				break;
			}
		}
		return bone;
	}
	
	public void generateASParts() {
		bones.forEach((k, v) -> {
			Vector pos = new Vector(v.getLocalOffset()[0], v.getLocalOffset()[1], v.getLocalOffset()[2]);
			EulerAngle rot = new EulerAngle(v.getLocalRotation()[0], v.getLocalRotation()[1], v.getLocalRotation()[2]);
			if(v.getChildren().size() > 0) {
				ArmorStandPart part = new ArmorStandPart(this, v.getOptions().get("small"), k, pos, rot, null, textureIds.get(k.toLowerCase()));
				part.setSmall(v.getOptions().get("small"));
				part.init();
				part.generateASParts(this, v.getChildren());
				part.getStand().setMeta(textureIds.get(k.toLowerCase()));
				parts.add(part);
			}
		});
	}
	
}