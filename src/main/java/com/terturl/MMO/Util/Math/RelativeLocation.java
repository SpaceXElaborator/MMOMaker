package com.terturl.MMO.Util.Math;

import org.bukkit.Location;

import lombok.Getter;
import lombok.Setter;

/**
 * Helper Location used to get a location in relation to the player no matter
 * where they are facing
 * 
 * @author Sean Rahman
 * @since 0.25.0
 *
 */
public class RelativeLocation implements Cloneable {

	@Getter
	private double upDown;

	@Getter
	private double leftRight;

	@Getter
	private double forwardBackward;

	@Getter
	@Setter
	private boolean backward = false;

	@Getter
	@Setter
	private float yaw = 0.0F;

	/**
	 * Create a new RelativeLocation
	 * 
	 * @param x Y cord in relation to a location
	 * @param y X cord in relation to a location
	 * @param z Z cord in relation to a location
	 * @param d Yaw in relation to player
	 */
	public RelativeLocation(double x, double y, double z, float d) {
		this.upDown = x;
		this.leftRight = y;
		this.forwardBackward = z;
		this.yaw = d;
	}

	/**
	 * Create a new RelativeLocation
	 * 
	 * @param x Y cord in relation to a location
	 * @param y X cord in relation to a location
	 * @param z Z cord in relation to a location
	 */
	public RelativeLocation(double x, double y, double z) {
		this(x, y, z, 0.0F);
	}

	private static Location getLocation(Location loc, double x, double y, double z) {
		Location location = loc.clone();
		float f = location.getYaw();
		if (location.getYaw() < 0.0F)
			f = Math.abs(f) + 180.0F;
		double d1 = f;
		double d2 = CosSineTable.getTable().getCos((int) d1);
		double d3 = CosSineTable.getTable().getSine((int) d1);
		location = location.add(d2 * x, y, d3 * x);
		location = location.add(-d3 * z, 0.0D, d2 * z);
		return location;
	}

	/**
	 * Get the location from the relative location
	 * 
	 * @param loc Location to get relative from
	 * @return
	 */
	public Location getFromRelative(Location loc) {
		Location location = getLocation(loc, getLeftRight(), getUpDown(), getForwardBackward()).clone();
		if (getYaw() != 0.0F)
			location.setYaw(location.getYaw() + getYaw());
		if (isBackward())
			location.setYaw(-location.getYaw());
		return location;
	}

	public RelativeLocation clone() {
		RelativeLocation rl = new RelativeLocation(getUpDown(), getLeftRight(), getForwardBackward(), getYaw());
		rl.setBackward(isBackward());

		return rl;
	}
}