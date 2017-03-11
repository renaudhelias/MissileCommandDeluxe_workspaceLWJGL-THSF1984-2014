package net.stinfoservices.helias.renaud.tempest.agent.projectile;

import net.stinfoservices.helias.renaud.tempest.tools.Point3D;

public interface IProjectile3D {
	Point3D.Double getDevantOld();
	Point3D.Double getDevant();
	Point3D.Double getDerriere();
}
