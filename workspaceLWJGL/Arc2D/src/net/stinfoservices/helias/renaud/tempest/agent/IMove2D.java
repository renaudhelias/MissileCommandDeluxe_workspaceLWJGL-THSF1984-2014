package net.stinfoservices.helias.renaud.tempest.agent;

import java.awt.geom.Point2D;

import net.stinfoservices.helias.renaud.tempest.IPosition2D;

public interface IMove2D extends IMove,IPosition2D {
	Point2D.Double getPreviousPosition();
	Point2D.Double getNextPosition();
}
