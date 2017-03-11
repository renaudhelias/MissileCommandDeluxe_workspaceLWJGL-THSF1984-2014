package net.stinfoservices.helias.renaud.tempest.level.terrain;

import net.stinfoservices.helias.renaud.tempest.tools.Point3D;


public interface ITerrain3D extends ITerrain {
	
	/**
	 * Nombre de cases.
	 * @return
	 */
	Point3D.Integer getTerrainSize();
	
	ICase3D getCase(Point3D.Integer position);

}
