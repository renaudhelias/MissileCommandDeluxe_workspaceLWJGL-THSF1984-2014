package net.stinfoservices.helias.renaud.tempest.agent;

public interface IMarcheOuCreve {
	void step();
	boolean isDeath();
	void kill(IMarcheOuCreve killer);
}
