package net.stinfoservices.helias.renaud.tempest.level.terrain;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import net.stinfoservices.helias.renaud.tempest.IScreen;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.BulleModel;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.ArrowModel;
import net.stinfoservices.helias.renaud.tempest.tools.Tools;

public class TerrainModel {
	private List<BulleModel> bulles = new ArrayList<BulleModel>();
	private ArrowModel arrow = null;
	private int nbFusion=0;
	private IScreen screen;
	
	public TerrainModel(IScreen screen) {
		this.screen = screen;
	}
	public int getNbFusion() {
		return nbFusion;
	}

	public void setNbFusion(int nbFusion) {
		this.nbFusion = nbFusion;
	}

	public void stepBulles() {
		avancerBulles();
		while (fusionImpactBulles()) {}
	}

	public ArrowModel getArrow() {
		return arrow;
	}

	public void setArrow(ArrowModel arrow) {
		System.out.println("arrow added");
		this.arrow = arrow;
	}

	public List<BulleModel> getBulles() {
		return bulles;
	}

	private boolean fusionImpactBulles() {
		for (BulleModel bulle1 : bulles) {
			for (BulleModel bulle2 : bulles) {
				if (bulle1 != bulle2) {
					if (bulle1.touch(bulle2)) {
						fusion(bulle1,bulle2);
						return true;
					}
				}
			}
		}
		return false;
	}

	private void fusion(BulleModel bulle1, BulleModel bulle2) {
		nbFusion++;
		bulles.remove(bulle1);
		bulles.remove(bulle2);
		bulle1.setPosition(new Point2D.Double((bulle1.getPosition().x+bulle2.getPosition().x)/2,(bulle1.getPosition().y+bulle2.getPosition().y)/2));
		// version 0 : TODO manque de précision : on doit additionner les volumes et non les rayons ici !
		bulle1.setRayon(bulle1.getRayon()+bulle2.getRayon());
		bulle1.setVecteur(new Point2D.Double((bulle1.getVecteur().x+bulle2.getVecteur().x)/2,(bulle1.getVecteur().y+bulle2.getVecteur().y)/2));
		bulles.add(bulle1);
	}

	private void avancerBulles() {
		for (BulleModel bulle : bulles) {
			bulle.step();
		}
	}

	public void stepArrow() {
		if (arrow!=null) {
			arrow.step();
			while (impactArrowBulles()) {}
		}
	}

	private boolean impactArrowBulles() {
		if (arrow == null) {
			return false;
		}
		for (BulleModel bulle : bulles) {
			if (bulle.touch(arrow)) {
				System.out.println("Paf bulle de rayon "+bulle.getRayon());
				bulles.remove(bulle);
				return true;
			}
		}
		if (arrow.isDeath()) {
			System.out.println("out of screen");
			arrow = null;
			return true;
		}
		return false;
	}
	
	public void generate(int nbBulles) {
		for (int i = 0; i<nbBulles; i++) {
			BulleModel bulle = new BulleModel(screen, Tools.randomPosition(screen.getScreenWidth(), screen.getScreenHeight()), Tools.randomValue(5,10), Tools.randomVector(0.1,0.5));
			bulles.add(bulle);
		}
	}

	

}
