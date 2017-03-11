package net.stinfoservices.helias.renaud.tempest.level.terrain;

import java.awt.geom.Point2D;

/**
 * Un terrain dans une zone. Du coup on peut en déduire la taille d'une case par exemple
 * @author freemac
 *
 */
public interface IArea2D extends ITerrain2D, IZone2D, IArea {
	Point2D.Double getCaseSize();
}
