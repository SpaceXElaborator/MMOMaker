package com.terturl.MMO.CustomArmorStandMobs.Utils;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import com.terturl.MMO.Util.Math.CosSineTable;
import com.terturl.MMO.Util.Math.RelativeLocation;

import lombok.Getter;

public class ArmorStandContainer {

	@Getter
	private ArmorStand stand;

	@Getter
	private RelativeLocation relative;

	public ArmorStandContainer(Location loc, RelativeLocation rLoc, boolean small) {
		relative = rLoc;
		ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(getFromRelative(loc), EntityType.ARMOR_STAND);
		as.setHeadPose(new EulerAngle(0.0D, 0.0D, 0.0D));
		as.setRightArmPose(new EulerAngle(0.0D, 0.0D, 0.0D));
		as.setRightLegPose(new EulerAngle(0.0D, 0.0D, 0.0D));
		as.setLeftArmPose(new EulerAngle(0.0D, 0.0D, 0.0D));
		as.setLeftLegPose(new EulerAngle(0.0D, 0.0D, 0.0D));
		as.setGravity(false);
		as.setVisible(false);
		as.setBasePlate(false);
		as.setArms(true);
		as.setSmall(small);
		this.stand = as;
	}

	public void setHelmet(ItemStack stack) {
		stand.getEquipment().setHelmet(stack);
	}

	public void setHeadRotation(EulerAngle ea) {
		stand.setHeadPose(ea);
	}

	public static Location getLocation(Location paramLocation, double paramDouble1, double paramDouble2,
			double paramDouble3) {
		Location location = paramLocation.clone();
		float f = location.getYaw();
		if (location.getYaw() < 0.0F)
			f = Math.abs(f) + 180.0F;
		double d1 = f;
		double d2 = CosSineTable.getTable().getCos((int) d1);
		double d3 = CosSineTable.getTable().getSine((int) d1);
		location = location.add(d2 * paramDouble1, paramDouble2, d3 * paramDouble1);
		location = location.add(-d3 * paramDouble3, 0.0D, d2 * paramDouble3);
		return location;
	}

	public void teleport(Location paramLocation) {
		stand.teleport(getFromRelative(paramLocation));
	}

	public static Location reverseRelative(Location paramLocation, RelativeLocation paramRelativeLocation) {
		Location location = getLocation(paramLocation, -paramRelativeLocation.getLeftRight(),
				-paramRelativeLocation.getUpDown(), -paramRelativeLocation.getForwardBackward());
		if (paramRelativeLocation.getYaw() != 0.0F)
			location.setYaw(paramLocation.getYaw() - paramRelativeLocation.getYaw());
		if (paramRelativeLocation.isBackward())
			location.setYaw(-paramLocation.getYaw());
		return location;
	}

	public void setRelativeLocation(RelativeLocation paramRelativeLocation) {
		RelativeLocation relativeLocation = getRelative();
		this.relative = paramRelativeLocation;
		teleport(reverseRelative(getStand().getLocation(), relativeLocation));
	}

	public Location getFromRelative(Location paramLocation) {
		Location location = getLocation(paramLocation, getRelative().getLeftRight(), getRelative().getUpDown(),
				getRelative().getForwardBackward()).clone();
		if (getRelative().getYaw() != 0.0F)
			location.setYaw(location.getYaw() + getRelative().getYaw());
		if (getRelative().isBackward())
			location.setYaw(-location.getYaw());
		return location;
	}

}