package net.stinfoservices.helias.renaud.tempest.tools;

import java.awt.geom.Point2D;

/**
 * Inspiré de Point2D.Double
 * @author freemac
 *
 */
public class Point3D {
	public static class Double {
		public double x;
		public double y;
		public double z;
		public Double(Double point3D) {
			this.x = point3D.x;
			this.y = point3D.y;
			this.z = point3D.z;
		}
		public Double(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		public Double() {
			x=0;y=0;z=0;
		}
		public Double(Point2D.Double point2D) {
			this.x = point2D.x;
			this.y = point2D.y;
			this.z = 0;
		}
		public Double(Integer point3D) {
			this.x = point3D.x;
			this.y = point3D.y;
			this.z = point3D.z;
		}
		@Override
		public String toString() {
			return "Point3D("+x+","+y+","+z+")";
		}
		
	}

	public static class Integer {
		public int x;
		public int y;
		public int z;
		public Integer(Integer point3D) {
			this.x = point3D.x;
			this.y = point3D.y;
			this.z = point3D.z;
		}
		public Integer(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		public Integer() {
			x=0;y=0;z=0;
		}
		@Override
		public String toString() {
			return "Point3D("+x+","+y+","+z+")";
		}
	}
}
