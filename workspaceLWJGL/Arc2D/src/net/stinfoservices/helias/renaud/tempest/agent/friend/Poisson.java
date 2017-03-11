package net.stinfoservices.helias.renaud.tempest.agent.friend;

import java.awt.geom.Point2D.Double;

import net.stinfoservices.helias.renaud.tempest.agent.CircleSpriteTextureRenderer;

public class Poisson extends CircleSpriteTextureRenderer {

	private Double position;

	public Poisson(Double position) {
		this.position = position;
	}
	
	public Poisson() {
	}

	@Override
	public double getRayon() {
		return 30;
	}

	@Override
	public Double getPosition() {
		return position;
	}
	
	public void setPosition(Double position) {
		this.position =position ;
	}

	@Override
	public boolean isOriented() {
		return false;
	}

	@Override
	public Double getOrientation() {
		return null;
	}

	

}
