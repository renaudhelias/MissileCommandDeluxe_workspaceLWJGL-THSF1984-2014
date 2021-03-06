package net.stinfoservices.helias.renaud.tempest.level;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import net.stinfoservices.helias.renaud.tempest.Camera;
import net.stinfoservices.helias.renaud.tempest.ICamera;
import net.stinfoservices.helias.renaud.tempest.IScreen;
import net.stinfoservices.helias.renaud.tempest.TempestMain;
import net.stinfoservices.helias.renaud.tempest.agent.IRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.Bouclier;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.Frogger;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.IMonstre;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.start.SimpleImage;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.Missile;
import net.stinfoservices.helias.renaud.tempest.level.terrain.IArea;
import net.stinfoservices.helias.renaud.tempest.level.terrain.IArea2D;
import net.stinfoservices.helias.renaud.tempest.level.terrain.ITerrain2D;
import net.stinfoservices.helias.renaud.tempest.level.terrain.Killapede;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D;
import net.stinfoservices.helias.renaud.tempest.tools.factory.TextureLoader;
import net.stinfoservices.helias.renaud.tempest.tools.manager.IPrimitiveMouseListener;
import net.stinfoservices.helias.renaud.tempest.tools.manager.PrimitiveMouseManager;

public class ScoreLevel implements ILevel2D, IPrimitiveMouseListener {

	private static final int LONGUEUR_NAME = 3;
	
	private Boolean endOfLevel = null;

	private IScreen screen;

	private IArea2D terrain;

//	private TextureLoader tex2;

	private List<IMonstre> monstres = new ArrayList<IMonstre>();

	private int score;
	
	String alphabet="AZERTYUIOPQSDFGHJKLMWXCVBN";
	int curseurAlphabet = 0;
//	String name="";

	private boolean winner;

	private TextureLoader texExplode;

	private SimpleImage pressStartImage;

	private Missile missile;

	private Audio nuclear;

