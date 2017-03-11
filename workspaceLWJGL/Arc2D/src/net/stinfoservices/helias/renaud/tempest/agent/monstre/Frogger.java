package net.stinfoservices.helias.renaud.tempest.agent.monstre;

import java.awt.geom.Point2D.Double;

import net.stinfoservices.helias.renaud.tempest.agent.CircleSpriteTextureRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle2D;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle3D;
import net.stinfoservices.helias.renaud.tempest.agent.IDangerous;
import net.stinfoservices.helias.renaud.tempest.agent.IMarcheOuCreve;
import net.stinfoservices.helias.renaud.tempest.agent.IMove2D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile2D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile3D;
import net.stinfoservices.helias.renaud.tempest.level.ILevel2D;
import net.stinfoservices.helias.renaud.tempest.level.terrain.ICase2D;
import net.stinfoservices.helias.renaud.tempest.level.terrain.ITerrain2D;
import net.stinfoservices.helias.renaud.tempest.tools.Tools;

/**
 * TODO : meilleur pattern de d�placement
 * 
 * 
 * Parfois elle disparait, mauvais axe de rotation ???? on sort de la grille ??? 
 * 
 * @author freemac
 *
 */
public class Frogger extends CircleSpriteTextureRenderer implements IDangerous, IMonstre, IMove2D {

	private static final int LATENCE = 80;
	private int progression = 0;
	private ITerrain2D terrain;
	private ICase2D c;
	private ICase2D cNext;
	private boolean death;
	
	public Frogger(ILevel2D level) {
		super(4);
		this.terrain = level.getArea();
		c = terrain.getCase((int)Math.floor(terrain.getWidth()*Math.random()),
				(int)Math.floor(terrain.getHeight()*Math.random()));
		cNext = c;
		progression = (int) (Math.random()*(LATENCE-1));
		c.addMonstre(this);
	}
	
	@Override
	public double getRayon() {
		return 25;
	}


	@Override
	public boolean isOriented() {
		return true;
	}

	@Override
	public Double getOrientation() {
		return Tools.vector(getNextPosition(), getPreviousPosition());
	}

	@Override
	public double getProgression() {
		return (double) progression / (double) (LATENCE);
	}

	@Override
	public Double getPreviousPosition() {
		return c.getPosition();
	}
	
	@Override
	public Double getNextPosition() {
		if (cNext == null) {
			return null;
		} else {
			return cNext.getPosition();
		}

	}
	@Override
	public Double getPosition() {
		return Tools.computeMiddle(this);
	}

	@Override
	public void step() {
		if (death == false) {

			progression ++;
			
			if (progression == LATENCE) {
				c.removeMonstre(this);
				int x = c.getX();
				int y = c.getY();
				x+=Math.round(Math.random()*4-1);
				y+=Math.round(Math.random()*4-1);
				if (x<0) x+=terrain.getWidth();
				if (y<0) y=-y;
				if (x>=terrain.getWidth()) x-=terrain.getWidth();
				if (y>=terrain.getHeight()) y=terrain.getHeight()-2;
				c = cNext;
				cNext = terrain.getCase(x,y);
				progression = 0;
				cNext.addMonstre(this);
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
		return Tools.touchArrowCircle(arrow, this);
	}

	@Override
	public boolean touch(ICircle2D canon) {
		return Tools.touchCircleCircle(canon, this);
	}
	@Override
	public void dispose() {
		kill(null);
	}

	@Override
	public boolean touch(IProjectile3D arrow) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touch(ICircle3D canon) {
		// TODO Auto-generated method stub
		return false;
	}

}
