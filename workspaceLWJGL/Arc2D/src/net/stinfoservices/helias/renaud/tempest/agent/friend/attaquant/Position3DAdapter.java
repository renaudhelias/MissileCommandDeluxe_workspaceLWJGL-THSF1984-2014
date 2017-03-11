package net.stinfoservices.helias.renaud.tempest.agent.friend.attaquant;

import net.stinfoservices.helias.renaud.tempest.IPosition3D;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D;

/**
 * a Move that doesn't move.
 * @author sti
 *
 */
public class Position3DAdapter implements IPosition3D {

	private Point3D.Double position;
	public Position3DAdapter(Point3D.Double position) {
		this.position = new Point3D.Double(position.x,position.y, position.z);
	}

	public Position3DAdapter(IPosition3D position3D) {
		Point3D.Double position = position3D.getPosition();
		this.position = new Point3D.Double(position.x,position.y, position.z);
	}

	@Override
	public Point3D.Double getPosition() {
		return position;
	}
	
	@Override
	public String toString() {
		return position.toString();
	}

}
