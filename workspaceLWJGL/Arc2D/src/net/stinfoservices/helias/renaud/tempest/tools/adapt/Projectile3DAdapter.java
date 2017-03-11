package net.stinfoservices.helias.renaud.tempest.tools.adapt;

import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile2D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile3D;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D;

/**
 * 93.5 radio espanol
 * @author freemac
 *
 */
public class Projectile3DAdapter implements IProjectile3D {

	private IProjectile2D projectile;

	public Projectile3DAdapter(IProjectile2D projectile) {
		this.projectile = projectile;
	}
	@Override
	public Point3D.Double getDevantOld() {
		return new Point3D.Double(projectile.getDevantOld());
	}

	@Override
	public Point3D.Double getDevant() {
		return new Point3D.Double(projectile.getDevant());
	}

	@Override
	public Point3D.Double getDerriere() {
		return new Point3D.Double(projectile.getDerriere());
	}

}
