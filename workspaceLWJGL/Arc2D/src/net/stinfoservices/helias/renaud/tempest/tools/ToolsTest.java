package net.stinfoservices.helias.renaud.tempest.tools;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URISyntaxException;

import net.stinfoservices.helias.renaud.tempest.agent.ICircle3D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile3D;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D;
import net.stinfoservices.helias.renaud.tempest.tools.factory.Image3DFactory;

import org.junit.Test;


public class ToolsTest {

	@Test
	public void testVector() {
		Point2D.Double vector0 = Tools.vector(new Point2D.Double(5.0,5.0), new Point2D.Double(5.0,5.0));
		assertEquals(Double.NaN, vector0.x,0);
		assertEquals(Double.NaN, vector0.y, 0);
		try {
			assertEquals(Tools.angleDegreesZ(new Point2D.Double(0.0,0.0)),0.0 ,0);
		} catch (NaNException e) {
			fail("Exception NaN raised");
		}
		try {
			Tools.angleDegreesZ(vector0);
			fail("Exception NaN not raised");
		} catch (NaNException e) {
			assertEquals(NaNException.MESSAGE,e.getMessage());
		}
		
		
		Point3D.Double v1 = Tools.vector3D(new Point3D.Double(0,10,-300),new Point3D.Double(0,10,0));
		assertEquals(v1.x,0,0.01);
		assertEquals(v1.y,0,0.01);
		assertEquals(v1.z,1,0.01);
		
	}
	
	@Test
	public void testImportImage3DObj() throws URISyntaxException, IOException {
		Image3DFactory.getInstance().create("missile2");
		
	}

	@Test
	public void calculAngle() {
		
		assertEquals(Tools.angleVectorVector(new Point3D.Double(0,0,1),new Point3D.Double(0,1,0)),Math.PI/2,0.001);
		assertEquals(Tools.angleVectorVector(new Point3D.Double(0,0,1),new Point3D.Double(0,-1,0)),Math.PI/2,0.001);
		assertEquals(Tools.angleVectorVector(new Point3D.Double(0,1,0),new Point3D.Double(0,0,1)),Math.PI/2,0.001);
		
		//cos(444/sqrt(444*444+(470-151)*(470-151)))*360/(2*PI) = 39°
		//o=(0,151,-444) bas=(0,151,0) haut=(0,470,0) 
		Point3D.Double v1=new Point3D.Double(0.0,0.5843047258450759,0.8115343414514944);
		Point3D.Double v2=new Point3D.Double(0.0,0.0,1.0);
		assertEquals(Tools.angleVectorVector(v1,v2),Math.cos(444/Math.sqrt(444*444+(470-151)*(470-151))),0.2);
//		0.0/16 PI=0.0
//		1.0/16 PI=0.19634954084936207
//		2.0/16 PI=0.39269908169872414
//		3.0/16 PI=0.5890486225480862
//		4.0/16 PI=0.7853981633974483
//		5.0/16 PI=0.9817477042468103
//		6.0/16 PI=1.1780972450961724
//		7.0/16 PI=1.3744467859455345
//		8.0/16 PI=1.5707963267948966
//		9.0/16 PI=1.7671458676442586
//		10.0/16 PI=1.9634954084936207
//		11.0/16 PI=2.1598449493429825
//		12.0/16 PI=2.356194490192345
//		13.0/16 PI=2.552544031041707
//		14.0/16 PI=2.748893571891069
//		15.0/16 PI=2.945243112740431
		int div=16;
		for (double i=0;i<div;i++) {
			double pas = i*(Math.PI/div);
			System.out.println(i+"/"+div+" PI="+pas);
		}
		Point3D.Double axeX = new Point3D.Double(1,0,0);
		
		assertEquals(0,acos(axeX,new Point3D.Double(1,0,0)),0.001);
		assertEquals(Math.PI/2,acos(axeX,new Point3D.Double(0,-1,0)),0.001);
		assertEquals(Math.PI,acos(axeX,new Point3D.Double(-1,0,0)),0.001);
		assertEquals(Math.PI/2,acos(axeX,new Point3D.Double(0,1,0)),0.001);

		assertEquals(Math.PI/4,acos(axeX,new Point3D.Double(1,-1,0)),0.001);
		assertEquals(Math.PI/2+Math.PI/4,acos(axeX,new Point3D.Double(-1,-1,0)),0.001);
		assertEquals(Math.PI/2+Math.PI/4,acos(axeX,new Point3D.Double(-1,1,0)),0.001);
		assertEquals(Math.PI/4,acos(axeX,new Point3D.Double(1,1,0)),0.001);
	}
		
