package com.terturl.MMO.MMOEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

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

@ToString
public class MMOMobEntity {

	@Getter
	private Integer resWidth;
	
	@Getter
	private Integer resHeight;
	
	@Getter @Setter
	private Double width = 0.0D;
	
	@Getter @Setter
	private Double height = 0.0D;
	
	@Getter @Setter
	private Double eyeHeight = 0.0D;
	
	@Getter
	private BlockBenchFile bbf;
	
	@Getter
	private Map<String, BBOBone> bones = new HashMap<>();
	
	@Getter
	private List<MobBoneFile> boneFiles = new ArrayList<>();
	
	@Getter @Setter
	private Map<String, Integer> textureMapping = new HashMap<>();
	
	public MMOMobEntity(BlockBenchFile block) {
		bbf = block;
		resWidth = block.getWidth();
		resHeight = block.getHeight();
		for(BBOOutliner outliner : block.getOutliner()) {
			BBOBone bone = createBone(null, outliner);
			if(bone == null) continue;
			bones.put(outliner.getName().toLowerCase(), bone);
		}
		bones.values().forEach(bone -> {
			bone.setRelativeOffset(new Double[] { 0.0D, 0.0D, 0.0D });
			bone.updateChildren();
		});
	}
	
	private BBOBone createBone(String parent, BBOOutliner outliner) {
		if(outliner.getName().equalsIgnoreCase("hitbox")) {
			setEyeHeight(outliner.getOrigin()[1]);
			BBOCube cube = bbf.findCubeByUUID(outliner.getChildren().get(0));
			if(cube == null) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Unable to find hitbox for " + ChatColor.GOLD + bbf.getName());
				return null;
			}
			setWidth(cube.getTo()[0] - cube.getFrom()[0]);
			setHeight(cube.getTo()[1] - cube.getFrom()[1]);
			return null;
		}
		Boolean hasCube = false;
		BBOBone bone = new BBOBone(parent, -outliner.getOrigin()[0], outliner.getOrigin()[1], outliner.getOrigin()[2], outliner.getRotation()[0], outliner.getRotation()[1], outliner.getRotation()[2]);
		if(outliner.getName().toLowerCase().startsWith("h_")) {
			outliner.setName(outliner.getName().substring(2));
			bone.getOptions().put("head", true);
		}
		MobBoneFile mbf = new MobBoneFile(this, outliner.getName().toLowerCase());
		for(UUID uuid : outliner.getChildren()) {
			if(bbf.isOutliner(uuid)) {
				BBOOutliner bChild = bbf.findOutlinerByUUID(uuid);
				BBOBone b = createBone(outliner.getName(), bChild);
				b.setRelativeOffset(new Double[] {outliner.getOrigin()[0], outliner.getOrigin()[1], outliner.getOrigin()[2]});
				bone.addChild(bChild.getName(), b);
			} else {
				BBOCube cube = bbf.findCubeByUUID(uuid);
				Cube c = createCube(cube, outliner);
				mbf.getElements().add(c);
				hasCube = true;
			}
		}
		
		if(hasCube) {
			bone.setOption("small", mbf.normalize());
		}
		boneFiles.add(mbf);
		return bone;
	}
	
	private Cube createCube(BBOCube bCube, BBOOutliner outliner) {
		Cube cube = new Cube();
		cube.setName(bCube.getName().toLowerCase());
		Double fx = bCube.getFrom()[0] - outliner.getOrigin()[0] - bCube.getInflate();
		Double fy = bCube.getFrom()[1] - outliner.getOrigin()[1] - bCube.getInflate();
		Double fz = bCube.getFrom()[2] - outliner.getOrigin()[2] - bCube.getInflate();
		cube.setFrom(fx + 8.0D, fy + 8.0D, fz + 8.0D);
		Double tx = bCube.getTo()[0] - outliner.getOrigin()[0] + bCube.getInflate();
		Double ty = bCube.getTo()[1] - outliner.getOrigin()[1] + bCube.getInflate();
		Double tz = bCube.getTo()[2] - outliner.getOrigin()[2] + bCube.getInflate();
		cube.setTo(tx + 8.0D, ty + 8.0D, tz + 8.0D);
		Rotation rot = new Rotation();
		switch(bCube.getAxis()) {
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
		}
		rot.setOrigin(bCube.getOrigin()[0] - outliner.getOrigin()[0] + 8.0D, bCube.getOrigin()[1] - outliner.getOrigin()[1] + 8.0D, bCube.getOrigin()[2] - outliner.getOrigin()[2] + 8.0D);
		cube.setRotation(rot);
		Double wRatio = 16.0D / resWidth;
		Double hRatio = 16.0D / resHeight;
		bCube.getFaces().forEach((side, face) -> {
			Face cFace = new Face();
			cFace.setUv(face.getUv()[0] * wRatio, face.getUv()[1] * hRatio, face.getUv()[2] * wRatio, face.getUv()[3] * hRatio);
			cFace.setTexture("#" + String.valueOf(face.getTexture()));
			cFace.setRotation(face.getRotation());
			cube.addFace(side, cFace);
		});
		return cube;
	}
	
}