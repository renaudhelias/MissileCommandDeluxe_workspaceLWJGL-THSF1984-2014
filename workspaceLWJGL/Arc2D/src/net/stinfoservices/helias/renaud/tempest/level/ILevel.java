package net.stinfoservices.helias.renaud.tempest.level;

import net.stinfoservices.helias.renaud.tempest.ICamera;
import net.stinfoservices.helias.renaud.tempest.IScreen;
import net.stinfoservices.helias.renaud.tempest.agent.IController;
import net.stinfoservices.helias.renaud.tempest.agent.IRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.IMonstre;
import net.stinfoservices.helias.renaud.tempest.level.terrain.IArea;

/**
 * Une explosion est un IMonstre, dans la liste friend et monstre � la fois.
 * @author sti
 *
 */
public interface ILevel extends IController, IRenderer {
	
	
	int getMunitionsRestantes();
	int getMaisonsRestantes();
	int getMaxMunitions();
	int getMaxMaisons();
	int getScore();
	void setScore(int score);
	void consumeMunition();
	
	void generateMonstres() throws Exception;
	void generateMonstres(int phase);
	void step();

	void addMonstre(IMonstre monstre);
	void addFriend(IMonstre friend);

	IScreen getScreen();

	/**
	 * 
	 * @return null not finished, true player does won
	 */
	Boolean finished();
	
	void setArea(IArea iArea);
	
	/**
	 * Transformations préliminaire
	 * @return
	 */
	ICamera getCamera();
	void init();
}
