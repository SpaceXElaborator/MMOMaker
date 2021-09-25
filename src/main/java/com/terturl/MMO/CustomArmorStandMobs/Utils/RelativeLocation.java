package com.terturl.MMO.CustomArmorStandMobs.Utils;

import org.bukkit.Location;

public class RelativeLocation implements Cloneable {
	private double upDown;

	private double leftRight;

	private double forwardBackward;

	public double getUpDown() {
		return this.upDown;
	}

	public double getLeftRight() {
		return this.leftRight;
	}

	public double getForwardBackward() {
		return this.forwardBackward;
	}

	private boolean backward = false;

	public boolean isBackward() {
		return this.backward;
	}

	public RelativeLocation setBackward(boolean paramBoolean) {
		this.backward = paramBoolean;
		return this;
	}

	private float yaw = 0.0F;

	public float getYaw() {
		return this.yaw;
	}

	public RelativeLocation setYaw(float paramFloat) {
		this.yaw = paramFloat;
		return this;
	}

	public RelativeLocation(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat) {
		this.upDown = paramDouble1;
		this.leftRight = paramDouble2;
		this.forwardBackward = paramDouble3;
		this.yaw = paramFloat;
	}

	public RelativeLocation(double paramDouble1, double paramDouble2, double paramDouble3) {
		this(paramDouble1, paramDouble2, paramDouble3, 0.0F);
	}

	public String toString() {
		return String.format("Up : %.2f , leftRight : %.2f , forwardBack : %.2f , Yaw : " + this.yaw, new Object[] {
				Double.valueOf(this.upDown), Double.valueOf(this.leftRight), Double.valueOf(this.forwardBackward) });
	}

	public RelativeLocation clone() {
		return (new RelativeLocation(getUpDown(), getLeftRight(), getForwardBackward())).setYaw(getYaw())
				.setBackward(isBackward());
	}

	private static Location getLocation(Location paramLocation, double paramDouble1, double paramDouble2,
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

	public Location getFromRelative(Location paramLocation) {
		Location location = getLocation(paramLocation, getLeftRight(), getUpDown(), getForwardBackward()).clone();
		if (getYaw() != 0.0F)
			location.setYaw(location.getYaw() + getYaw());
		if (isBackward())
			location.setYaw(-location.getYaw());
		return location;
	}
}