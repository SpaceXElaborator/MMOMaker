package com.terturl.MMO.Util.Math;

import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import lombok.Getter;

public class Quaternion {

	@Getter
	private Double w;

	@Getter
	private Vector vec;

	public Quaternion(Double d, Vector v) {
		w = d;
		vec = v;
	}

	public static Quaternion toQuaternion(EulerAngle e) {
		double c1 = Math.cos(e.getX() * 0.5D);
		double c2 = Math.cos(e.getY() * -0.5D);
		double c3 = Math.cos(e.getZ() * 0.5D);
		double s1 = Math.sin(e.getX() * 0.5D);
		double s2 = Math.sin(e.getY() * -0.5D);
		double s3 = Math.sin(e.getZ() * 0.5D);
		Vector vec = new Vector(s1 * c2 * c3 + c1 * s2 * s3, c1 * s2 * c3 - s1 * c2 * s3, c1 * c2 * s3 + s1 * s2 * c3);
		double w = c1 * c2 * c3 - s1 * s2 * s3;
		return new Quaternion(w, vec);
	}

	public static EulerAngle toEuler(Quaternion q) {
		double ex, ez, x = q.getVec().getX(), y = q.getVec().getY(), z = q.getVec().getZ(), w = q.getW();
		double x2 = x + x, y2 = y + y, z2 = z + z;
		double xx = x * x2, xy = x * y2, xz = x * z2;
		double yy = y * y2, yz = y * z2, zz = z * z2;
		double wx = w * x2, wy = w * y2, wz = w * z2;
		double m11 = 1.0D - yy + zz;
		double m12 = xy - wz;
		double m13 = xz + wy;
		double m22 = 1.0D - xx + zz;
		double m23 = yz - wx;
		double m32 = yz + wx;
		double m33 = 1.0D - xx + yy;
		double ey = Math.asin(clamp(m13, -1.0D, 1.0D));
		if (Math.abs(m13) < 0.99999D) {
			ex = Math.atan2(-m23, m33);
			ez = Math.atan2(-m12, m11);
		} else {
			ex = Math.atan2(m32, m22);
			ez = 0.0D;
		}
		return new EulerAngle(ex, -ey, ez);
	}

	public static Quaternion multiply(Quaternion a, Quaternion b) {
		double qax = a.getVec().getX(), qay = a.getVec().getY(), qaz = a.getVec().getZ(), qaw = a.getW();
		double qbx = b.getVec().getX(), qby = b.getVec().getY(), qbz = b.getVec().getZ(), qbw = b.getW();
		Vector vec = new Vector(qax * qbw + qaw * qbx + qay * qbz - qaz * qby,
				qay * qbw + qaw * qby + qaz * qbx - qax * qbz, qaz * qbw + qaw * qbz + qax * qby - qay * qbx);
		double w = qaw * qbw - qax * qbx - qay * qby - qaz * qbz;
		return new Quaternion(w, vec);
	}

	public static EulerAngle combine(EulerAngle origin, EulerAngle delta) {
		return toEuler(multiply(toQuaternion(origin), toQuaternion(delta)));
	}

	private static double clamp(double value, double min, double max) {
		return Math.min(Math.max(value, min), max);
	}

}