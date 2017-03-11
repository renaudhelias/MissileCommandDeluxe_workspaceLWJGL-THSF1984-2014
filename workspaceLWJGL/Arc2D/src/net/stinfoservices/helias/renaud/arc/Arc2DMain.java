package net.stinfoservices.helias.renaud.arc;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import javax.swing.JFrame;
import javax.swing.JLabel;

import net.stinfoservices.helias.renaud.tempest.IScreen;
import net.stinfoservices.helias.renaud.tempest.agent.friend.attaquant.Arc2DModel;
import net.stinfoservices.helias.renaud.tempest.agent.friend.attaquant.Arc2DRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.BulleModel;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.ArrowModel;
import net.stinfoservices.helias.renaud.tempest.level.terrain.IZone2D;
import net.stinfoservices.helias.renaud.tempest.level.terrain.TerrainModel;
import net.stinfoservices.helias.renaud.tempest.tools.Tools;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Timer;

/**
 * http://lwjgl.org/wiki/index.php?title=Main_Page Basics
 * 
 * @author STI
 * 
 */
public class Arc2DMain implements IScreen {

	private static final int SCREEN_WIDTH = 800;
	private static final int SCREEN_HEIGHT = 600;
	private static final int MOUSE_LEFT = 0;
	private static final int MOUSE_RIGHT = 1;
	/**
	 * Show line between haut and bas.
	 */
	private static final boolean DEBUG = false;
	private static final boolean DEBUG_ARC_CORD = false;
	private static final int NB_BULLES = 20;
	/**
	 * Let's hack, for just one mouse.
	 */
	Arc2DModel model = new Arc2DModel();
	Arc2DRenderer modelRenderer = new Arc2DRenderer();
	TerrainModel terrain = new TerrainModel(this);

	JLabel message = new JLabel("Arc demo");

