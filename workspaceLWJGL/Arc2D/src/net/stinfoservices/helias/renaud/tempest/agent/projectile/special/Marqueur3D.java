package net.stinfoservices.helias.renaud.tempest.agent.projectile.special;


import net.stinfoservices.helias.renaud.tempest.agent.Circle3DRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle2D;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle3D;
import net.stinfoservices.helias.renaud.tempest.agent.IMarcheOuCreve;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.IMonstre;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile2D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile3D;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D;
import net.stinfoservices.helias.renaud.tempest.tools.Tools;

public class Marqueur3D extends Circle3DRenderer implements IMonstre {

	public static final double RAYON = 20;
	private Point3D.Double position;

	public Marqueur3D(double x, double y, double z) {
		super(false);
		position = new Point3D.Double(x,y,z);
		prepare("curseurBleu");
	}

	@Override
	public void step() {
		// nothing
	}

	@Override
	public boolean isDeath() {
		return false;
	}

	@Override
	public void kill(IMarcheOuCreve killer) {
	}

	@Override
	public double getRayon() {
		return RAYON;
	}

	@Override
	public Point3D.Double getPosition() {
		return position;
	}

	@Override
	public boolean isOriented() {
		return false;
	}

	@Override
	public Point3D.Double getOrientation() {
		return null;
	}

	@Override
	public boolean touch(IProjectile2D arrow) {
		return Tools.touchArrowCircle3D(arrow, this);
	}

	@Override
	public boolean touch(ICircle2D canon) {
		return false;
	}
	@Override
	public void dispose() {
		kill(null);
	}

	

	@Override
	public boolean touch(IProjectile3D arrow) {
		return Tools.touchArrow3DCircle3D(arrow, this);
	}

	@Override
	public boolean touch(ICircle3D canon) {
		return false;
	}

}
