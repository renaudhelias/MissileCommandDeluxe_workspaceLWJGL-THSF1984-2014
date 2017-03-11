package net.stinfoservices.helias.renaud.tempest.level;

import net.stinfoservices.helias.renaud.tempest.level.terrain.IArea3D;

public interface ILevel3D extends ILevel {
	IArea3D getArea();
	void setArea(IArea3D terrain);
}
