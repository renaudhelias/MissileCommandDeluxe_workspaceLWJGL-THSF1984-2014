package net.stinfoservices.helias.renaud.tempest.agent.friend;

import java.awt.geom.Point2D.Double;

import net.stinfoservices.helias.renaud.tempest.agent.CircleSpriteTextureRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle2D;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle3D;
import net.stinfoservices.helias.renaud.tempest.agent.IHero;
import net.stinfoservices.helias.renaud.tempest.agent.IMarcheOuCreve;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.IMonstre;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile2D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile3D;
import net.stinfoservices.helias.renaud.tempest.level.ILevel2D;

public class Maison extends CircleSpriteTextureRenderer implements IHero, IMonstre {

	private ILevel2D level;
	private int x;
	private int y;
	private boolean death;

	public Maison(ILevel2D level, int x, int y) {
		super(level.getArea().getWidth(),3,x, 3-1-y);
		this.x=x;
		this.y=y;
		this.level = level;
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
	public Double getPosition() {
		return level.getArea().getCase(x, y).getPosition();
	}
	
	@Override
	public void step() {
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
