package net.stinfoservices.helias.renaud.tempest.tools;

import java.util.ArrayList;
import java.util.List;

import net.stinfoservices.helias.renaud.tempest.tools.Point3D;

/**
 * http://fr.wikipedia.org/wiki/Matrice_de_rotation
 * "Matrice de rotation à partir d'un axe et d'un angle"
 * 
 * @author freemac
 *
 */
public class Rotate3D {
	private Point3D.Double u;
	private double c;
	private double s;

	List<Point3D.Double> matrix;
	
	/**
	 * 
	 * @param axe vecteur unitaire
	 * @param angle radian
	 */
	public Rotate3D(Point3D.Double axe, double angle) {
		this.u = axe;
		this.c = Math.cos(angle);
		this.s = Math.sin(angle);
		fillMatrix3D();
	}

	private void fillMatrix3D() {
		matrix = new ArrayList<Point3D.Double>();
		Point3D.Double ligne1 = new Point3D.Double();
		Point3D.Double ligne2 = new Point3D.Double();
		Point3D.Double ligne3 = new Point3D.Double();
		ligne1.x =u.x*u.x+(1-u.x*u.x)*c;
		ligne1.y =u.x*u.y*(1-c)-u.z*s;
		ligne1.z =u.x*u.z*(1-c)+u.y*s;
		ligne2.x=u.x*u.y*(1-c)+u.z*s;
		ligne2.y=u.y*u.y+(1-u.y*u.y)*c;
		ligne2.z=u.y*u.z*(1-c)-u.x*s;
		ligne3.x=u.x*u.z*(1-c)-u.y*s;
		ligne3.y=u.y*u.z*(1-c)+u.x*s;
		ligne3.z=u.z*u.z+(1-u.z*u.z)*c;
		
		matrix.add(ligne1);
		matrix.add(ligne2);
		matrix.add(ligne3);
	}
	
	public Point3D.Double projeter(Point3D.Double vecteur) {
		Point3D.Double proj = new Point3D.Double();
		proj.x=matrix.get(0).x*vecteur.x+matrix.get(0).y*vecteur.y+matrix.get(0).z*vecteur.z;
		proj.y=matrix.get(1).x*vecteur.x+matrix.get(1).y*vecteur.y+matrix.get(1).z*vecteur.z;
		proj.z=matrix.get(2).x*vecteur.x+matrix.get(2).y*vecteur.y+matrix.get(2).z*vecteur.z;
		return proj;
	}
	
	
}
