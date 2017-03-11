package net.stinfoservices.helias.renaud.tempest.level.terrain;

import java.util.ArrayList;
import java.util.List;

import net.stinfoservices.helias.renaud.tempest.agent.monstre.IMonstre;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D;

public class Case3D implements ICase3D {

	/**
	 * Ceci est mon territoire !
	 */
	List<IMonstre> monstres= new ArrayList<IMonstre>();
	private Point3D.Integer positionTerrain;
	private Point3D.Double positionZone;
	
	/**
	 * 
	 * @param positionTerrain en nombre de cases
	 * @param positionZone selon position case et taille en pixel de la zone.
	 */
	public Case3D(Point3D.Integer positionTerrain, Point3D.Double positionZone) {
		this.positionTerrain = positionTerrain;
		this.positionZone =positionZone;
	}
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
	public Point3D.Double getPosition() {
		return positionZone;
	}

	@Override
	public int getX() {
		return positionTerrain.x;
	}

	@Override
	public int getY() {
		return positionTerrain.y;
	}

	@Override
	public int getZ() {
		return positionTerrain.z;
	}

}
