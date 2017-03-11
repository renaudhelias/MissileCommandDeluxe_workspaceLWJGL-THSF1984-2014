package net.stinfoservices.helias.renaud.tempest.level.terrain;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

public class Tempest implements IArea2D {

	private final static int WIDTH=20;
	private final static int HEIGHT=10;
	
	Color color = Color.BLUE;
	Map<Integer, Map<Integer, ICase2D>> cases = new HashMap<Integer, Map<Integer, ICase2D>>();
	Map<Integer, Double> canons = new HashMap<Integer, Double>();
	private int zoneWidth;
	private int zoneHeight;

	public Tempest(int zoneWidth, int zoneHeight) {
		this.zoneWidth = zoneWidth;
		this.zoneHeight = zoneHeight;
		double step = 2*Math.PI/WIDTH;
		double currentStep = 0;
		double [] depths = new double[WIDTH];
		double offset = 0.1;
		for (int i = 0; i<WIDTH; i++) {
			// [0.9:1.0]
			depths[i]=Math.random()*0.2+0.8-offset;
		}
		for (int i = 0; i<WIDTH; i++) {
			double depth = depths[i];
			double nextDepth; 
			if (i+1==WIDTH) {
				nextDepth = depths[0];
			} else {
				nextDepth = depths[i+1];
			}
			
			cases.put(i, new HashMap<Integer,ICase2D>());
			for (int j=0;j<HEIGHT;j++) {
				List<Point2D.Double> points = new ArrayList<Point2D.Double>();
				points.add(new Point2D.Double(Math.cos(currentStep)*(offset+j*depth/(double)HEIGHT),Math.sin(currentStep)*(offset+j*depth/(double)HEIGHT)));
				points.add(new Point2D.Double(Math.cos(currentStep)*(offset+(j+1.0)*depth/(double)HEIGHT),Math.sin(currentStep)*(offset+(j+1.0)*depth/(double)HEIGHT)));
				points.add(new Point2D.Double(Math.cos(currentStep+step)*(offset+(j+1.0)*nextDepth/(double)HEIGHT),Math.sin(currentStep+step)*(offset+(j+1.0)*nextDepth/(double)HEIGHT)));
				points.add(new Point2D.Double(Math.cos(currentStep+step)*(offset+j*nextDepth/(double)HEIGHT),Math.sin(currentStep+step)*(offset+j*nextDepth/(double)HEIGHT)));
				
				//de-normalize
				for (Point2D.Double point : points) {
					point.x=((point.x+1.0)/2.0)*(double)zoneWidth;
					point.y=((point.y+1.0)/2.0)*(double)zoneHeight;
//					point.x=point.x*(double)screen.getScreenHeight()/2.0;
//					point.y=point.y*(double)screen.getScreenHeight()/2.0;
				}
				
				cases.get(i).put(j, new Case2D(points, i, j));
			}

			canons.put(i,new Point2D.Double(Math.cos(currentStep)*(offset/2),Math.sin(currentStep)*(offset/2)));

			
			currentStep+=step;
		}
		
//		verifCases();
		
		//de-normalize
		for (int i = 0; i<WIDTH; i++) {
			Point2D.Double point = canons.get(i);
			point.x=((point.x+1)/2)*zoneWidth;
			point.y=((point.y+1)/2)*zoneHeight;
		}

	}
	
	@Override
	public Double getCanon(int x) {
		return canons.get(x);
	}

	@Override
	public ICase2D getCase(int x, int y) {
		return cases.get(x).get(y);
	}

	@Override
	public int getWidth() {
		return WIDTH;
	}

	@Override
	public int getHeight() {
		return HEIGHT;
	}

	@Override
	public boolean isTerrainMirrorX() {
		return true;
	}

	@Override
	public boolean isTerrainMirrorY() {
		return false;
	}

	@Override
	public boolean render(int calque) {
		GL11.glColor3d(color.getRed()/256.0,color.getGreen()/256.0,color.getBlue()/256.0);
		GL11.glLineWidth(1);
		
		for (int x=0;x<getWidth();x++) {
			for (int y=0;y<getHeight();y++) {
				ICase2D c =getCase(x, y);
				Point2D.Double pointPrecedent = null;
				GL11.glBegin(GL11.GL_LINES);
				for (Point2D.Double point : c.getPoints()) {
					if (pointPrecedent != null) {
					    GL11.glVertex2d(pointPrecedent.x,pointPrecedent.y);
					    GL11.glVertex2d(point.x,point.y);
					}
					pointPrecedent = point;
				}
			    GL11.glEnd();
			    GL11.glVertex2d(pointPrecedent.x,pointPrecedent.y);
			    GL11.glVertex2d(c.getPoints().get(0).x, c.getPoints().get(0).y);
			}
		}
		return true;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public int getZoneWidth() {
		return zoneWidth;
	}

	@Override
	public int getZoneHeight() {
		return zoneHeight;
	}

	@Override
	public Point2D.Double getCaseSize() {
		return new Point2D.Double((double)zoneWidth/(double)WIDTH,(double)zoneHeight/(double)HEIGHT);
	}

}
