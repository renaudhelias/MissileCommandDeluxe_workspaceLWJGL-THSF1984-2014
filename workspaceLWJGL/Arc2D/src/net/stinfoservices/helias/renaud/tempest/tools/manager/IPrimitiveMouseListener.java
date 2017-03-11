package net.stinfoservices.helias.renaud.tempest.tools.manager;

public interface IPrimitiveMouseListener {

	void mousePrimitiveClick(String mouseName, int i, int value);

	/**
	 * 
	 * @param mouseName
	 * @param i 0=x 1=y
	 * @param value
	 */
	void mousePrimitiveMouve(String mouseName, int i, float value);

}
