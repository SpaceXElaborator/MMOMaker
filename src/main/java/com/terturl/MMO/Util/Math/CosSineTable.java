package com.terturl.MMO.Util.Math;

/**
 * Helper class to help with rotation and location services for
 * RelativeLocations
 * 
 * @author Sean Rahman
 * @since
 *
 */
public class CosSineTable {
	double[] cos = new double[361];

	double[] sin = new double[361];

	private static CosSineTable table = new CosSineTable(System.currentTimeMillis());

	private CosSineTable(long paramLong) {
		for (double b = 0; b <= 360; b++) {
			this.cos[(int) b] = Math.cos(Math.toRadians(b));
			this.sin[(int) b] = Math.sin(Math.toRadians(b));
		}
	}

	public double getSine(int paramInt) {
		int i = paramInt % 360;
		return this.sin[i];
	}

	public double getCos(int paramInt) {
		int i = paramInt % 360;
		return this.cos[i];
	}

	public static CosSineTable getTable() {
		return table;
	}
}