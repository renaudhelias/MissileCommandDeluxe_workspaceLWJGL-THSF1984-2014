package net.stinfoservices.helias.renaud.tempest.agent;

import java.awt.geom.Point2D;

import net.stinfoservices.helias.renaud.tempest.IPosition2D;

public interface ICircle2D extends IPosition2D {
	double getRayon();
	boolean isOriented();
	/**
	 * Shall be nicer with a vector here :/
	 * @return another point (bad !)
	 */
	Point2D.Double getOrientation();
}
