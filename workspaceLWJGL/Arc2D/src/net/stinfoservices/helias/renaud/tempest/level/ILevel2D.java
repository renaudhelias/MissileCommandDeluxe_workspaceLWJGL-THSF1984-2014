package net.stinfoservices.helias.renaud.tempest.level;

import net.stinfoservices.helias.renaud.tempest.level.terrain.IArea2D;

public interface ILevel2D extends ILevel {
	IArea2D getArea();
	void setArea(IArea2D terrain);
}
