package com.terturl.MMO.MMOEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.terturl.MMO.MMOEntity.Animation.Animation;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOBone;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOCube;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOOutliner;
import com.terturl.MMO.MMOEntity.ResourcePack.MobBoneFile;
import com.terturl.MMO.MMOEntity.ResourcePack.Elements.Cube;
import com.terturl.MMO.MMOEntity.ResourcePack.Elements.Face;
import com.terturl.MMO.MMOEntity.ResourcePack.Elements.Rotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.md_5.bungee.api.ChatColor;

/**
 * A base class that represents all locations and positions of BBOBones and
 * animations that will be used for the creation of a MMOMob when spawned into
 * the world
 * 
 * @author Sean Rahman
 * @since 0.58.0
 *
 */
@ToString
public class MMOMobEntity {

	@Getter
	private int resWidth;

	@Getter
	private int resHeight;

	@Getter
	@Setter
	private double width = 0.0D;

	@Getter
	@Setter
	private double height = 0.0D;

	@Getter
	@Setter
	private double eyeHeight = 0.0D;

	@Getter
	@Setter
	private String name;

	@Getter
	private BlockBenchFile bbf;

	@Getter
	private Map<String, BBOBone> bones = new HashMap<>();

	@Getter
	private List<MobBoneFile> boneFiles = new ArrayList<>();

	@Getter
	private List<Animation> animations = new ArrayList<>();

	@Getter
	@Setter
	private Map<String, Integer> textureMapping = new HashMap<>();

	/**
	 * Creates a new named MMOMobEntity with the given BlockBenchFile for options
	 * and objects
	 * 
	 * @param n     Name of the MMOMobEntity
	 * @param block BlockBenchFile to be used for templating
	 */
	public MMOMobEntity(String n, BlockBenchFile block) {
		bbf = block;
		name = n;
		resWidth = block.getWidth();
		resHeight = block.getHeight();
		for (BBOOutliner outliner : block.getOutliner()) {
			BBOBone bone = createBone(null, outliner);
			if (bone == null)
				continue;
			bones.put(outliner.getName().toLowerCase(), bone);
		}
		bones.values().forEach(bone -> {
			bone.setRelativeOffset(new double[] { 0.0D, 0.0D, 0.0D });
			bone.updateChildren();
		});
		for (Animation a : block.getAnimations()) {
			animations.add(a);
		}
	}

	private BBOBone createBone(String parent, BBOOutliner outliner) {
		
		// Create where the entity can be hit from, this is currently not used and developed
		// This will also be used to make sure that the mob can fit inbetween gaps of certain sizes or not
		if (outliner.getName().equalsIgnoreCase("hitbox")) {
			setEyeHeight(outliner.getOrigin()[1]);
			BBOCube cube = bbf.findCubeByUUID(outliner.getChildren().get(0));
			if (cube == null) {
				Bukkit.getServer().getConsoleSender()
						.sendMessage(ChatColor.RED + "Unable to find hitbox for " + ChatColor.GOLD + bbf.getName());
				return null;
			}
			setWidth(cube.getTo()[0] - cube.getFrom()[0]);
			setHeight(cube.getTo()[1] - cube.getFrom()[1]);
			return null;
		}
		
		Boolean hasCube = false;
		BBOBone bone = new BBOBone(parent, -outliner.getOrigin()[0], outliner.getOrigin()[1], outliner.getOrigin()[2],
				outliner.getRotation()[0], outliner.getRotation()[1], outliner.getRotation()[2]);
		
		// Check for the head piece which will be used to rotate and look at Entities they are targetting
		if (outliner.getName().toLowerCase().startsWith("h_")) {
			outliner.setName(outliner.getName().substring(2));
			bone.getOptions().put("head", true);
		}
		
		MobBoneFile mbf = new MobBoneFile(this, outliner.getName().toLowerCase());
		for (UUID uuid : outliner.getChildren()) {
			if (bbf.isOutliner(uuid)) {
				BBOOutliner bChild = bbf.findOutlinerByUUID(uuid);
				BBOBone b = createBone(outliner.getName(), bChild);
				b.setRelativeOffset(
						new double[] { outliner.getOrigin()[0], outliner.getOrigin()[1], outliner.getOrigin()[2] });
				bone.addChild(bChild.getName(), b);
			} else {
				BBOCube cube = bbf.findCubeByUUID(uuid);
				try {
					Cube c = createCube(cube, outliner);
					mbf.getElements().add(c);
				} catch (IllegalArgumentException e) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + name + " - A Cube as an invalid rotation");
					continue;
				}
				hasCube = true;
			}
		}

		// Make sure to set if the BBOBone will be small or not to set the distance of global offsets later in MMOMob/MMOMobArmorStand
		if (hasCube) {
			bone.setOption("small", mbf.normalize());
		}
		boneFiles.add(mbf);
		return bone;
	}

	private Cube createCube(BBOCube bCube, BBOOutliner outliner) throws IllegalArgumentException {
		Cube cube = new Cube();
		cube.setName(bCube.getName().toLowerCase());
		double fx = bCube.getFrom()[0] - outliner.getOrigin()[0] - bCube.getInflate();
		double fy = bCube.getFrom()[1] - outliner.getOrigin()[1] - bCube.getInflate();
		double fz = bCube.getFrom()[2] - outliner.getOrigin()[2] - bCube.getInflate();
		cube.setFrom(fx + 8.0D, fy + 8.0D, fz + 8.0D);
		double tx = bCube.getTo()[0] - outliner.getOrigin()[0] + bCube.getInflate();
		double ty = bCube.getTo()[1] - outliner.getOrigin()[1] + bCube.getInflate();
		double tz = bCube.getTo()[2] - outliner.getOrigin()[2] + bCube.getInflate();
		cube.setTo(tx + 8.0D, ty + 8.0D, tz + 8.0D);
		Rotation rot = new Rotation();
		switch (bCube.getAxis()) {
			case "x":
				rot.setAxis("x");
				rot.setAngle(bCube.getRotation()[0]);
				break;
			case "y":
				rot.setAxis("y");
				rot.setAngle(bCube.getRotation()[1]);
				break;
			case "z":
				rot.setAxis("z");
				rot.setAngle(bCube.getRotation()[2]);
				break;
			default:
				throw new IllegalArgumentException();
		}
		rot.setOrigin(bCube.getOrigin()[0] - outliner.getOrigin()[0] + 8.0D,
				bCube.getOrigin()[1] - outliner.getOrigin()[1] + 8.0D,
				bCube.getOrigin()[2] - outliner.getOrigin()[2] + 8.0D);
		cube.setRotation(rot);
		double wRatio = 16.0D / resWidth;
		double hRatio = 16.0D / resHeight;
		bCube.getFaces().forEach((side, face) -> {
			Face cFace = new Face();
			cFace.setUv(face.getUv()[0] * wRatio, face.getUv()[1] * hRatio, face.getUv()[2] * wRatio,
					face.getUv()[3] * hRatio);
			cFace.setTexture("#" + String.valueOf(face.getTexture()));
			cFace.setRotation(face.getRotation());
			cube.addFace(side, cFace);
		});
		return cube;
	}

}