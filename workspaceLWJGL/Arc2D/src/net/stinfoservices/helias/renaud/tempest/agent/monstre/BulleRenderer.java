package net.stinfoservices.helias.renaud.tempest.agent.monstre;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class BulleRenderer {

	private int nbPoints;
	private List<Point2D.Double> points;

	public BulleRenderer(int nbPoints) {
		this.nbPoints = nbPoints;
	}

	public void refresh(BulleModel bulle) {
		points = new ArrayList<Point2D.Double>();
		double step = Math.PI*2/nbPoints;
		double angle = 0;
		double rayon = bulle.getRayon();
		for (int i =0;i<nbPoints;i++) {
			Point2D.Double point = new Point2D.Double(Math.cos(angle)*rayon+bulle.getPosition().x,Math.sin(angle)*rayon+bulle.getPosition().y);
			points.add(point);
			angle+=step;
		}
		points.add(points.get(0));
	}

	public List<Point2D.Double> getPoints() {
		return points;
	}

}
