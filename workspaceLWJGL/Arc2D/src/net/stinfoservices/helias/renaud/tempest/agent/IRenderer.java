package net.stinfoservices.helias.renaud.tempest.agent;

public interface IRenderer {
	/**
	 * 
	 * @param calque de 0 à +oo
	 * @return false rien à rendre
	 */
	boolean render(int calque);
}
