package net.stinfoservices.helias.renaud.tempest.agent.monstre;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import org.lwjgl.opengl.GL11;

import net.stinfoservices.helias.renaud.tempest.IScreen;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle2D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.ArrowModel;

public class BulleModel implements ICircle2D {

	private Point2D.Double position;
	/**
	 * Non normalis�, donc direction+vitesse
	 */
	private Point2D.Double vecteur;
	private double rayon;
	private IScreen screen;
	
	private static BulleRenderer bulleRenderer = new BulleRenderer(10);

	
	public BulleModel(IScreen screen, Point2D.Double position,double rayon, Point2D.Double vecteur) {
		this.screen = screen;
		this.position = position;
		this.rayon = rayon;
		this.vecteur = vecteur;
	}
	
	/**
	 * Avancer la bulle d'un pas.
	 */
	public void step() {
		this.position.x = this.position.x + vecteur.x;
		this.position.y = this.position.y + vecteur.y;
		
		// miroir, version 0 : TODO l'afficher plusieurs fois si sur les bords
		if (this.position.x < 0) {
			this.position.x=this.position.x+screen.getScreenWidth();
		}
		if (this.position.x >screen.getScreenWidth()) {
			this.position.x=this.position.x-screen.getScreenWidth();
		}
		if (this.position.y < 0) {
			this.position.y=this.position.y+screen.getScreenHeight();
		}
		if (this.position.y >screen.getScreenHeight()) {
			this.position.y=this.position.y-screen.getScreenHeight();
		}
		
	}
	/**
	 * D�tection de contact entre deux bulles
	 * @param bulle
	 * @return
	 */
	public boolean touch(BulleModel bulle) {
		double distanceBuBulle = Math.sqrt(Math.pow(bulle.getPosition().x-getPosition().x,2) + Math.pow(bulle.getPosition().y-getPosition().y,2));
		return distanceBuBulle <= getRayon()+bulle.getRayon();
	}
	public Point2D.Double getPosition() {
		return position;
	}
	public void setPosition(Point2D.Double position) {
		this.position = position;
	}
	public Point2D.Double getVecteur() {
		return vecteur;
	}
	public void setVecteur(Point2D.Double vecteur) {
		this.vecteur = vecteur;
	}
	public double getRayon() {
		return rayon;
	}
	public void setRayon(double rayon) {
		this.rayon = rayon;
	}
	public boolean touch(ArrowModel arrow) {
		Point2D.Double p1 = arrow.getDevantOld();
		Point2D.Double p2 = arrow.getDevant();
		
		// on va la jouer warrior : le point le plus proche d'un segment par rapport � un point (centre du cercle) est la perpendiculaire au segment passant par ce point.
		
		Point2D.Double vecteur12 = new Point2D.Double();
		vecteur12.x = p2.x-p1.x;
		vecteur12.y = p2.y-p1.y;
		double tailleVecteur =Math.sqrt(vecteur12.x*vecteur12.x+vecteur12.y*vecteur12.y); 
		// normalize
		vecteur12.x = vecteur12.x/tailleVecteur;
		vecteur12.y = vecteur12.y/tailleVecteur;
		double angle = Math.acos(vecteur12.x);
		if (vecteur12.y<0) {
			angle=-angle;
		}
		// rotate
		angle+=Math.PI/2;
		Point2D.Double vecteurPerpendiculaire = new Point2D.Double();
		vecteurPerpendiculaire.x=Math.cos(angle);
		vecteurPerpendiculaire.y=Math.sin(angle);
		// intersection
		
		// p1.x+vecteur12.x*A = pos.x+vecteurPerpendiculaire.x*B
		// p1.y+vecteur12.y*A = pos.y+vecteurPerpendiculaire.y*B => A= (pos.y+vecteurPerpendiculaire.y*B -p1.y)/vecteur12.y
		
		// p1.x+vecteur12.x*(pos.y+vecteurPerpendiculaire.y*B -p1.y)/vecteur12.y = pos.x+vecteurPerpendiculaire.x*B
		// p1.x+vecteur12.x*pos.y/vecteur12.y+vecteur12.x*vecteurPerpendiculaire.y*B/vecteur12.y-vecteur12.x*p1.y/vecteur12.y = pos.x+vecteurPerpendiculaire.x*B
		// p1.x+vecteur12.x*pos.y/vecteur12.y-vecteur12.x*p1.y/vecteur12.y -pos.x = vecteurPerpendiculaire.x*B-vecteur12.x*vecteurPerpendiculaire.y*B/vecteur12.y
		// p1.x+vecteur12.x*pos.y/vecteur12.y-vecteur12.x*p1.y/vecteur12.y -pos.x = B*(vecteurPerpendiculaire.x-vecteur12.x*vecteurPerpendiculaire.y/vecteur12.y)
		double B=(p1.x+vecteur12.x*getPosition().y/vecteur12.y-vecteur12.x*p1.y/vecteur12.y -getPosition().x )/(vecteurPerpendiculaire.x-vecteur12.x*vecteurPerpendiculaire.y/vecteur12.y);
		
		// point le plus proche
		double x=getPosition().x+vecteurPerpendiculaire.x*B;
		double y=getPosition().y+vecteurPerpendiculaire.y*B;
		// dans le segment ?
		boolean inSegment = true;
		if (p1.x<p2.x){
			if (!(p1.x<x && x<p2.x)) {
				inSegment=false;
			}
		} else {
			if (!(p2.x<x && x<p1.x)) {
				inSegment=false;
			}
		}
		if (p1.y<p2.y){
			if (!(p1.y<y && y<p2.y)) {
				inSegment=false;
			}
		} else {
			if (!(p2.y<y && y<p1.y)) {
				inSegment=false;
			}
		}
		
		if (inSegment) {
			double distance = Math.sqrt(Math.pow((getPosition().x-x),2)+Math.pow((getPosition().y-y),2));
			if (distance <= getRayon()) {
				return true;
			}
		} else {
			double distance1 = Math.sqrt(Math.pow((getPosition().x-p1.x),2)+Math.pow((getPosition().y-p1.y),2));
			double distance2 = Math.sqrt(Math.pow((getPosition().x-p2.x),2)+Math.pow((getPosition().y-p2.y),2));
			if (distance1 <= getRayon() || distance2 <= getRayon()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isOriented() {
		return false;
	}

	@Override
	public Double getOrientation() {
		return null;
	}

	public void render() {
		
		bulleRenderer.refresh(this);
		
		
		GL11.glBegin(GL11.GL_LINES);

		Point2D.Double pointPrecedent = null;
		for (Point2D.Double point : bulleRenderer.getPoints()) {
			if (pointPrecedent != null) {
				GL11.glVertex2d(pointPrecedent.x, pointPrecedent.y);
				GL11.glVertex2d(point.x, point.y);
			}
			pointPrecedent = point;
		}

		GL11.glEnd();
	}

}