	private Point3D.Double norm(
			Point3D.Double vector) {
		return Tools.vector3D(new Point3D.Double(), vector);
	}

	/**
	 * L'angle entre deux vecteur est toujours entre 0 et 180...
	 * @param i
	 * @param i2
	 * @return
	 */
	private double acos(
			Point3D.Double i,Point3D.Double i2) {
		Point3D.Double o = norm(i);
		Point3D.Double o2 = norm(i2);
		return Tools.angleVectorVector(o,o2);
	}
	
	@Test
	public void rotate3D() {
		Point3D.Double vecteur2D = new Point3D.Double(1,0,0);
		Point3D.Double rotVecteur90 = new Rotate3D(new Point3D.Double(0,0,1), Math.PI/2).projeter(vecteur2D );
		assertEquals(rotVecteur90.x, 0, 0.001);
		assertEquals(rotVecteur90.y, 1, 0.001);
		assertEquals(rotVecteur90.z, 0, 0.001);
	}
	
	@Test
	public void touchArrow3DCircle3D() {
		Point3D.Double p1;
		Point3D.Double p2;
		Point3D.Double c;
		double r;
		// a travers
		p1 = new Point3D.Double(1,0,0);
		p2 = new Point3D.Double(-1,0,0);
		c = new Point3D.Double();
		r = 0.5;
		assertTrue(Tools.touchArrow3DCircle3D(p(p1,p2) , c(c,r)));
		// par dessus
		p1 = new Point3D.Double(1,0,0);
		p2 = new Point3D.Double(0.4,0,0);
		c = new Point3D.Double();
		r = 0.5;
		assertTrue(Tools.touchArrow3DCircle3D(p(p1,p2) , c(c,r)));
		// presque par dessus
		p1 = new Point3D.Double(1,0,0);
		p2 = new Point3D.Double(0.6,0,0);
		c = new Point3D.Double();
		r = 0.5;
		assertFalse(Tools.touchArrow3DCircle3D(p(p1,p2) , c(c,r)));
		// par dessus légèrement décalé
		p1 = new Point3D.Double(1,0.1,0);
		p2 = new Point3D.Double(0.4,0.1,0);
		c = new Point3D.Double();
		r = 0.5;
		assertTrue(Tools.touchArrow3DCircle3D(p(p1,p2) , c(c,r)));
		//tangente
		p1 = new Point3D.Double(-1,0.4,0);
		p2 = new Point3D.Double(1,0.4,0);
		c = new Point3D.Double();
		r = 0.5;
		assertTrue(Tools.touchArrow3DCircle3D(p(p1,p2) , c(c,r)));
		
		
		// a travers
		p1 = new Point3D.Double(0,0,1);
		p2 = new Point3D.Double(0,0,-1);
		c = new Point3D.Double();
		r = 0.5;
		assertTrue(Tools.touchArrow3DCircle3D(p(p1,p2) , c(c,r)));
		// par dessus
		p1 = new Point3D.Double(0,0,1);
		p2 = new Point3D.Double(0,0,0.4);
		c = new Point3D.Double();
		r = 0.5;
		assertTrue(Tools.touchArrow3DCircle3D(p(p1,p2) , c(c,r)));
		// presque par dessus
		p1 = new Point3D.Double(0,0,1);
		p2 = new Point3D.Double(0,0,0.6);
		c = new Point3D.Double();
		r = 0.5;
		assertFalse(Tools.touchArrow3DCircle3D(p(p1,p2) , c(c,r)));
		// par dessus légèrement décalé
		p1 = new Point3D.Double(0,0.1,1);
		p2 = new Point3D.Double(0,0.1,0.4);
		c = new Point3D.Double();
		r = 0.5;
		assertTrue(Tools.touchArrow3DCircle3D(p(p1,p2) , c(c,r)));
		//tangente
		p1 = new Point3D.Double(0,0.4,-1);
		p2 = new Point3D.Double(0,0.4,1);
		c = new Point3D.Double();
		r = 0.5;
		assertTrue(Tools.touchArrow3DCircle3D(p(p1,p2) , c(c,r)));
				
	}

	
	
	
	
