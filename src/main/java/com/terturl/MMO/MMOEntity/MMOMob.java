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

import com.terturl.MMO.MMOEntity.Animation.Animation;
import com.terturl.MMO.MMOEntity.Animation.AnimationManager;
import com.terturl.MMO.MMOEntity.ArmorStand.ArmorStandPart;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOBone;

import lombok.Getter;
import lombok.Setter;

/**
 * A finished product of the actual Mob the player will see in game. Will
 * eventually hold information like abilities the mob can use, health, stats,
 * and more
 * 
 * @author Sean Rahman
 * @since 0.60.0
 *
 */
public class MMOMob {

	@Getter
	@Setter
	private UUID uuid;

	@Getter
	private String entityName;

	@Getter
	private Map<String, BBOBone> bones = new HashMap<>();

	@Getter
	private List<ArmorStandPart> parts = new ArrayList<>();

	@Getter
	private Map<String, Integer> textureIds;

	@Getter
	@Setter
	private Location location;

	@Getter
	@Setter
	private AnimationManager walkingAnimation;

	public MMOMob(String s, MMOMobEntity entity) {
		entityName = s;
		uuid = UUID.randomUUID();
		entity.getBones().forEach((k, v) -> {
			bones.put(k, v);
		});
		textureIds = entity.getTextureMapping();
		Animation animWalking = entity.getAnimations().stream().filter(e -> e.getName().equalsIgnoreCase("walking"))
				.findFirst().orElse(null);
		if (animWalking != null) {
			walkingAnimation = new AnimationManager(this, animWalking);
		}
	}

	/**
	 * Called every Minecraft tick to determine what the MMOMob needs to be doing.
	 * This will eventually hold animations and MMOMob AI for the Mob to move and
	 * live
	 */
	public void tick() {
		Player p = getClosestPlayer(10.0, 10.0, 10.0);
		if (p != null) {
			ArmorStandPart asp = getHead();
			if (asp != null) {
				lookAtPlayer(p, asp);
			}
		}
		if (walkingAnimation != null) {
			walkingAnimation.tick();
		}
		for (ArmorStandPart part : parts)
			part.update();
		// TODO: Start working on entity AI. When will it move? When will it target a
		// player? Does it even target a player? How does the walking animation player?
		// Things like that
	}

	/**
	 * Generate a list of near by entities taken from Minecrafts source code
	 * 
	 * @param x X range
	 * @param y Y range
	 * @param z Z range
	 * @return List of Entities near the ArmorStand
	 */
	public List<Entity> getNearEntities(Double x, Double y, Double z) {
		List<net.minecraft.world.entity.Entity> entities = parts.get(0).getStand().getEnt().t.getEntities(
				parts.get(0).getStand().getEnt(), parts.get(0).getStand().getEnt().getBoundingBox().grow(x, y, z));
		List<Entity> bukkitEntities = new ArrayList<>(entities.size());
		for (net.minecraft.world.entity.Entity e : entities) {
			bukkitEntities.add(e.getBukkitEntity());
		}
		return bukkitEntities;
	}

	/**
	 * Finds the nearest player to the MMOMob
	 * 
	 * @param x X range
	 * @param y Y range
	 * @param z Z range
	 * @return Nearest player or null
	 */
	public Player getClosestPlayer(Double x, Double y, Double z) {
		List<Entity> nearby = getNearEntities(x, y, z);
		List<Entity> nearbyP = nearby.stream().filter(e -> e instanceof Player).collect(Collectors.toList());
		if (nearbyP.size() <= 0)
			return null;
		Entity p = nearbyP.get(0);
		for (Entity e2 : nearbyP) {
			if (e2.getLocation().distanceSquared(location) < p.getLocation().distanceSquared(location)) {
				p = e2;
			}
		}
		return (Player) p;
	}

