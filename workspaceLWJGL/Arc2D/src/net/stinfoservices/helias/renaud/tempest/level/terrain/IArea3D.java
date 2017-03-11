package net.stinfoservices.helias.renaud.tempest.level.terrain;

import net.stinfoservices.helias.renaud.tempest.tools.Point3D;

/**
 * Un terrain dans une zone. Du coup on peut en déduire la taille d'une case par exemple
 * @author freemac
 *
 */
public interface IArea3D extends ITerrain3D,IZone3D,IArea {
	Point3D.Double getCaseSize();
}
