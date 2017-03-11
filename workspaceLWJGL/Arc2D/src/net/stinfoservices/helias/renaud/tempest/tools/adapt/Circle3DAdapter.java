package net.stinfoservices.helias.renaud.tempest.tools.adapt;

import java.awt.geom.Point2D;

import net.stinfoservices.helias.renaud.tempest.agent.ICircle2D;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle3D;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D;

public class Circle3DAdapter implements ICircle3D {

	private ICircle2D cercle;
	public Circle3DAdapter(ICircle2D cercle) {
		this.cercle = cercle;
	}
	
	@Override
	public boolean isOriented() {
		return cercle.isOriented();
	}
	@Override
	public double getRayon() {
		return cercle.getRayon();
	}
	@Override
	public Point3D.Double getPosition() {
		return new Point3D.Double(cercle.getPosition().x,cercle.getPosition().y,0);
	}
	@Override
	public Point3D.Double getOrientation() {
		Point2D.Double orientation = cercle.getOrientation();
		if (orientation != null) {
			return new Point3D.Double(orientation.x,orientation.y,0);
		}
		return null;
	}
}
