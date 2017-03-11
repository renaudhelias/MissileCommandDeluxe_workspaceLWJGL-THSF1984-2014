package net.stinfoservices.helias.renaud.tempest.agent.friend.attaquant;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 * Modèle d'un arc 2D.
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
	 * Précision du type (tremblement)
	 * @return
	 */
	public double getDexterite() {
		return distance-(parcours1+parcours2);
	}
	/**
	 * parcours effectué par le premier doigt
	 * @param parcours1
	 */
	public void setParcours1(double parcours1) {
		this.parcours1 = parcours1;
	}
	/**
	 * parcours effectué par le second doigt
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
	 * position de départ du premier doigt
	 * @return
	 */
	public Point2D.Double getPositionDebut1() {
		return positionDebut1;
	}
	
	/**
	 * position de départ du premier doigt
	 * @return
	 */
	public void setPositionDebut1(Point2D.Double positionDebut1) {
		this.positionDebut1 = positionDebut1;
		creating = true;
		using = false;
	}

	/**
	 * position de départ du second doigt. Déduit la taille au passage
	 * @return
	 */
	public void setPositionDebut2(Point2D.Double positionDebut1, Point2D.Double positionDebut2) {
		// on met à jour la position du doigt un, car on a le droit de choisir la position de départ.
		this.positionDebut1 = positionDebut1;
		this.positionDebut2 = positionDebut2;
		// la taille de l'arc est déductible
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
	 * position de départ du premier doigt
	 * @return
	 */
	public void setPositionFin1(Point2D.Double positionFin1) {
		this.positionFin1 = positionFin1;
		setDistance(Math.sqrt((positionFin2.x-positionFin1.x)*(positionFin2.x-positionFin1.x) + (positionFin2.y-positionFin1.y)*(positionFin2.y-positionFin1.y)));
		creating = false;
		using = false;
	}

	/**
	 * position de départ du premier doigt
	 * @return
	 */
	public void setPositionFin2(Point2D.Double positionFin2) {
		this.positionFin2 = positionFin2;
		setDistance(Math.sqrt((positionFin2.x-positionFin1.x)*(positionFin2.x-positionFin1.x) + (positionFin2.y-positionFin1.y)*(positionFin2.y-positionFin1.y)));
		creating = false;
		using = false;
	}

	/**
	 * distance entre les deux doigts lorsqu'on pose le second doigt. Déduit la taille de l'arc.
	 * @return
	 */
	public double getTaille() {
		return taille;
	}
	/**
	 * distance entre les deux doigts lorsqu'on pose le second doigt. Déduit la taille de l'arc.
	 * @return
	 */
	private void setTaille(double taille) {
		this.taille = taille;
	}
	
	/**
	 * Création de l'arc en bois
	 * @return
	 */
	public boolean isCreating() {
		return creating;
	}

	/**
	 * déplacement de l'arc en bois + tendre l'arc
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
