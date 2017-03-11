package net.stinfoservices.helias.renaud.tempest.agent.monstre;

import java.util.ArrayList;
import java.util.List;

import net.stinfoservices.helias.renaud.tempest.IPosition;
import net.stinfoservices.helias.renaud.tempest.IPosition3D;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle2D;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle3D;
import net.stinfoservices.helias.renaud.tempest.agent.IDangerous;
import net.stinfoservices.helias.renaud.tempest.agent.IMarcheOuCreve;
import net.stinfoservices.helias.renaud.tempest.agent.IMove;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IMissile;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile2D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile3D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.factory.IMissileFactory;
import net.stinfoservices.helias.renaud.tempest.level.ILevel;
import net.stinfoservices.helias.renaud.tempest.level.properties.CalibrageProps;
import net.stinfoservices.helias.renaud.tempest.level.properties.Scenario;
import net.stinfoservices.helias.renaud.tempest.level.terrain.ICase;

public class Satellite implements IMonstre, IDangerous {
	public enum PHASES {PHASE_0, PHASE_1, PHASE_2, PHASE_3, PHASE_4, PHASE_5, PHASE_6, PHASE_7, PHASE_8, PHASE_9, PHASE_10, PHASE_11, PHASE_12, PHASE_13, PHASE_14, PHASE_15, PHASE_16, PHASE_17, PHASE_18, PHASE_19, PHASE_20, PHASE_21, PHASE_22};

	private ILevel level;
	
	private int phase = 0;

	private List<IMissile> missiles = new ArrayList<IMissile>();

	IMissileFactory factory;
	
	private boolean death = false;

	public Satellite(ILevel level, IMissileFactory factory) {
		this.level = level;
		this.factory = factory;
	}
	
	public void add(IMissile missile) {
		missiles.add(missile);
	}
	
	@Override
	public void step() {
		if (missiles.isEmpty()) {
			phase++;
			if (phase == Scenario.getInstance().getNbNiveauxPhases()) {
				death  = true;
			} else {
				level.generateMonstres(phase);
			}
		}
	}


	@Override
	public boolean isDeath() {
		return death;
	}

	@Override
	public void kill(IMarcheOuCreve killer) {
		death = true;
	}

	@Override
	public boolean touch(IProjectile2D arrow) {
		return false;
	}

	@Override
	public boolean touch(ICircle2D canon) {
		return false;
	}

	public boolean killMissiles() {
		for (IMissile missile : missiles) {
			if (missile.isDeath()) {
				missiles.remove(missile);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void dispose() {
		kill(null);
	}

	@Override
	public boolean touch(IProjectile3D arrow) {
		return false;
	}

	@Override
	public boolean touch(ICircle3D canon) {
		return false;
	}

	public void buildMissile(IPosition source, IPosition destination) {
		buildMissile(source, destination,0);
	}

	public void buildMissile(IPosition source, IPosition destination,
			int period) {
		add(factory.launcheMissile(source, destination, true, CalibrageProps.getInstance().getForceMissileSatellite(),period));
	}

}
