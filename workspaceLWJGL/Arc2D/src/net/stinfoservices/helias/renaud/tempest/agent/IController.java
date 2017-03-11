package net.stinfoservices.helias.renaud.tempest.agent;

import java.awt.geom.Point2D;

public interface IController {
	/**
	 * @param eventKey
	 * @param eventKeyState
	 * @return true si je suis concern�
	 */
	boolean key(int eventKey, boolean eventKeyState);
	void mouse(int eventButton, boolean eventButtonState, Point2D.Double position);
	
	
//	/**
//	 * Source du tireur. : non ça c'est propre à Batterie !
//	 * @return
//	 */
//	Point2D.Double mouseSource();
}
