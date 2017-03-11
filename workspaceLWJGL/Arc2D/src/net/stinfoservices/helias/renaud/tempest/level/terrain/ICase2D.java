package net.stinfoservices.helias.renaud.tempest.level.terrain;

import java.awt.geom.Point2D;
import java.util.List;

import net.stinfoservices.helias.renaud.tempest.IPosition2D;

public interface ICase2D extends ICase, IPosition2D{

	
	/**
	 * List of points, for drawing.
	 * @return
	 */
	List<Point2D.Double> getPoints();
	int getX();
	int getY();

}
