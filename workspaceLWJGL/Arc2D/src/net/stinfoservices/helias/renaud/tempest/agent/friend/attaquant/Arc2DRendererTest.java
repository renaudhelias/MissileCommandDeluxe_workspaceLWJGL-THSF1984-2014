package net.stinfoservices.helias.renaud.tempest.agent.friend.attaquant;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;

import org.junit.Test;

public class Arc2DRendererTest {

	@Test
	public void testImpossible() {
		
		Arc2DRenderer modelRenderer = new Arc2DRenderer();
		modelRenderer.setRatio(1);
		Point2D.Double p1 = new Point2D.Double(0,0);
		Point2D.Double p2 = new Point2D.Double(1,0);
		Point2D.Double p3 = new Point2D.Double(3,0);
		modelRenderer.refreshArc(p1, p2, p3);
		assertTrue(modelRenderer.isImpossible());
	}
	
	@Test
	public void testTirerX() {
		Arc2DRenderer modelRenderer = new Arc2DRenderer();
		modelRenderer.setRatio(3);
		Point2D.Double p1 = new Point2D.Double(0,0);
		Point2D.Double p2 = new Point2D.Double(1,0);
		Point2D.Double p3 = new Point2D.Double(2,0);
		modelRenderer.refreshArc(p1, p2, p3);
		assertFalse(modelRenderer.isImpossible());
		assertEquals(modelRenderer.getHaut().y,-modelRenderer.getBas().y,0.01);
		assertEquals(1.25,modelRenderer.getHaut().x,0.01);
		assertEquals(1.25,modelRenderer.getBas().x,0.01);
	}

	@Test
	public void testTirerY() {
		Arc2DRenderer modelRenderer = new Arc2DRenderer();
		modelRenderer.setRatio(3);
		Point2D.Double p1 = new Point2D.Double(0,0);
		Point2D.Double p2 = new Point2D.Double(0,1);
		Point2D.Double p3 = new Point2D.Double(0,2);
		modelRenderer.refreshArc(p1, p2, p3);
		assertFalse(modelRenderer.isImpossible());
		assertEquals(modelRenderer.getHaut().x,-modelRenderer.getBas().x,0.01);
		assertEquals(1.25,modelRenderer.getHaut().y,0.01);
		assertEquals(1.25,modelRenderer.getBas().y,0.01);
	}

}
