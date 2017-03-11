package net.stinfoservices.helias.renaud.tempest.agent;

import net.stinfoservices.helias.renaud.tempest.IPosition3D;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D;

public interface ICircle3D extends IPosition3D {
	double getRayon();
	boolean isOriented();
	/**
	 * Shall be nicer with a vector here :/
	 * @return another point (bad !)
	 */
	Point3D.Double getOrientation();

}
