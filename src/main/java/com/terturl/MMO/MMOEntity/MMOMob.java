package com.terturl.MMO.MMOEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import com.terturl.MMO.MMOEntity.ArmorStand.ArmorStandPart;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOBone;

import lombok.Getter;
import lombok.Setter;

public class MMOMob {

	@Getter
	private Map<String, BBOBone> bones = new HashMap<>();

	@Getter
	private List<ArmorStandPart> parts = new ArrayList<>();
	
	@Getter
	private Map<String, Integer> textureIds;
	
	@Getter @Setter
	private Location location;
	
	public MMOMob(MMOMobEntity entity) {
		entity.getBones().forEach((k, v) -> {
			bones.put(k, v);
		});
		textureIds = entity.getTextureMapping();
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