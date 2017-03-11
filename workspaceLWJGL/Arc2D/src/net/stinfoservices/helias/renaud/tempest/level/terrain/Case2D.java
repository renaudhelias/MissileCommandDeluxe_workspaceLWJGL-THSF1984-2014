package net.stinfoservices.helias.renaud.tempest.level.terrain;

import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

import net.stinfoservices.helias.renaud.tempest.agent.monstre.IMonstre;

public class Case2D implements ICase2D {

	private List<Double> points;

	public Case2D(List<Double> points, int x, int y) {
		this.points = points;
		this.x=x;
		this.y=y;
	}
	@Override
	public List<Double> getPoints() {
		return points;
	}

	@Override
	public Double getPosition() {
		Double center= new Double(0,0);
		for (Double point : points) {
			center.x = center.x + point.x;
			center.y = center.y + point.y;
		}
		center.x = center.x /points.size();
		center.y = center.y /points.size();
		
		return center;
	}

	List<IMonstre> monstres = new ArrayList<IMonstre>();
	private int x;
	private int y;
	
	@Override
	public void addMonstre(IMonstre monstre) {
		monstres.add(monstre);
	}

	@Override
	public void removeMonstre(IMonstre monstre) {
		monstres.remove(monstre);
	}

	@Override
	public List<IMonstre> getMonstres() {
		return monstres;
	}
	@Override
	public int getX() {
		return x;
	}
	@Override
	public int getY() {
		return y;
	}

}
