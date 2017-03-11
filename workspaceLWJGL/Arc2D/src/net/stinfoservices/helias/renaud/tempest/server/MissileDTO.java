package net.stinfoservices.helias.renaud.tempest.server;

import java.awt.geom.Point2D.Double;
import java.io.Serializable;

import net.stinfoservices.helias.renaud.tempest.agent.projectile.Missile;

public class MissileDTO implements Serializable {

	/**
	 * UID 
	 */
	private static final long serialVersionUID = 1L;
	
	private Double devant;
	
	public MissileDTO() {
		
	}
	
	public MissileDTO(Missile m) {
		devant = new Double(m.getDevant().x,m.getDevant().y);
	}

	public Double getDevant() {
		return devant;
	}

	public void setDevant(Double devant) {
		this.devant = devant;
	}
	

}
