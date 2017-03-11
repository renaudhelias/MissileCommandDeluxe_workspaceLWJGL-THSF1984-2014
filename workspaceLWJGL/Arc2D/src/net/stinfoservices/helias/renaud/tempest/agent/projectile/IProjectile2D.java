package net.stinfoservices.helias.renaud.tempest.agent.projectile;

import java.awt.geom.Point2D;

public interface IProjectile2D {

	Point2D.Double getDevantOld();
	Point2D.Double getDevant();
	Point2D.Double getDerriere();

}
