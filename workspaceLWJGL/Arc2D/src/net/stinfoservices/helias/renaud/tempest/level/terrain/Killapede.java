package net.stinfoservices.helias.renaud.tempest.level.terrain;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

public class Killapede implements IArea2D {

	private static final int WIDTH = 20;
	private static final int HEIGHT = 10;

	Map<Integer, Map<Integer, ICase2D>> cases = new HashMap<Integer, Map<Integer, ICase2D>>();
	Map<Integer, Double> canons = new HashMap<Integer, Double>();
	Color color = Color.RED;
	private int zoneWidth;
	private int zoneHeight;

	private Killapede(int zoneWidth, int zoneHeight) {
		this.zoneWidth = zoneWidth;
		this.zoneHeight = zoneHeight;
		// canons, 0 is bottom y.
		for (int i = 0; i < WIDTH; i++) {
			// vide = 0 pour un impact direct/ une g�ne, ou sinon viser � la fin
			// le joueur.

			// Le monstre :
			// se d�place sur la grille
			// se d�place sans utiliser la grille
			// se d�place vers le joueur
			int vide = 0;
			cases.put(i, new HashMap<Integer, ICase2D>());
			for (int j = 0; j < HEIGHT; j++) {
				List<Double> points = new ArrayList<Double>();

				double secousseX = (zoneWidth + 1) / WIDTH
						* Math.random() * 0.2 - 0.1
						* (zoneWidth + 1) / WIDTH;
				double secousseY = (zoneWidth + 1) / WIDTH
						* Math.random() * 0.2 - 0.1
						* (zoneWidth + 1) / WIDTH;

				points.add(new Double(secousseX + i
						* (zoneWidth + 1) / WIDTH, secousseY
						+ (j + vide) * (zoneHeight + 1)
						/ (HEIGHT + vide)));
				points.add(new Double(secousseX + (i + 1)
						* (zoneWidth + 1) / WIDTH, secousseY
						+ (j + vide) * (zoneHeight + 1)
						/ (HEIGHT + vide)));
				points.add(new Double(secousseX + (i + 1)
						* (zoneWidth + 1) / WIDTH, secousseY
						+ ((j + vide) + 1) * (zoneHeight + 1)
						/ (HEIGHT + vide)));
				points.add(new Double(secousseX + i
						* (zoneWidth + 1) / WIDTH, secousseY
						+ ((j + vide) + 1) * (zoneHeight + 1)
						/ (HEIGHT + vide)));
				cases.get(i).put(j, new Case2D(points, i, j));
			}
		}
		for (int i = 0; i < WIDTH; i++) {
			int vide = 1;
			canons.put(
					i,
					new Double(
							i * (zoneWidth + 1) / WIDTH
									+ ((zoneWidth + 1) / WIDTH)
									/ 2,
							((zoneHeight + 1) / (HEIGHT + vide)) / 2));
		}
	}

	public Killapede() {
		// fixed zone size.
		this(800,600);
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
		return false;
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
