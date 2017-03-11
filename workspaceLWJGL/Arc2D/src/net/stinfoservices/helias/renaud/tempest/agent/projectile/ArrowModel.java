package net.stinfoservices.helias.renaud.tempest.agent.projectile;

import java.awt.geom.Point2D;

import org.lwjgl.opengl.GL11;

import net.stinfoservices.helias.renaud.tempest.agent.ICircle2D;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle3D;
import net.stinfoservices.helias.renaud.tempest.agent.IMarcheOuCreve;
import net.stinfoservices.helias.renaud.tempest.agent.IRenderer2D;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.IMonstre;
import net.stinfoservices.helias.renaud.tempest.level.terrain.IZone2D;
import net.stinfoservices.helias.renaud.tempest.tools.Tools;

public class ArrowModel implements IMonstre, IProjectile2D, IRenderer2D {

	public final static double VITESSE = 1;
	/**
	 * What ?
	 */
	public static final double CALIBRAGE_FORCE = (800+600)/2;
	private Point2D.Double derriere;
	private Point2D.Double devant;
	private Point2D.Double devantOld;
	private double force;
	private IZone2D zone;
	private boolean death;
	
	public ArrowModel(IZone2D zone, Point2D.Double devant,Point2D.Double derriere, double force) {
		this.zone = zone;
		this.devant=devant;
		this.devantOld = new Point2D.Double(devant.x, devant.y);
		this.derriere=derriere;
		this.force = force;
	}
	
	public Point2D.Double getDerriere() {
		return derriere;
	}
	public void setDerriere(Point2D.Double derriere) {
		this.derriere = derriere;
	}
	public Point2D.Double getDevant() {
		return devant;
	}
	public void setDevant(Point2D.Double devant) {
		this.devant = devant;
	}

	public void step() {
		
		devantOld.x=devant.x;
		devantOld.y=devant.y;
		
		// suivant la force de la fl�che
		Point2D.Double vecteur = new Point2D.Double(devant.x-derriere.x, devant.y-derriere.y);
		vecteur.x=vecteur.x * force/CALIBRAGE_FORCE;
		vecteur.y=vecteur.y * force/CALIBRAGE_FORCE;
		devant.x=devant.x+vecteur.x;
		devant.y=devant.y+vecteur.y;
		derriere.x=derriere.x+vecteur.x;
		derriere.y=derriere.y+vecteur.y;
		
		//isOutOfScreen
		// suivant taille �cran donn� lors du main
		if (devant.x<0 || devant.y<0 || devant.x>zone.getZoneWidth() || devant.y>zone.getZoneHeight()) {
			death = true;
		}
	}

	@Override
	public boolean isDeath() {
		return death;
	}

	/**
	 * Permet de mieux calculer l'impact.
	 * @return
	 */
	public Point2D.Double getDevantOld() {
		return devantOld;
	}

	@Override
	public void kill(IMarcheOuCreve killer) {
		death = true;
	}

	@Override
	public void prepare(int idTex, int width, int height) {
		// nothing
	}

	@Override
	public boolean render(int calque) {
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glLineWidth(10);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(getDerriere().x, getDerriere().y);
		GL11.glVertex2d(getDevant().x, getDevant().y);
		GL11.glEnd();
		return true;
	}

//	@Override
//	public boolean isHumanFire() {
//		return true;
//	}

	@Override
	public boolean touch(IProjectile2D arrow) {
		return false;
	}

	@Override
	public boolean touch(ICircle2D canon) {
		return Tools.touchArrowCircle(this, canon);
	}
	@Override
	public void dispose() {
		kill(null);
	}

	@Override
	public boolean touch(IProjectile3D arrow) {
		return false;
	}

	@Override
	public boolean touch(ICircle3D canon) {
		return false;
	}

}
