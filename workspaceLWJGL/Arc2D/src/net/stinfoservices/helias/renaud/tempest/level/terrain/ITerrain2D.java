package net.stinfoservices.helias.renaud.tempest.level.terrain;

import java.awt.geom.Point2D;


public interface ITerrain2D extends ITerrain, IZone2D {
	/**
	 * Endroit o� poser le canon.
	 * 
	 * FIXME 
	 * 
	 * @return
	 */
	Point2D.Double getCanon(int x);

	ICase2D getCase(int x, int y);

	/**
	 * Nombre de cases.
	 * @return
	 */
	int getWidth();

	/**
	 * Nombre de cases.
	 * @return
	 */
	int getHeight();

	boolean isTerrainMirrorX();
	
	boolean isTerrainMirrorY();
	
}
