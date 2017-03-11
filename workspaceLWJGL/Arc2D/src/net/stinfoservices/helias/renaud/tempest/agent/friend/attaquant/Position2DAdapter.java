package net.stinfoservices.helias.renaud.tempest.agent.friend.attaquant;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import net.stinfoservices.helias.renaud.tempest.IPosition2D;

/**
 * A Move that doen't move...
 * @author sti
 */
public class Position2DAdapter implements IPosition2D {

	private Double position;
	public Position2DAdapter(Point2D.Double position) {
		this.position = new Point2D.Double(position.x,position.y);
	}

	@Override
	public Double getPosition() {
		return position;
	}

}
