package net.stinfoservices.helias.renaud.tempest.agent.monstre;

import java.awt.geom.Point2D.Double;

import org.newdawn.slick.openal.Audio;

import net.stinfoservices.helias.renaud.tempest.agent.CircleSpriteTextureRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle2D;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle3D;
import net.stinfoservices.helias.renaud.tempest.agent.IMarcheOuCreve;
import net.stinfoservices.helias.renaud.tempest.agent.ISoundRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile2D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile3D;
import net.stinfoservices.helias.renaud.tempest.level.ILevel2D;
import net.stinfoservices.helias.renaud.tempest.level.terrain.ITerrain2D;
import net.stinfoservices.helias.renaud.tempest.tools.Tools;

/**
 * https://linuxfr.org/users/devnewton/journaux/nanim-1-6
 * 
 * Il faut une diff pour recalibrer (genre zone contact moins grande que le gif)
 * Non il faut surtout savoir recalibrer un gif dans gimp
 * @author freemac
 *
 */
public class Bouclier extends CircleSpriteTextureRenderer implements IMonstre, ISoundRenderer {

	private int LATENCE=180;
	private double progression=LATENCE;
	private Double position;
	private boolean death;
	private Audio killSound;
	private ILevel2D level;
	public Bouclier(ILevel2D level) {
		super(20);
		progression= Math.floor(Math.random()*(LATENCE-1));
		position = new Double(Math.random()*level.getArea().getZoneWidth(),Math.random()*(level.getArea().getZoneHeight()-100)+100);
		Tools.findCaseNearOf(position,level.getArea()).addMonstre(this);
		//System.out.println("installing bouclier with territory "+Tools.findCaseNearOf(position,level.getTerrain()).getX()+","+Tools.findCaseNearOf(position,level.getTerrain()).getY());
		this.level = level;
	}
	
	@Override
	public void step() {
		// not moving
		progression++;
		if (progression== LATENCE) progression = 0;
	}

	/**
	 * BUG : (-0.025)
	 * 
	 */
	@Override
	public double getProgression() {
		return progression/LATENCE;
	}

	@Override
	public Double getPosition() {
		return position;
	}

	@Override
	public boolean isDeath() {
		return death;
	}

	@Override
	public void kill(IMarcheOuCreve killer) {
		killSound.playAsSoundEffect(1.0f, 0.5f, false);
		death = true;
	}

	@Override
	public boolean touch(IProjectile2D arrow) {
		return Tools.touchArrowCircle(arrow, this);
	}

	@Override
	public double getRayon() {
		return 80;
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
		ITerrain2D terrain = level.getArea();
		return Tools.vector(getPosition(), terrain.getCase(terrain.getWidth()/2, 0).getPosition());
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
		return false;
	}

	@Override
	public boolean touch(ICircle3D canon) {
		return false;
	}

}
