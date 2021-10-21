package com.terturl.MMO.MMOEntity.ArmorStand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import com.terturl.MMO.MMOEntity.MMOMob;
import com.terturl.MMO.MMOEntity.BlockBenchObjects.BBOBone;
import com.terturl.MMO.Util.Math.Quaternion;

import lombok.Getter;
import lombok.Setter;

public class ArmorStandPart {

	@Getter @Setter
	private boolean small = false;
	
	@Getter
	private MMOMobArmorStand stand;
	
	@Getter
	private String partId;
	
	@Getter
	private MMOMob mob;
	
	@Getter @Setter
	private Vector globalPosition;
	
	@Getter @Setter
	private EulerAngle globalRotation;
	
	@Getter @Setter
	private Vector localPosition;
	
	@Getter @Setter
	private EulerAngle localRotation;
	
	@Getter @Setter
	private Vector defaultPosition;
	
	@Getter @Setter
	private EulerAngle defaultRotation;
	
	@Getter @Setter
	private Location worldPosition;
	
	@Getter @Setter
	private EulerAngle worldRotation;
	
	@Getter @Setter
	private Double currentYaw = 0.0D;
	
	@Getter @Setter
	private boolean head;
	
	@Getter @Setter
	private ArmorStandPart parent;
	
	@Getter
	private List<ArmorStandPart> children = new ArrayList<ArmorStandPart>();
	
	@Getter
	private Integer itemModel;
	
	public ArmorStandPart(MMOMob me, Boolean s, String pid, Vector position, EulerAngle rot, ArmorStandPart p, Integer customI) {
		mob = me;
		parent = p;
		partId = pid;
		defaultPosition = position.clone();
		globalPosition = position.clone();
		localPosition = new Vector();
		defaultRotation = Quaternion.toEuler(Quaternion.toQuaternion(new EulerAngle(rot.getX(), rot.getY(), rot.getZ())));
		globalRotation = new EulerAngle(rot.getX(), rot.getY(), rot.getZ());
		localRotation = EulerAngle.ZERO;
		small = s;
		if(me.getBone(pid).getOptions().containsKey("head")) {
			head = me.getBone(pid).getOptions().get("head");
		} else {
			head = false;
		}
		itemModel = customI;
	}
	
	public void generateASParts(MMOMob me, Map<String, BBOBone> bones) {
		bones.forEach((k, v) -> {
			Vector pos = new Vector(v.getLocalOffset()[0], v.getLocalOffset()[1], v.getLocalOffset()[2]);
			EulerAngle rot = new EulerAngle(v.getLocalRotation()[0], v.getLocalRotation()[1], v.getLocalRotation()[2]);
			ArmorStandPart as = new ArmorStandPart(me, v.getOptions().get("small"), k.toLowerCase(), pos, rot, this, me.getTextureIds().get(k.toLowerCase()));
			as.init();
			children.add(as);
			as.generateASParts(me, v.getChildren());
		});
	}
	
	public void init() {
		Location loc = mob.getLocation().clone();
		stand = new MMOMobArmorStand(loc.add(globalPosition), this);
		stand.setRotation(globalRotation);
		stand.setMeta(itemModel);
	}
	
	public void showModel(Player p) {
		stand.spawn(p);
		for(ArmorStandPart asp : children) {
			asp.showModel(p);
		}
	}
	
}