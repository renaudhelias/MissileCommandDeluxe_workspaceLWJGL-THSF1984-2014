package net.stinfoservices.helias.renaud.tempest.agent.projectile.special;

import java.awt.geom.Point2D.Double;

import net.stinfoservices.helias.renaud.tempest.agent.CircleSpriteTextureRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle2D;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle3D;
import net.stinfoservices.helias.renaud.tempest.agent.IMarcheOuCreve;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.IMonstre;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile2D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile3D;
import net.stinfoservices.helias.renaud.tempest.tools.Tools;

public class Marqueur extends CircleSpriteTextureRenderer implements IMonstre {

	private Double position;

	public Marqueur(double x, double y) {
		position = new Double(x,y);
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
		return 20;
	}

	@Override
	public Double getPosition() {
		return position;
	}

	@Override
	public boolean isOriented() {
		return false;
	}

	@Override
	public Double getOrientation() {
		return null;
	}

	@Override
	public boolean touch(IProjectile2D arrow) {
		return Tools.touchArrowCircle(arrow, this);
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
		return Tools.touchArrow3DCircle(arrow, this);
	}

	@Override
	public boolean touch(ICircle3D canon) {
		return false;
	}

}
