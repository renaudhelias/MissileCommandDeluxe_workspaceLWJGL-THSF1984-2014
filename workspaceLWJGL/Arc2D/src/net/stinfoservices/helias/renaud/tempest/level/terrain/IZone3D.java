package net.stinfoservices.helias.renaud.tempest.level.terrain;

import net.stinfoservices.helias.renaud.tempest.tools.Point3D;

/**
 * Le Terrain (compos� de cases) est pos� sur zone, qui a une certaine taille réel en pixels.
 * @author sti
 *
 */
public interface IZone3D extends IZone {
	Point3D.Integer getZoneSize();
}