	public ScoreLevel(IScreen screen) {
		this.screen = screen;
//	    BufferedImage image2 = TextureLoader.loadImage(TempestMain.class.getResource("frogger_tiles3.png"));//The path is inside the jar file
//	    tex2 = new TextureLoader(image2);
	    BufferedImage image7 = TextureLoader.loadImage(TempestMain.class.getResource("explosion.png"));//The path is inside the jar file
	    texExplode = new TextureLoader(image7);
	    setArea(new Killapede());
	    try {
			nuclear = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("testdata/explosion_fin.ogg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void init() {
		PrimitiveMouseManager.getInstance().addListener(this);
		endOfLevel = null;
		monstres.clear();
	}
	
	@Override
	public boolean key(int eventKey, boolean eventKeyState) {
//		if (name.length() < LONGUEUR_NAME) {
//			if (eventKeyState && eventKey == Keyboard.KEY_LEFT) {
//				if (curseurAlphabet == 0) {
//					curseurAlphabet = alphabet.length()-1;
//				} else {
//					curseurAlphabet--;
//				}
//				System.out.println("Selecting : "+alphabet.charAt(curseurAlphabet));
//				return true;
//			}
//			if (eventKeyState && eventKey == Keyboard.KEY_RIGHT) {
//				if (curseurAlphabet == alphabet.length()-1) {
//					curseurAlphabet = 0;
//				} else {
//					curseurAlphabet++;
//				}
//				System.out.println("Selecting : "+alphabet.charAt(curseurAlphabet));
//				return true;
//			}
//			if (eventKeyState && eventKey == Keyboard.KEY_RETURN) {
//				name+=alphabet.charAt(curseurAlphabet);
//				System.out.println("name :"+name);
//				if (name.length() == LONGUEUR_NAME) {
//					monstres.add(pressStartImage);
//				}
//				return true;
//			}
//		} else if (name.length() == LONGUEUR_NAME) {
//			if (eventKeyState && eventKey == Keyboard.KEY_SPACE ) {
//				endOfLevel = true;
//			}
//		}
		return false;
	}

	@Override
	public void mouse(int eventButton, boolean eventButtonState, Double position) {
		
	}

	@Override
	public boolean render(int calque) {
		// les grenouilles
		if (calque < monstres.size()) {
			((IRenderer) monstres.get(calque)).render(0);
			return true;
		}
		return false;
	}

	@Override
	public void generateMonstres() {
		generateMonstres(0);
	}

	@Override
	public void generateMonstres(int phase) {
		if (winner) {
			monstres.add(new SimpleImage(this,"youWin.jpg"));
		} else {
			nuclear.playAsSoundEffect(1.0f, 0.1f, false);

			// force � z�ro donc explosion directe
			missile = new Missile(this, null , new Double(0, getArea().getZoneHeight()/2), null, 0, null, texExplode.getIdTex(), texExplode.getWidth(), texExplode.getHeight());
			monstres.add(missile);
			monstres.add(new SimpleImage(this,"theEnd.png"));
		}
		pressStartImage = new SimpleImage(this, "press_space.png", new Point2D.Double(0, getArea().getZoneHeight()*(1.0/5.0)));
		
//		
//		for (int i = 0; i < 3; i++) {
//			Bouclier baf = new Bouclier(this);
//			baf.prepare(tex2.getIdTex(),tex2.getWidth(),tex2.getHeight());
//			monstres.add(baf);
//		}
	}

	@Override
	public void step() {
		// les grenouilles s'animent
		for (IMonstre monstre : monstres) {
			monstre.step();
		}
	}

	@Override
	public void addMonstre(IMonstre monstre) {
	}

	@Override
	public void addFriend(IMonstre friend) {
	}

	@Override
	public IScreen getScreen() {
		return screen;
	}

	@Override
	public IArea2D getArea() {
		return terrain;
	}

	@Override
	public void setArea(IArea2D terrain) {
		this.terrain = terrain;
	}

	@Override
	public Boolean finished() {
		return endOfLevel;
	}
	@Override
	public ICamera getCamera() {
		
//		Point3D.Double position = new Point3D.Double(getArea().getZoneWidth()/2,0,0);
		Point3D.Double position = new Point3D.Double(0,0,0);
		Point3D.Double orientation = new Point3D.Double(0,0,1);
		// zoom relatif à la taille de l'écran et à la taille du jeu.
		double sw=getScreen().getScreenWidth();
		double sh=getScreen().getScreenHeight();
		double zw=getArea().getZoneWidth();
		double zh=getArea().getZoneHeight();
		double zoom = Math.sqrt(sw*sw+sh*sh)/Math.sqrt(zw*zw+zh*zh); 
		return new Camera(zoom,position, orientation);

//		
//		Point3D.Double position = new Point3D.Double(getArea().getZoneWidth()/2,0,0);
//		Point3D.Double orientation = new Point3D.Double(0,0,1);
//		// zoom relatif à la taille de l'écran et à la taille du jeu.
//		double sw=getScreen().getScreenWidth();
//		double sh=getScreen().getScreenHeight();
//		double zw=getArea().getZoneWidth();
//		double zh=getArea().getZoneHeight();
//		double zoom = Math.sqrt(sw*sw+sh*sh)/Math.sqrt(zw*zw+zh*zh); 
//		return new Camera(zoom,position, orientation);
	}
	@Override
	public void setArea(IArea area) {
		setArea((IArea2D)area);
	}
	@Override
	public int getMunitionsRestantes() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaisonsRestantes() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxMunitions() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxMaisons() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void consumeMunition() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePrimitiveClick(String mouseName, int i, int value) {
		System.out.println("click"+value);
		if ((missile == null || missile.isDeath()) && value==1) {
			if (nuclear.isPlaying()) {
				nuclear.stop();
			}
			endOfLevel = true;
		}
	}
	@Override
	public void mousePrimitiveMouve(String mouseName, int i, float value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getScore() {
		return score;
	}

	@Override
	public void setScore(int score) {
		this.score = score;
	}

	public void youWin(boolean winner) {
		this.winner=winner;
	}
	
}
