package net.stinfoservices.helias.renaud.tempest.agent.friend;

import net.stinfoservices.helias.renaud.tempest.agent.Circle3DRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle2D;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle3D;
import net.stinfoservices.helias.renaud.tempest.agent.IMarcheOuCreve;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.IMonstre;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile2D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile3D;
import net.stinfoservices.helias.renaud.tempest.level.ILevel3D;
import net.stinfoservices.helias.renaud.tempest.level.terrain.ITerrain3D;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D;
import net.stinfoservices.helias.renaud.tempest.tools.Tools;

public class Route3D extends Circle3DRenderer implements IMonstre {

	private ILevel3D level;
	private Point3D.Integer position;
	private boolean death;

	public Route3D(ILevel3D level, Point3D.Integer position) {
		super(true);
		this.level = level;
		this.position = position;
		prepare("route");
	}
	@Override
	public double getRayon() {
		return Tools.deduceRayon(level);
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
	public Point3D.Double getPosition() {
		return ((ITerrain3D)level.getArea()).getCase(position).getPosition();
	}
	@Override
	public void step() {
	}
	@Override
	public boolean isDeath() {
		return death;
	}
	@Override
	public void kill(IMarcheOuCreve killer) {
		death = true;
	}
	@Override
	public boolean touch(IProjectile2D arrow) {
		return false;
	}
	@Override
	public boolean touch(ICircle2D canon) {
		return false;
	}
	@Override
	public boolean touch(IProjectile3D arrow) {
		return false;
	}
	@Override
	public boolean touch(ICircle3D canon) {
		return false;
	}
	@Override
	public void dispose() {
		kill(null);
	}

}
