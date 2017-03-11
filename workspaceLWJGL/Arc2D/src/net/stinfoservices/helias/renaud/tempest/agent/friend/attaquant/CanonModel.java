package net.stinfoservices.helias.renaud.tempest.agent.friend.attaquant;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.openal.Audio;

import net.stinfoservices.helias.renaud.tempest.TempestMain;
import net.stinfoservices.helias.renaud.tempest.agent.CircleSpriteTextureRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle2D;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle3D;
import net.stinfoservices.helias.renaud.tempest.agent.IController;
import net.stinfoservices.helias.renaud.tempest.agent.IHero;
import net.stinfoservices.helias.renaud.tempest.agent.IMarcheOuCreve;
import net.stinfoservices.helias.renaud.tempest.agent.IMove2D;
import net.stinfoservices.helias.renaud.tempest.agent.ISoundRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.IMonstre;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.ArrowModel;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile2D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile3D;
import net.stinfoservices.helias.renaud.tempest.level.ILevel;
import net.stinfoservices.helias.renaud.tempest.level.ILevel2D;
import net.stinfoservices.helias.renaud.tempest.level.terrain.ITerrain2D;
import net.stinfoservices.helias.renaud.tempest.tools.Tools;
import net.stinfoservices.helias.renaud.tempest.tools.factory.TextureLoader;

/**
 * Commande par touche, le tir est direct les fl�ches (gauche et droite) forment
 * une s�quence. Le canon est lent � tourner
 * 
 * @author STI
 * 
 */
public class CanonModel extends CircleSpriteTextureRenderer implements IHero, ISoundRenderer, IController, IMonstre,IMove2D {

	static enum DIRECTION {
		DROITE, GAUCHE
	};

	/**
	 * Nombre de pas
	 */
	private int nbStep = 0;

	/**
	 * position actuel
	 */
	private int currentStep = 0;

	/**
	 * S�quence de d�placement du canon.
	 */
	private List<DIRECTION> sequence = new ArrayList<DIRECTION>();

	/**
	 * lors du passage � 0, le premier �l�ment de la sequence est consom�.
	 * Initialis� � LATENCE.
	 */
	private int progression;

	private boolean isMiroir;

	private ILevel2D level;

	private boolean death;

	private final static int LATENCE=5;

//	private Double orientation;

	private Audio killSound;

	private Audio fireSound;

	
	public CanonModel(ILevel2D level) {
		this.level = level;
		this.progression = 0;
//		level.setCanon(this);
		ITerrain2D terrain = level.getArea();
		this.nbStep=terrain.getWidth();
		this.currentStep=terrain.getWidth()/2;
		this.isMiroir=terrain.isTerrainMirrorX();
//		this.orientation = level.getCanon().getPosition();
		BufferedImage image = TextureLoader.loadImage(TempestMain.class.getResource("Couteau.png"));//The path is inside the jar file
		TextureLoader tex = new TextureLoader(image);
		// memory growing at each death ?
		prepare(tex.getIdTex(),tex.getWidth(),tex.getHeight());
	}
	
	public void left() {
		sequence.add(DIRECTION.GAUCHE);
	}

	public void right() {
		sequence.add(DIRECTION.DROITE);
	}

	public void step() {
		// dans la pile on a le d�placement courrant
		if (sequence.size() > 0) {
			progression++;
			if (progression == LATENCE) {
				// je suis arriv� au bout, donc je consomme la tete de la pile
				DIRECTION hop = sequence.remove(0);
				progression = 0;
				if (hop == DIRECTION.DROITE) {
					currentStep++;
					if (currentStep >= nbStep) {
						if (isMiroir) {
							currentStep = 0;
						} else {
							currentStep = nbStep - 1;
						}
					}
				} else if (hop == DIRECTION.GAUCHE) {
					currentStep--;
					if (currentStep <0) {
						if (isMiroir) {
							currentStep = nbStep - 1;
						} else {
							currentStep = 0;
						}
					}
				}
			}
			
			
		}
	}
	
	public int getCurrent() {
		return currentStep;
	}
	
	public int getNext() {
		// Je suis en mouvement, donc je d�termine l'endroit suivant
		if (sequence.size()>0) {
			if (sequence.get(0) == DIRECTION.DROITE) {
				if (currentStep+1 < nbStep) {
					return currentStep+1;
				} else if (isMiroir){
					return 0;
				} else {
					return currentStep; 
				}
			} else if(sequence.get(0) == DIRECTION.GAUCHE) {
				if (currentStep-1 >= 0) {
					return currentStep-1;
				} else if (isMiroir){
					return nbStep-1;
				} else {
					return currentStep; 
				}
			}
		}
		//ne bouge pas.
		return getCurrent();
	}
	
	/**
	 * 
	 * @return [0:1[
	 */
	public double getProgression() {
		return (double)progression/(double)LATENCE;
	}

	public void fire() {
		Double derriere = getPosition();
		Double devant = Tools.computeMiddle(level.getArea().getCase(getCurrent(),level.getArea().getHeight()-1).getPosition(),level.getArea().getCase(getNext(),level.getArea().getHeight()-1).getPosition(), getProgression());
		double taille = Math.sqrt(Math.pow(devant.x-derriere.x,2)+Math.pow(devant.y-derriere.y,2));
		devant.x = devant.x - derriere.x;
		devant.y = devant.y - derriere.y;
		devant.x =devant.x*50.0/taille;
		devant.y =devant.y*50.0/taille;
		devant.x = devant.x + derriere.x;
		devant.y = devant.y + derriere.y;
		level.addFriend(new ArrowModel(level.getArea(), devant, derriere, 100));
		fireSound.playAsSoundEffect(1.0f, 0.1f, false);
	}

	@Override
	public Double getPreviousPosition() {
		return level.getArea().getCanon(getCurrent());
	}

	@Override
	public Double getNextPosition() {
		return level.getArea().getCanon(getNext());
	}
	
	@Override
	public Double getPosition() {
		return Tools.computeMiddle(this);
	}

	@Override
	public double getRayon() {
		return 75;
	}

	@Override
	public boolean isDeath() {
		return death;
	}

	@Override
	public void kill(IMarcheOuCreve killer) {
		killSound.playAsSoundEffect(1.0f, 0.2f, false);
		death = true;
	}

	@Override
	public boolean isOriented() {
		// bad for canon : it's inverse : target is progress case center.
		return true;
	}

	@Override
	public Double getOrientation() {
		return new Double(0,1);
	}

	@Override
	public void prepare(Audio kill, Audio action) {
		this.killSound = kill;
		this.fireSound = action;
	}

	@Override
	public boolean key(int eventKey, boolean eventKeyState) {
		if (eventKey == Keyboard.KEY_LEFT) {
			if (eventKeyState) {
				//LEFT pressed
				left();
			} else {
				//LEFT realised
			}
		}
		if (eventKey == Keyboard.KEY_RIGHT) {
			if (eventKeyState) {
				right();
			}
		}
		if (eventKey == Keyboard.KEY_SPACE) {
			if (eventKeyState) {
				fire();
			}
		}
		return false;
	}
	

	@Override
	public void mouse(int eventButton, boolean eventButtonState, Point2D.Double position) {
		// nothing
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
