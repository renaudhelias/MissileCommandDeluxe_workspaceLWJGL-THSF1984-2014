package net.stinfoservices.helias.renaud.tempest.level.terrain;

/**
 * Le Terrain (compos� de cases) est pos� sur zone, qui a une certaine taille, et un offset
 * @author sti
 *
 */
public interface IZone2D extends IZone {
	int getZoneWidth();
	int getZoneHeight();
}
