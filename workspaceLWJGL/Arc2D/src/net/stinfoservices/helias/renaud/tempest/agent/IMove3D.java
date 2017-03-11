package net.stinfoservices.helias.renaud.tempest.agent;

import net.stinfoservices.helias.renaud.tempest.IPosition3D;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D;

public interface IMove3D extends IMove, IPosition3D {
	Point3D.Double getPreviousPosition();
	Point3D.Double getNextPosition();
}