	/**
	 * Finds the closest entity to the MMOMob
	 * 
	 * @param x X range
	 * @param y Y range
	 * @param z Z range
	 * @return Nearest entity or null
	 */
	public Entity getClosestEntity(Double x, Double y, Double z) {
		Entity e = null;
		List<Entity> nearby = getNearEntities(x, y, z);
		if (nearby.size() <= 0)
			return null;
		e = nearby.get(0);
		for (Entity e2 : nearby) {
			if (e2.getLocation().distanceSquared(location) < e.getLocation().distanceSquared(location)) {
				e = e2;
			}
		}
		return e;
	}

	/**
	 * Returns the ArmorStandPart from the given name
	 * 
	 * @param s Name of ArmorStandPart
	 * @return ArmorStandPart or Null
	 */
	public ArmorStandPart getArmorStandPart(String s) {
		ArmorStandPart part = null;
		for (ArmorStandPart asp : parts) {
			if (asp.getPartId().equalsIgnoreCase(s)) {
				part = asp;
				break;
			}
			part = getArmorStandPartChild(asp, s);
			if (part != null)
				break;
		}
		return part;
	}

	private ArmorStandPart getArmorStandPartChild(ArmorStandPart part, String s) {
		for (ArmorStandPart parts : part.getChildren()) {
			if (parts.getPartId().equalsIgnoreCase(s)) {
				return parts;
			}
		}
		return null;
	}

	/**
	 * Spawns the MMOMob into the world for the player to view
	 * 
	 * @param p Player to view MMOMob
	 */
	public void spawnEntity(Player p) {
		parts.forEach(e -> {
			e.showModel(p);
		});
	}

	/**
	 * Game the BBOBone from the given name
	 * 
	 * @param name Name of BBOBone
	 * @return BBOBone or null
	 */
	public BBOBone getBone(String name) {
		BBOBone bone = bones.get(name);
		if (bone == null) {
			for (BBOBone parents : bones.values()) {
				BBOBone b = parents.getChildren().get(name);
				if (b == null)
					continue;
				bone = b;
				break;
			}
		}
		return bone;
	}

	/**
	 * Generates all the parent level BBOBones and cascades down to create the
	 * branch of ArmorStandParts to be viewed by the player
	 */
	public void generateASParts() {
		bones.forEach((k, v) -> {
			Vector pos = new Vector(v.getLocalOffset()[0], v.getLocalOffset()[1], v.getLocalOffset()[2]);
			EulerAngle rot = new EulerAngle(v.getLocalRotation()[0], v.getLocalRotation()[1], v.getLocalRotation()[2]);
			if (v.getChildren().size() > 0) {
				ArmorStandPart part = new ArmorStandPart(this, v.getOptions().get("small"), k, pos, rot, null,
						textureIds.get(k.toLowerCase()));
				part.setSmall(v.getOptions().get("small"));
				part.init();
				part.generateASParts(this, v.getChildren());
				part.getStand().setMeta(textureIds.get(k.toLowerCase()));
				parts.add(part);
			}
		});
	}

	private ArmorStandPart getHead() {
		for (ArmorStandPart asp : parts) {
			if (!asp.isHead()) {
				return checkChildren(asp);
			}
			return asp;
		}
		return null;
	}

	private ArmorStandPart checkChildren(ArmorStandPart asp) {
		for (ArmorStandPart child : asp.getChildren()) {
			if (child.isHead())
				return child;
		}
		return null;
	}

	private void lookAtPlayer(Player e, ArmorStandPart asp) {
		Location eyeLocation = location.add(0, asp.getLocalPosition().getY(), 0);
		Location loc = e.getEyeLocation();
		
		float yaw = (float) Math.toDegrees(Math.atan2(loc.getZ() - eyeLocation.getZ(), loc.getX() - eyeLocation.getX()))
				- 90;
		yaw = (float) (yaw + Math.ceil(-yaw / 360) * 360);
		
		float deltaXZ = (float) Math
				.sqrt(Math.pow(eyeLocation.getX() - loc.getX(), 2) + Math.pow(eyeLocation.getZ() - loc.getZ(), 2));
		float pitch = (float) Math.toDegrees(Math.atan2(deltaXZ, loc.getY() - eyeLocation.getY())) - 90;
		
		pitch = (float) (pitch + Math.ceil(-pitch / 360) * 360);
		asp.rotate(yaw, pitch);
	}

}