	public void start() {
		try {
			JFrame jFrame = new JFrame("Arc game");
			// manque Display.destroy
			jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jFrame.setLayout(new BorderLayout());
			Canvas parent = new Canvas();
			jFrame.add(parent, BorderLayout.CENTER);
			jFrame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
			jFrame.setVisible(true);
			jFrame.add(message, BorderLayout.SOUTH);
			Display.setParent(parent);
			// Display.setVSyncEnabled(true);
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		// init OpenGL here
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, SCREEN_WIDTH, 0, SCREEN_HEIGHT, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		// init ennemis
		initGame();

		while (!Display.isCloseRequested()) {
			// inputs
			pollInputs();

			// tick
			Timer.tick();

			// terrain et bulles
			terrainUpdate();

			// matrix
			matrixUpdate();

			// render OpenGL here
			Display.update();

			checkScore();
		}

		Display.destroy();
		System.exit(0);
	}

	private void initGame() {
		arrowsCount = 0;
		impossibleCount = 0;
		terrain.setNbFusion(0);
		terrain.generate(NB_BULLES);
		Tools.nbSteps(true);
	}

	private int arrowsCount = 0;
	private int impossibleCount;

	private void checkScore() {
		if (terrain.getBulles().size() == 0) {
			int nbWin = NB_BULLES - terrain.getNbFusion();
			message.setText("Score : " + arrowsCount + " fleches pour " + nbWin
					+ " bulles touch�es !, vous avez laiss� passer "
					+ terrain.getNbFusion()
					+ " fusions de bulles, vous avez cass� " + impossibleCount
					+ " arcs.");
			initGame();
		}
	}

	private void terrainUpdate() {
		int nbSteps = Tools.nbSteps(false);
		for (int i = 0; i < nbSteps; i++) {
			terrain.stepBulles();
			terrain.stepArrow();
		}
	}

	private void matrixUpdate() {
		// Clear the screen and depth buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		//

		if (model.isCreating()) {
			Point2D.Double p = new Point2D.Double(Mouse.getX(), Mouse.getY());
			if (DEBUG_ARC_CORD) {
				drawArc(model.getPositionDebut1(), p);
			}
			drawArcCord(model.getPositionDebut1(), p, p);

			drawArrow(
					model.getPositionDebut1(),
					p,
					Math.sqrt((p.x - model.getPositionDebut1().x)
							* (p.x - model.getPositionDebut1().x)
							+ (p.y - model.getPositionDebut1().y)
							* (p.y - model.getPositionDebut1().y)));

		} else if (model.isUsing()) {
			Point2D.Double po = model.getPositionDebut1();
			Point2D.Double p = new Point2D.Double(Mouse.getX(), Mouse.getY());
			double taille = Math.sqrt((p.x - po.x) * (p.x - po.x)
					+ (p.y - po.y) * (p.y - po.y));
			Point2D.Double pArc = new Point2D.Double();
			pArc.x = po.x + ((p.x - po.x) * model.getTaille() / taille);
			pArc.y = po.y + ((p.y - po.y) * model.getTaille() / taille);
			if (DEBUG_ARC_CORD) {
				drawArc(model.getPositionDebut1(), pArc);
				drawCord(pArc, p);
			}
			drawArcCord(model.getPositionDebut1(), pArc, p);

			drawArrow(model.getPositionDebut1(), p, model.getTaille());
		}

		drawBulles();

	}

	private void drawBulles() {
		GL11.glColor3f(0.5f, 0.5f, 1.0f);
		GL11.glLineWidth(1);
		for (BulleModel bulle : terrain.getBulles()) {
			
			bulle.render();
			

		}
		if (terrain.getArrow() != null) {
			terrain.getArrow().render(0);
		}
	}

	private void drawArcCord(Double p1, Double p2, Double p3) {
		modelRenderer.refreshArc(p1, p2, p3);
		if (!modelRenderer.isImpossible()) {
			// Arc
			// set the color of the quad (R,G,B,A)
			GL11.glColor3f(0.5f, 0.5f, 1.0f);
			GL11.glLineWidth(10);

			// version 0.1
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d(modelRenderer.getHaut().x,
					modelRenderer.getHaut().y);
			GL11.glVertex2d(p1.x, p1.y);
			GL11.glVertex2d(p1.x, p1.y);
			GL11.glVertex2d(modelRenderer.getBas().x, modelRenderer.getBas().y);
			GL11.glEnd();
			// Cord
			// set the color of the quad (R,G,B,A)
			GL11.glColor3f(1.0f, 0.5f, 0.5f);
			GL11.glLineWidth(2);
			// version 0.1
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d(modelRenderer.getHaut().x,
					modelRenderer.getHaut().y);
			GL11.glVertex2d(p3.x, p3.y);
			GL11.glVertex2d(p3.x, p3.y);
			GL11.glVertex2d(modelRenderer.getBas().x, modelRenderer.getBas().y);
			GL11.glEnd();

			if (DEBUG) {
				// debug
				// set the color of the quad (R,G,B,A)
				GL11.glColor3f(0.5f, 1.0f, 0.5f);
				GL11.glLineWidth(2);
				// version 0.1
				GL11.glBegin(GL11.GL_LINES);
				GL11.glVertex2d(modelRenderer.getHaut().x,
						modelRenderer.getHaut().y);
				GL11.glVertex2d(modelRenderer.getBas().x,
						modelRenderer.getBas().y);
				GL11.glEnd();
			}

		}
	}

	private void drawCord(Double pFrom, Double pTo) {
		// set the color of the quad (R,G,B,A)
		GL11.glColor3f(1.0f, 0.5f, 0.5f);
		GL11.glLineWidth(2);

		// version 0
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(pFrom.x, pFrom.y);
		GL11.glVertex2d(pTo.x, pTo.y);
		GL11.glEnd();
	}

	private void drawArc(Double pFrom, Double pTo) {
		// set the color of the quad (R,G,B,A)
		GL11.glColor3f(0.5f, 0.5f, 1.0f);
		GL11.glLineWidth(5);
		// version 0
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(pFrom.x, pFrom.y);
		GL11.glVertex2d(pTo.x, pTo.y);
		GL11.glEnd();

	}

	private void drawArrow(Double pFrom, Double pTo, double tailleIn) {

		double taille = Math.sqrt((pFrom.x - pTo.x) * (pFrom.x - pTo.x)
				+ (pFrom.y - pTo.y) * (pFrom.y - pTo.y));
		Point2D.Double pArc = new Point2D.Double();
		pArc.x = pTo.x + ((pFrom.x - pTo.x) * (3 * tailleIn) / taille);
		pArc.y = pTo.y + ((pFrom.y - pTo.y) * (3 * tailleIn) / taille);

		// set the color of the quad (R,G,B,A)
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glLineWidth(4);
		// version 0
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(pArc.x, pArc.y);
		GL11.glVertex2d(pTo.x, pTo.y);
		GL11.glEnd();

	}

	private void pollInputs() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_A) {
				if (Keyboard.getEventKeyState()) {
					System.out.println("A pressed");
				} else {
					System.out.println("A realised");
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_Z) {
				if (Keyboard.getEventKeyState()) {
					System.out.println("Z pressed");
				} else {
					System.out.println("Z realised");
				}
			}
		}
		while (Mouse.next()) {
			if (Mouse.getEventButton() != -1) {
				if (Mouse.getEventButtonState()) {

					if (Mouse.getEventButton() == MOUSE_LEFT) {
						Point2D.Double p1 = new Point2D.Double(Mouse.getX(),
								Mouse.getY());
						// hacking
						model.setPositionFin1(p1);
						// hacking state
						model.setPositionDebut1(p1);
						System.out.println("Arc pointer at "
								+ model.getPositionDebut1().x + ","
								+ model.getPositionDebut1().y);
					} else if (Mouse.getEventButton() == MOUSE_RIGHT) {
						Point2D.Double p2 = new Point2D.Double(Mouse.getX(),
								Mouse.getY());
						// hacking
						model.setPositionDebut2(model.getPositionDebut1(), p2);
						System.out.println("Arc wood created at ("
								+ model.getPositionDebut1().x + ","
								+ model.getPositionDebut1().y + ")-(" + p2.x
								+ "," + p2.y + ") with fixed size of "
								+ model.getTaille());
					}

					System.out.println(Mouse.getEventButton()
							+ " clicked   at " + Mouse.getX() + ","
							+ Mouse.getY());
				} else {
					if (Mouse.getEventButton() == MOUSE_LEFT
							|| Mouse.getEventButton() == MOUSE_RIGHT) {
						if (model.isCreating()) {
							// undo
							Point2D.Double p2 = new Point2D.Double(
									Mouse.getX(), Mouse.getY());
							model.setPositionFin2(p2);
						} else if (model.isUsing()) {
							// action !
							Point2D.Double p2 = new Point2D.Double(
									Mouse.getX(), Mouse.getY());
							model.setPositionFin2(p2);
							// hacking
							model.setParcours1(0);
							model.setParcours2(Math.sqrt((p2.x - model
									.getPositionDebut1().x)
									* (p2.x - model.getPositionDebut1().x)
									+ (p2.y - model.getPositionDebut1().y)
									* (p2.y - model.getPositionDebut1().y)));
							System.out.println("fire ! (taille : "
									+ model.getTaille() + ", dexterity 0 : "
									+ model.getDexterite() + ", force : "
									+ model.getForce() + ")");

							// modeling the launched arrow
							double taille = model.getDistance();
							double tailleIn = model.getTaille();

							modelRenderer.refreshArc(model.getPositionDebut1(),
									model.getPositionDebut2(),
									model.getPositionFin2());
							if (modelRenderer.isImpossible()) {
								impossibleCount++;
							} else {
								Point2D.Double pArc = new Point2D.Double();
								Point2D.Double pFrom = model
										.getPositionDebut1();
								Point2D.Double pTo = p2;

								pArc.x = pTo.x
										+ ((pFrom.x - pTo.x) * (3 * tailleIn) / taille);
								pArc.y = pTo.y
										+ ((pFrom.y - pTo.y) * (3 * tailleIn) / taille);
								terrain.setArrow(new ArrowModel(new IZone2D() {

									@Override
									public int getZoneWidth() {
										return SCREEN_WIDTH;
									}

									@Override
									public int getZoneHeight() {
										return SCREEN_HEIGHT;
									}

								}, pArc, pTo,model.getForce()));
								arrowsCount++;
							}
						}
					}

					System.out.println(Mouse.getEventButton()
							+ " unclicked at " + Mouse.getX() + ","
							+ Mouse.getY());
				}
			}
		}

	}

	public static void main(String[] argv) {
		Arc2DMain displayExample = new Arc2DMain();
		displayExample.start();
	}

	@Override
	public int getScreenWidth() {
		return SCREEN_WIDTH;
	}

	@Override
	public int getScreenHeight() {
		return SCREEN_HEIGHT;
	}

}
