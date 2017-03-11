package net.stinfoservices.helias.renaud.tempest.agent.projectile;

import java.awt.geom.Point2D;

public class ProjectileAdapter implements IProjectile2D {

	private IProjectile2D projectile;
	private double offsetX;
	private double offsetY;

	public ProjectileAdapter(IProjectile2D projectile, double offsetX, double offsetY) {
		this.projectile = projectile;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	@Override
	public Point2D.Double getDevantOld() {
		return new Point2D.Double(projectile.getDevantOld().x+offsetX,projectile.getDevantOld().y+offsetY);
	}

	@Override
	public Point2D.Double getDevant() {
		return new Point2D.Double(projectile.getDevant().x+offsetX,projectile.getDevant().y+offsetY);
	}

	@Override
	public Point2D.Double getDerriere() {
		return new Point2D.Double(projectile.getDerriere().x+offsetX,projectile.getDerriere().y+offsetY);
	}

}
