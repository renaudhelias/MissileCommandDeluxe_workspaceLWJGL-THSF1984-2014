package net.stinfoservices.helias.renaud.tempest.agent.projectile;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.Serializable;

import org.lwjgl.opengl.GL11;

import net.stinfoservices.helias.renaud.tempest.agent.CircleSpriteTextureRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle2D;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle3D;
import net.stinfoservices.helias.renaud.tempest.agent.IMarcheOuCreve;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.IMonstre;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.special.Marqueur;
import net.stinfoservices.helias.renaud.tempest.level.ILevel2D;
import net.stinfoservices.helias.renaud.tempest.tools.Tools;

// air/terre ou terre/air
/**
 * Le Missile est � la fois un ICircle, et � la fois un IProjectile.
 * Il a donc deux �tats, non cumulable
 * Cela dit il y a un marqueur. Qui disparait aussi
 * @author freemac
 *
 */
public class Missile extends CircleSpriteTextureRenderer implements IMissile, IProjectile2D, IMonstre, Serializable {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * What ???
	 */
	public static final double CALIBRAGE_FORCE = (800+600)/2;

//	private boolean firedFromHuman;
	
	private boolean stateExplosing;

	private transient double explosingRayon=1.0;

	private transient boolean death;

	private transient Double devantOld;

	private Double devant;

	private transient Double derriere;

	private transient double force;

	private transient ILevel2D level;

	private transient Marqueur marqueur;

	private transient int idTexExplode;

	private transient int widthExplode;

	private transient int heightExplode;

	private transient Double origine;

	public Missile(ILevel2D level, Point2D.Double origine, Point2D.Double devant,Point2D.Double derriere, double force, Marqueur marqueur, int idTexExplode, int widthExplode, int heightExplode) {
		super();
//		this.firedFromHuman=firedFromHuman;
		this.marqueur = marqueur;
		this.level = level;
		
		this.devant = devant;
		this.devantOld = new Point2D.Double(devant.x, devant.y);
		this.derriere = derriere;
		this.force=force;
		this.idTexExplode = idTexExplode;
		this.widthExplode = widthExplode;
		this.heightExplode = heightExplode;
		
		this.origine = origine;
		if (force == 0) {
			this.marqueur = null;
			// ScoreLevel : you loose, "The End"
			changeStateIntoExplosion();
		}
	}
	
	@Override
	public Double getDevant() {
		return devant;
	}

	@Override
	public Double getDevantOld() {
		return devantOld;
	}

	@Override
	public Double getDerriere() {
		return derriere;
	}

//	@Override
//	public boolean isHumanFire() {
//		return firedFromHuman;
//	}
	@Override
	public double getRayon() {
		if (stateExplosing) {
			return explosingRayon;
		} else {
			// largeur du missile
			return 40;
		}
	}
	@Override
	public Double getPosition() {
		if (stateExplosing) {
			return getDevant();
		} else {
			Double devant = getDevant();
			Double derriere = getDerriere();
			return new Double((devant.x-derriere.x)/2.0+derriere.x, (devant.y-derriere.y)/2.0+derriere.y);
		}
	}
	@Override
	public boolean isOriented() {
		if (stateExplosing) {
			return false;
		} else {
			return true;
		}
	}
	@Override
	public Double getOrientation() {
		return Tools.vector(getDevant(), getDerriere());
	}
	@Override
	public double getProgression() {
		return 0;
	}
	@Override
	public void step() {
		if (!death) {
			if (stateExplosing) {
				if( derriere==null) {
					//ScoreLevel
					if (explosingRayon>512.0) {
						death = true;
					} else {
						explosingRayon+=1.0;
					}
				} else {
					if (explosingRayon>150.0) {
						death = true;
					} else {
						explosingRayon+=1.0;
					}
				}
			} else {
				devantOld.x=devant.x;
				devantOld.y=devant.y;
				
				// suivant la force de la fl�che
				Point2D.Double vecteur = new Point2D.Double(devant.x-derriere.x, devant.y-derriere.y);
				vecteur.x=vecteur.x * force/CALIBRAGE_FORCE;
				vecteur.y=vecteur.y * force/CALIBRAGE_FORCE;
				devant.x=devant.x+vecteur.x;
				devant.y=devant.y+vecteur.y;
				derriere.x=derriere.x+vecteur.x;
				derriere.y=derriere.y+vecteur.y;
				
				//isOutOfScreen
				// suivant taille �cran donn� lors du main
				if ((devant.x<0 || devant.x>level.getArea().getZoneWidth())) {
					death = true;
				}
				if ((devant.y<0 || devant.y>level.getArea().getZoneHeight())) {
					death = true;
				}
				// marqueur touch it
				if (marqueur !=null && marqueur.touch((IProjectile2D)this)) {
					changeStateIntoExplosion();
					
				}
			}
		}
	}
	
	private void changeStateIntoExplosion() {
		stateExplosing = true;
		// change image
		prepare(idTexExplode, widthExplode, heightExplode);
//level.getArrows().remove(this); java.util.ConcurrentModificationException
level.addFriend(this);
level.addMonstre(this);
	}

	@Override
	public boolean isDeath() {
		return death;
	}
	@Override
	public void kill(IMarcheOuCreve killer) {
		// not killable this way
		if (!stateExplosing) {
			if (killer instanceof IMissile) {
				changeStateIntoExplosion();
			} else {
				death = true;
			}
		}
	}
	
	@Override
	public boolean render(int calque) {
		if (stateExplosing) {
			super.render(calque);
		} else {
			renderLine();
			super.render(calque);
			if (marqueur != null) marqueur.render(calque);
		}
		return true;
	}
	
	public void renderLine() {
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glLineWidth(5);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(getOrigine().x, getOrigine().y);
		GL11.glVertex2d(getDevant().x, getDevant().y);
		GL11.glEnd();
	}

	private Double getOrigine() {
		return origine;
	}

	@Override
	public boolean touch(IProjectile2D arrow) {
		if (stateExplosing)
		return Tools.touchArrowCircle(arrow, this);
		else return false;
	}

	@Override
	public boolean touch(ICircle2D canon) {
		if (canon instanceof IMissile && !stateExplosing) {
			return false;
		}
		return Tools.touchCircleCircle(canon, this);
	}

	@Override
	public void dispose() {
		death = true;
	}

	@Override
	public boolean touch(IProjectile3D arrow) {
		if (stateExplosing)
		return Tools.touchArrow3DCircle(arrow, this);
		else return false;
	}

	@Override
	public boolean touch(ICircle3D canon) {
		if (canon instanceof IMissile && !stateExplosing) {
			return false;
		}
		return Tools.touchCircleCircle3D(this, canon);
	}

}
