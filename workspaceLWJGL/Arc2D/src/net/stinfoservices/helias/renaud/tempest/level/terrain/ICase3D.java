package net.stinfoservices.helias.renaud.tempest.level.terrain;


import net.stinfoservices.helias.renaud.tempest.IPosition3D;

public interface ICase3D extends ICase, IPosition3D{

	int getX();
	int getY();
	int getZ();
}
