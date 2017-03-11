package net.stinfoservices.helias.renaud.tempest.agent.monstre;

import net.stinfoservices.helias.renaud.tempest.IPosition;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle2D;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle3D;
import net.stinfoservices.helias.renaud.tempest.agent.IMarcheOuCreve;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile2D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile3D;

public interface IMonstre extends IMarcheOuCreve, IPosition {

	boolean touch(IProjectile2D arrow);

	boolean touch(ICircle2D canon);
	
	boolean touch(IProjectile3D arrow);

	boolean touch(ICircle3D canon);

	/**
	 * Remove from game
	 */
	void dispose();
}
