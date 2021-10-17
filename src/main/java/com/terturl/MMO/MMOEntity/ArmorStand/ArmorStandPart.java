package com.terturl.MMO.MMOEntity.ArmorStand;

import org.bukkit.Location;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import lombok.Getter;
import lombok.Setter;

public class ArmorStandPart {

	@Getter @Setter
	private boolean small = false;
	
	@Getter
	private MMOMobArmorStand stand;
	
	@Getter
	private String partId;
	
//	@Getter
//	private MMOMob mob;
	
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
	
	public ArmorStandPart(MMOMob me, String pid, Vector position, EulerAngle rot) {
		mob = me;
		partId = pid;
		defaultPosition = position.clone();
		globalPosition = position.clone();
		localPosition = new Vector();
		defaultRotation = Quaternion.toEuler(Quaternion.toQuaternion(new EulerAngle(rot.getX(), rot.getY(), rot.getZ())));
		globalRotation = new EulerAngle(rot.getX(), rot.getY(), rot.getZ());
		localRotation = EulerAngle.ZERO;
		if(me.getBone(pid).getOptions().containsKey("head")) {
			head = me.getBone(pid).getOptions().get("head");
		} else {
			head = false;
		}
	}
	
	public void init() {
		Location loc = mob.getLocation();
		stand = new MMOMobArmorStand(loc.add(globalPosition), this);
		stand.setRotation(globalRotation);
	}
	
	public void showModel(Player p) {
		stand.spawn(p);
	}
	
}