package net.stinfoservices.helias.renaud.tempest.agent;

import net.stinfoservices.helias.renaud.tempest.IPosition;

public interface IMove extends IPosition {
	/**
	 * 
	 * @return [0:1[ en partant de zéro, croissant
	 */
	double getProgression();
}
