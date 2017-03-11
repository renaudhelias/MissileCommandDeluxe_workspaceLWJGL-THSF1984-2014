package net.stinfoservices.helias.renaud.tempest;

import org.lwjgl.opengl.GL11;

import net.stinfoservices.helias.renaud.tempest.level.terrain.Puits;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D;
import net.stinfoservices.helias.renaud.tempest.tools.Tools;

public class Camera implements ICamera {

	private Point3D.Double position;
	private Point3D.Double orientation;
	private double taille;

	public Camera(double taille, Point3D.Double position, Point3D.Double orientation) {
		this.taille=taille;
		this.position= position;
		this.orientation=orientation;
	}
	@Override
	public double getRayon() {
		return taille;
	}

	@Override
	public boolean isOriented() {
		return true;
	}

	@Override
	public Point3D.Double getOrientation() {
		return orientation;
	}

	@Override
	public Point3D.Double getPosition() {
		return Tools.computeMiddle(this);
	}

	@Override
	public Point3D.Double getPreviousPosition() {
		return position;
	}

	@Override
	public Point3D.Double getNextPosition() {
		return null;
	}

	@Override
	public double getProgression() {
		return 0;
	}
	@Override
	public boolean render(int calque) {
		TempestMain.lumiere();
		TempestMain.directionLumiere(new Point3D.Double(0,getRayon()*10,getRayon()*10),new Point3D.Double(0.25,-0.5,-1),45);
		// zoom
		GL11.glScaled(getRayon(),getRayon(), getRayon());
		
		// je met mon canon aux coordonnées zero (donc au centre de l'écran pour le WIDTH, et en bas de l'écran pour le HEIGHT, voir def ecran)
		
		
		// centrer mon terrain en 0,0 pour la rotation
	
				
		if (isOriented()) {
			Point3D.Double o = getOrientation();
			if (o != null) {
				// pour le MissileLauncher c'est en Y (sinon angle constant... banane)
				Point3D.Double o2 = new Point3D.Double(0,0,-1);
				double angle = Tools.angleVectorVector(o, o2);
				if (!Double.isNaN(angle)) {
					double rX=o.x*o2.y-o.y*o2.x;
					double rY=o.y*o2.z-o.z*o2.y;
					double rZ=o.z*o2.x-o.x*o2.z;
					if (!(rX==0 && rY==0 && rZ==0)) {
						GL11.glRotated((360.0*angle)/(Math.PI*2), rY, rZ, rX);
					}
				}
//				else {
//					double direction = Tools.directionVectorVector(o, o2);
//					if (direction < 0) {
////						GL11.glRotated(180.0,0,1, 0);
//					}GL11.glRotated(180.0,0,1, 0);
//				}
				
			}
		}
		
		Point3D.Double position = new Point3D.Double(getPosition());
//		GL11.glTranslated(-position.x, -position.y-Puits.ZONE_HEIGHT/2, -position.z);
		GL11.glTranslated(-position.x, -position.y, -position.z);
		

		return true;
	}

}
