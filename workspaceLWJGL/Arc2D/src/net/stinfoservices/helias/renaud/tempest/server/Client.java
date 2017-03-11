package net.stinfoservices.helias.renaud.tempest.server;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import net.stinfoservices.helias.renaud.tempest.TempestMain;
import net.stinfoservices.helias.renaud.tempest.agent.friend.Poisson;
import net.stinfoservices.helias.renaud.tempest.level.terrain.ITerrain2D;
import net.stinfoservices.helias.renaud.tempest.level.terrain.Killapede;
import net.stinfoservices.helias.renaud.tempest.tools.factory.TextureLoader;

public class Client implements Runnable {
	
	private static final boolean GUI = true;
	private static final boolean DEBUG = false;
	
	private static final int SCREEN_WIDTH = 400;
	private static final int SCREEN_HEIGHT = SCREEN_WIDTH;
	private Socket socket;
	private ObjectInputStream input;
	private Thread thread;
	private boolean stop;
	
	private ITerrain2D terrain = new Killapede();
	private ArrayList<MissileDTO> events;
	private TextureLoader tex;

	public Client() {
		try {
			if (GUI) {
				BufferedImage image = TextureLoader.loadImage(TempestMain.class.getResource("GREEN2.png"));//The path is inside the jar file
			    tex = new TextureLoader(image);
			}
			socket = new Socket("127.0.0.1", 12321);
			input = new ObjectInputStream(socket.getInputStream());
			thread = new Thread(this);
			thread.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			Object o;
			while ((o = input.readObject())!=null) {
				events = (ArrayList<MissileDTO>) o;
				if (DEBUG) {
					for (int i = 0;i<events.size();i++) {
						System.out.println("missile "+i+":"+events.get(i).getDevant().x+" "+events.get(i).getDevant().y);
					}
					System.out.println("");
				}
				try {
					Thread.sleep(50); // half of server
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("bye !");
		} catch (SocketException e) {
			if (e.getMessage().equals("Connection reset")) {
				System.out.println("reset");
			} else {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stop = true;
	}
	
	private Double projection(ITerrain2D terrain, Double devant) {
		
		terrain.getZoneWidth();
		
		double angle = (devant.x*Math.PI*2.0)/terrain.getZoneWidth();
		double rayon = (devant.y*((double)SCREEN_WIDTH)/2.0) / terrain.getZoneHeight();
		
		double x = Math.cos(angle)*rayon;
		double y = Math.sin(angle)*rayon;
		x+=(double)SCREEN_WIDTH/2.0;
		y+=(double)SCREEN_WIDTH/2.0;
		
		return new Double(x,y);
	}

	public static void main(String [] args) {
		
		if (GUI) {
			try {
				JFrame jFrame = new JFrame("Radar");
				// manque Display.destroy
				jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				jFrame.setLayout(new BorderLayout());
				Canvas parent = new Canvas();
				jFrame.add(parent, BorderLayout.CENTER);
				jFrame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
				jFrame.setVisible(true);
				Display.setParent(parent);
				// Display.setVSyncEnabled(true);
				Display.setDisplayMode(new DisplayMode(800, 600));
				Display.create();
			} catch (LWJGLException e) {
				e.printStackTrace();
				System.exit(0);
			}
		
			// init OpenGL here
			GL11.glLoadIdentity();
			GL11.glOrtho(0, SCREEN_WIDTH, 0, SCREEN_HEIGHT, 1, -1);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
	//		GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glClearColor(0.0F,0.0F,0.0F,1.0F);

		}
//		GL11.glEnable(GL11.GL_DEPTH_TEST);

		Client client = new Client();
		
		if (GUI) {
			while (!Display.isCloseRequested() && !client.isCloseRequested()) {
				// inputs
				//pollInputs();
	
				// tick
				//Timer.tick();
	
				// terrain et bulles
				//terrainUpdate();
				
				// Clear the screen and depth buffer
				GL11.glClear (GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
	
				// MY NAME IS BARBARIAN
				
				client.render();
				
				
				// matrix
				//matrixUpdate();

				Display.sync(20);

				// render OpenGL here
				Display.update();
	
				//checkScore();
			}

			Display.destroy();
			AL.destroy(); // sound system
			System.exit(0);
		}
	}

	private static Poisson p = new Poisson();
	
	private synchronized void render() {
		if (events == null || !GUI) return;
		for (MissileDTO m : events) {
			p.setPosition(projection(terrain,m.getDevant()));
			p.prepare(tex.getIdTex(), tex.getWidth(), tex.getHeight());
			p.render(0);
		}

	}

	private synchronized boolean isCloseRequested() {
		return stop;
	}
}
