package net.stinfoservices.helias.renaud.tempest.agent.monstre;

import java.awt.geom.Point2D.Double;
import java.util.List;

import org.newdawn.slick.openal.Audio;

import net.stinfoservices.helias.renaud.tempest.agent.CircleSpriteTextureRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle2D;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle3D;
import net.stinfoservices.helias.renaud.tempest.agent.IDangerous;
import net.stinfoservices.helias.renaud.tempest.agent.IMarcheOuCreve;
import net.stinfoservices.helias.renaud.tempest.agent.IMove2D;
import net.stinfoservices.helias.renaud.tempest.agent.ISoundRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile2D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile3D;
import net.stinfoservices.helias.renaud.tempest.level.ILevel2D;
import net.stinfoservices.helias.renaud.tempest.level.terrain.ICase2D;
import net.stinfoservices.helias.renaud.tempest.level.terrain.ITerrain2D;
import net.stinfoservices.helias.renaud.tempest.tools.Tools;

/**
 * Ratio shootable.
 * @author STI
 *
 * TODO : algo � changer (je rencontre un autre serpent je vais ailleurs)
 * TODO : t�te de serpent
 */
public class Serpent extends CircleSpriteTextureRenderer implements IDangerous, IMonstre, ISoundRenderer, IMove2D {

	static int monsterNameGenerator = 0;

	String monsterName = "";
	ICase2D c;
	ICase2D cNext;

	static final int LATENCE = 40;

	/**
	 * Initialis� � LATENCE.
	 */
	private int progression = 0;

	boolean goingRight = true;
	private boolean death = false;

	private ITerrain2D terrain;

//	private Double orientation;

	private Audio killSound;

private boolean blocked = true;

	public Serpent(ILevel2D level) {
		monsterNameGenerator++;
		monsterName = "MonsterName" + monsterNameGenerator;
		this.terrain = level.getArea();
		c = terrain.getCase(0,
				terrain.getHeight() - 1);
		cNext = null;
		c.addMonstre(this);
		System.out.println(monsterName + " born at " + c.getX() + ","
				+ c.getY());
//		this.orientation = level.getCanon().getPosition();
	}

	@Override
	public void step() {
		if (death == false) {
			if (!blocked) {
				progression++;
			}
			if (progression == LATENCE || blocked) {
				blocked=true;
				if (cNext != null) { 
					
					ICase2D next = getNextCase(cNext);
					if (next == null ) {
						System.out.println(monsterName + " dying");
						c.removeMonstre(this);
						death = true;
						return;
					}
					
					if(!containsSerpent(next.getMonstres())) {
					
						if (next.getY() != c.getY() && next.getX() == c.getX()) {
							goingRight = !goingRight;
						}
					
						// lag ?
						c =cNext;
						c.removeMonstre(this); // monstre arriv�
						cNext=next;
						cNext.addMonstre(this); // je r�serve la case suivante
						blocked=false;
					}
				} else if (cNext == null) {
					ICase2D next = getNextCase(c);
					if (next == null ) {
						System.out.println(monsterName + " dying");
						c.removeMonstre(this);
						death = true;
						return;
					}
					if(!containsSerpent(next.getMonstres())){
						// let's start
						cNext = next;
						blocked=false;
						c.removeMonstre(this);
						// je r�serve la case suivante !
						cNext.addMonstre(this);
					}
				}
				progression = 0;
			}
		}
	}

	private boolean containsSerpent(List<IMonstre> monstres) {
		for (IMonstre monstre : monstres) {
			if (!monstre.isDeath() && monstre instanceof Serpent) {
				return true;
			}
		}
		return false;
	}
	private boolean containsBouclier(List<IMonstre> monstres) {
		for (IMonstre monstre : monstres) {
			if (!monstre.isDeath() && monstre instanceof Bouclier) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * bug : (-0.025)
	 * @return [0:1]
	 */
	public double getProgression() {
		return (double) progression / (double) LATENCE;
	}

	public ICase2D getCase() {
		return c;
	}

	public ICase2D getNextCase(ICase2D cPrevious) {
		int x = cPrevious.getX();
		int y = cPrevious.getY();
		if (goingRight) {
			if (x == terrain.getWidth() - 1) {
				if (terrain.isTerrainMirrorX()) {
					x=0;
				}
				y--;
			} else if (containsBouclier(terrain.getCase(x+1, y).getMonstres())) {
				y--;
			} else {
				x++;
			}
		} else {
			if (x == 0) {
				if (terrain.isTerrainMirrorX()) {
					x=terrain.getWidth()-1;
				}
				y--;
			} else if (containsBouclier(terrain.getCase(x-1, y).getMonstres())) { 
				y--;
			} else {
				x--;
			}
		}
		if (y < 0) {
			return null;
		}
		// System.out.println(level.getCase(x, y).getY()+"=="+y);
		return terrain.getCase(x, y);
	}

	

	@Override
	public Double getPreviousPosition() {
		return getCase().getPosition();
	}

	@Override
	public Double getNextPosition() {
		ICase2D next = cNext;
		if (next == null) {
			return null;
		} else {
			return next.getPosition();
		}
	}
	@Override
	public Double getPosition() {
		return Tools.computeMiddle(this);
	}
	
	@Override
	public boolean isDeath() {
		return death;
	}

	@Override
	public void kill(IMarcheOuCreve killer) {
		killSound.playAsSoundEffect(1.0f, 0.4f, false);
		death = true;
	}

	@Override
	public boolean touch(IProjectile2D arrow) {
		return Tools.touchArrowCircle(arrow, this);
	}

	@Override
	public double getRayon() {
		return 35;//15;
	}

	@Override
	public boolean touch(ICircle2D canon) {
		return Tools.touchCircleCircle(canon, this);
	}

	@Override
	public boolean isOriented() {
		return true;
	}

	@Override
	public Double getOrientation() {
		if (getNextPosition() == null) return null;
		return Tools.vector(getPreviousPosition(),getNextPosition());
	}

	@Override
	public void prepare(Audio kill, Audio action) {
		this.killSound = kill;
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
