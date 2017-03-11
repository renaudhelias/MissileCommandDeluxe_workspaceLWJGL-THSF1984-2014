package net.stinfoservices.helias.renaud.tempest.agent.friend.attaquant;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import org.newdawn.slick.openal.Audio;

import net.stinfoservices.helias.renaud.tempest.agent.CircleSpriteTextureRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle2D;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle3D;
import net.stinfoservices.helias.renaud.tempest.agent.IController;
import net.stinfoservices.helias.renaud.tempest.agent.IMarcheOuCreve;
import net.stinfoservices.helias.renaud.tempest.agent.ISoundRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.IMonstre;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile2D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile3D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.factory.IMissileFactory;
import net.stinfoservices.helias.renaud.tempest.level.ILevel;
import net.stinfoservices.helias.renaud.tempest.level.ILevel2D;


// defense terre/air
public class Batterie extends CircleSpriteTextureRenderer implements IController, IMonstre, ISoundRenderer {

	private ILevel2D level;
	private int x;
	private int y;
	private int eventKey;
	private boolean active;
	private boolean death;
	private Audio fireSound;
	private Audio killSound;
	private IMissileFactory factory;
	
	public Batterie(ILevel2D level, IMissileFactory factory, int x, int y, int eventKey) {
		this.level = level;
		this.factory = factory;
		this.x = x;
		this.y = y;
		this.eventKey = eventKey;
	}
	
	@Override
	public double getRayon() {
		return 30;
	}

	@Override
	public boolean isOriented() {
		return false;
	}

	@Override
	public Double getOrientation() {
		return null;
	}

	@Override
	public double getProgression() {
		return 0;
	}
	
	@Override
	public Double getPosition() {
		return level.getArea().getCase(x, y).getPosition();
	}

	@Override
	public boolean key(int eventKey, boolean eventKeyState) {
		if (eventKey == this.eventKey) {
			active = eventKeyState;
		}
		return active;
	}
	
	public Double mouseSource() {
		if (active) {
			return this.getPosition();
		}
		return null;
	}

	@Override
	public void mouse(int eventButton, boolean eventButtonState, Point2D.Double position) {
		if (active) {
			if (eventButton==0 && eventButtonState) {
				// click gauche enfonc�
				fireSound.playAsSoundEffect(1.0f, 0.1f, false);
				factory.launcheMissile(this, new Position2DAdapter(position), false,20,0);
			}
		}
	}

	
	
	

	@Override
	public void step() {
		// TODO Auto-generated method stub
		
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
	
	@Override
	public void prepare(Audio kill, Audio action) {
		this.killSound = kill;
		this.fireSound = action;
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


}
