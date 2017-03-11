package net.stinfoservices.helias.renaud.tempest.agent.friend.attaquant;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 * Mod�le d'un arc 2D.
 * 
 * @author STI
 *
 */
public class Arc2DModel {
	private double parcours1;
	private double parcours2;
	private double distance;
	private Point2D.Double positionDebut1 = new Point2D.Double(0,0);
	private Point2D.Double positionDebut2 = new Point2D.Double(0,0);
	private Point2D.Double positionFin1 = new Point2D.Double(0,0);
	private Point2D.Double positionFin2 = new Point2D.Double(0,0);
	private double taille;
	private boolean creating;
	private boolean using;
	/**
	 * Pr�cision du type (tremblement)
	 * @return
	 */
	public double getDexterite() {
		return distance-(parcours1+parcours2);
	}
	/**
	 * parcours effectu� par le premier doigt
	 * @param parcours1
	 */
	public void setParcours1(double parcours1) {
		this.parcours1 = parcours1;
	}
	/**
	 * parcours effectu� par le second doigt
	 * @param parcours2
	 */
	public void setParcours2(double parcours2) {
		this.parcours2 = parcours2;
	}
	/**
	 * distance entre le premier doigt
	 * @return
	 */
	public double getForce() {
		return distance-taille;
	}
	/**
	 * distance entre le premier doigt et le second lorsqu'on relache
	 * @return
	 */
	private void setDistance(double distance) {
		this.distance = distance;
	}
	public double getDistance() {
		return distance;
	}
	/**
	 * position de d�part du premier doigt
	 * @return
	 */
	public Point2D.Double getPositionDebut1() {
		return positionDebut1;
	}
	
	/**
	 * position de d�part du premier doigt
	 * @return
	 */
	public void setPositionDebut1(Point2D.Double positionDebut1) {
		this.positionDebut1 = positionDebut1;
		creating = true;
		using = false;
	}

	/**
	 * position de d�part du second doigt. D�duit la taille au passage
	 * @return
	 */
	public void setPositionDebut2(Point2D.Double positionDebut1, Point2D.Double positionDebut2) {
		// on met � jour la position du doigt un, car on a le droit de choisir la position de d�part.
		this.positionDebut1 = positionDebut1;
		this.positionDebut2 = positionDebut2;
		// la taille de l'arc est d�ductible
		setTaille(Math.sqrt((positionDebut2.x-positionDebut1.x)*(positionDebut2.x-positionDebut1.x) + (positionDebut2.y-positionDebut1.y)*(positionDebut2.y-positionDebut1.y)));
		creating = false;
		using = true;
	}

	Point2D.Double getVecteurDirection() {
		// positionFin1 positionFin2
		Point2D.Double direction = new Point2D.Double(positionFin2.x-positionFin1.x,positionFin2.y-positionFin1.y);
		return direction;
	}
	
	
	
	/**
	 * position de d�part du premier doigt
	 * @return
	 */
	public void setPositionFin1(Point2D.Double positionFin1) {
		this.positionFin1 = positionFin1;
		setDistance(Math.sqrt((positionFin2.x-positionFin1.x)*(positionFin2.x-positionFin1.x) + (positionFin2.y-positionFin1.y)*(positionFin2.y-positionFin1.y)));
		creating = false;
		using = false;
	}

	/**
	 * position de d�part du premier doigt
	 * @return
	 */
	public void setPositionFin2(Point2D.Double positionFin2) {
		this.positionFin2 = positionFin2;
		setDistance(Math.sqrt((positionFin2.x-positionFin1.x)*(positionFin2.x-positionFin1.x) + (positionFin2.y-positionFin1.y)*(positionFin2.y-positionFin1.y)));
		creating = false;
		using = false;
	}

	/**
	 * distance entre les deux doigts lorsqu'on pose le second doigt. D�duit la taille de l'arc.
	 * @return
	 */
	public double getTaille() {
		return taille;
	}
	/**
	 * distance entre les deux doigts lorsqu'on pose le second doigt. D�duit la taille de l'arc.
	 * @return
	 */
	private void setTaille(double taille) {
		this.taille = taille;
	}
	
	/**
	 * Cr�ation de l'arc en bois
	 * @return
	 */
	public boolean isCreating() {
		return creating;
	}

	/**
	 * d�placement de l'arc en bois + tendre l'arc
	 * @return
	 */
	public boolean isUsing() {
		return using;
	}
	public Double getPositionDebut2() {
		return positionDebut2;
	}
	public Double getPositionFin2() {
		return positionFin2;
	}

}
