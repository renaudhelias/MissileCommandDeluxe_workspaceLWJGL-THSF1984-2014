package net.stinfoservices.helias.renaud.tempest.agent.projectile.factory;

import net.stinfoservices.helias.renaud.tempest.IPosition;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IMissile;

public interface IMissileFactory {
	public void prepareMissile(int idTexMarqueur, int widthMarqueur, int heightMarqueur, int idTexMissile, int widthMissile, int heightMissile, int idTexExplode, int widthExplode, int heightExplode);

	public IMissile launcheMissile(IPosition source, IPosition destination, boolean satellite, double force10, int period);
}
