package net.stinfoservices.helias.renaud.tempest.level.terrain;

import java.util.List;

import net.stinfoservices.helias.renaud.tempest.IPosition;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.IMonstre;

public interface ICase extends IPosition {


	void addMonstre(IMonstre monstre);
	void removeMonstre(IMonstre monstre);
	List<IMonstre> getMonstres();
}
