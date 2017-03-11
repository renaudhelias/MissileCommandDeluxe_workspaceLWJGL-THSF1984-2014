package net.stinfoservices.helias.renaud.tempest.agent.projectile.factory;

import java.awt.geom.Point2D;

import net.stinfoservices.helias.renaud.tempest.IPosition;
import net.stinfoservices.helias.renaud.tempest.IPosition2D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IMissile;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.Missile;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.special.Marqueur;
import net.stinfoservices.helias.renaud.tempest.level.ILevel2D;

public class MissileFactory implements IMissileFactory {
	private int idTexMissile;
	private int widthMissile;
	private int heightMissile;
	private int idTexExplode;
	private int widthExplode;
	private int heightExplode;
	private int idTexMarqueur;
	private int widthMarqueur;
	private int heightMarqueur;
	private ILevel2D level;
	

	public MissileFactory(ILevel2D level) {
		this.level = level;
	}
	
	@Override
	public void prepareMissile(int idTexMarqueur, int widthMarqueur, int heightMarqueur, int idTexMissile, int widthMissile, int heightMissile, int idTexExplode, int widthExplode, int heightExplode) {
		this.idTexMissile = idTexMissile;
		this.widthMissile = widthMissile;
		this.heightMissile = heightMissile;
		this.idTexExplode = idTexExplode;
		this.widthExplode = widthExplode;
		this.heightExplode = heightExplode;
		this.idTexMarqueur = idTexMarqueur;
		this.widthMarqueur = widthMarqueur;
		this.heightMarqueur = heightMarqueur;
		
	}

	public IMissile launcheMissile(IPosition source, IPosition destination, boolean satellite, double force20, int period0) {
		Point2D.Double derriere = ((IPosition2D)source).getPosition();
		Point2D.Double devant = ((IPosition2D)destination).getPosition();
		Point2D.Double derriereSave = new Point2D.Double(derriere.x, derriere.y);
		Point2D.Double devantSave = new Point2D.Double(devant.x, devant.y);
		double taille = Math.sqrt(Math.pow(devant.x-derriere.x,2)+Math.pow(devant.y-derriere.y,2));
		devant.x = devant.x - derriere.x;
		devant.y = devant.y - derriere.y;
		devant.x =devant.x*50.0/taille;
		devant.y =devant.y*50.0/taille;
		devant.x = devant.x + derriere.x;
		devant.y = devant.y + derriere.y;
//			level.addArrow(new ArrowModel(level.getScreen(), devant, derriere, 100));
		
		Marqueur marqueur= null;
		if (!satellite) {
			marqueur = new Marqueur(devantSave.x, devantSave.y);
			marqueur.prepare(idTexMarqueur, widthMarqueur, heightMarqueur);
		}
		Point2D.Double origine = derriereSave;
		Missile missile = new Missile(level, origine, devant, derriere, force20, marqueur, idTexExplode, widthExplode, heightExplode);
		missile.prepare(idTexMissile, widthMissile, heightMissile);
		if (satellite) {
			level.addMonstre(missile);
		} else {
			level.addFriend(missile);
		}
		return missile;
	}

}