	private ICircle3D c(
			final Point3D.Double c,
			final double r) {
		return new ICircle3D() {
			@Override
			public Point3D.Double getPosition() {
				return c;
			}
			@Override
			public boolean isOriented() {
				return false;
			}
			@Override
			public double getRayon() {
				return r;
			}
			@Override
			public Point3D.Double getOrientation() {
				return null;
			}
		};
	}

	private IProjectile3D p(
			final Point3D.Double p1,
			final Point3D.Double p2) {
		return new IProjectile3D() {
			@Override
			public Point3D.Double getDevantOld() {
				return p1;
			}
			@Override
			public Point3D.Double getDevant() {
				return p2;
			}
			@Override
			public Point3D.Double getDerriere() {
				return null;
			}
		};
	}
	@Test
	public void computeOrientation() {
		Point3D.Double res =orientation(0, 0);
		assertEquals(res.x,0,0.01);
		assertEquals(res.y,0,0.01);
		assertEquals(res.z,1,0.01);
		
		res =orientation(Math.PI/2, 0);
		assertEquals(res.x,1,0.01);
		assertEquals(res.y,0,0.01);
		assertEquals(res.z,0,0.01);

		res =orientation(Math.PI, 0);
		assertEquals(res.x,0,0.01);
		assertEquals(res.y,0,0.01);
		assertEquals(res.z,-1,0.01);
		
		res =orientation(Math.PI+Math.PI/2, 0);
		assertEquals(res.x,-1,0.01);
		assertEquals(res.y,0,0.01);
		assertEquals(res.z,0,0.01);
		
		// si le canon franchie 180° c'est que le plateau a fait demi tour...
		for (double canon=-Math.PI/2;canon<Math.PI/2;canon +=0.2) {
			res =orientation(Math.PI+Math.PI/2, canon);	
			res.y=0;
			res=Tools.normalize(res);
			assertEquals(res.x,-1,0.01);
			assertEquals(res.y,0,0.01);
			assertEquals(res.z,0,0.01);
			
		}
		for (double canon=Math.PI/2+0.2;canon<Math.PI;canon +=0.2) {
			res =orientation(Math.PI+Math.PI/2, canon);	
			res.y=0;
			res=Tools.normalize(res);
			assertEquals(res.x,1,0.01);
			assertEquals(res.y,0,0.01);
			assertEquals(res.z,0,0.01);
			
		}
		for (double canon=-Math.PI;canon<-Math.PI/2;canon +=0.2) {
			res =orientation(Math.PI+Math.PI/2, canon);	
			res.y=0;
			res=Tools.normalize(res);
			assertEquals(res.x,1,0.01);
			assertEquals(res.y,0,0.01);
			assertEquals(res.z,0,0.01);
			
		}
		
//		res =orientation(Math.PI/2, 0);
//		
//		res.
	}

	private Point3D.Double orientation(double anglePlateau, double angleCanon) {
		Point3D.Double orientation = new Point3D.Double(0,0,1);
		orientation = new Rotate3D(new Point3D.Double(0,1,0),anglePlateau).projeter(orientation);
		Point3D.Double axeXprojeted=new Point3D.Double(1,0,0);
		axeXprojeted=new Rotate3D(new Point3D.Double(0,1,0),anglePlateau).projeter(axeXprojeted);
		orientation = new Rotate3D(axeXprojeted,angleCanon).projeter(orientation);
		
		return Tools.normalize(orientation);

	}
}
