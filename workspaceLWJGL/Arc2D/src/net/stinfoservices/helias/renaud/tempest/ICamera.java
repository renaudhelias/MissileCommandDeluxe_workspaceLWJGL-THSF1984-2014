package net.stinfoservices.helias.renaud.tempest;

import net.stinfoservices.helias.renaud.tempest.agent.ICircle3D;
import net.stinfoservices.helias.renaud.tempest.agent.IMove3D;
import net.stinfoservices.helias.renaud.tempest.agent.IRenderer;

/**
 * Une camera a une position, une orientation et bouge.
 * @author freemac
 *
 */
public interface ICamera extends ICircle3D, IMove3D, IRenderer {


}
