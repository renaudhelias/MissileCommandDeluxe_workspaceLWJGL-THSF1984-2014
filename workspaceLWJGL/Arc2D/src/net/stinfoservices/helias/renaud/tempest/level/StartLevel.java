package net.stinfoservices.helias.renaud.tempest.level;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import net.stinfoservices.helias.renaud.tempest.Camera;
import net.stinfoservices.helias.renaud.tempest.ICamera;
import net.stinfoservices.helias.renaud.tempest.IScreen;
import net.stinfoservices.helias.renaud.tempest.TempestMain;
import net.stinfoservices.helias.renaud.tempest.agent.IRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.Frogger;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.IMonstre;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.start.SimpleImage;
import net.stinfoservices.helias.renaud.tempest.level.terrain.IArea;
import net.stinfoservices.helias.renaud.tempest.level.terrain.IArea2D;
import net.stinfoservices.helias.renaud.tempest.level.terrain.Killapede;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D;
import net.stinfoservices.helias.renaud.tempest.tools.factory.TextureLoader;
import net.stinfoservices.helias.renaud.tempest.tools.manager.IPrimitiveMouseListener;
import net.stinfoservices.helias.renaud.tempest.tools.manager.PrimitiveMouseManager;

public class StartLevel implements ILevel2D, IPrimitiveMouseListener {

	private static final int NB_STEP_SLIDE=300;
	/**
	 * Histoire de
	 */
	private static final int NB_STEP_AVANT_PRESS_SPACE = 200;
	
	private int countDown = NB_STEP_AVANT_PRESS_SPACE;
	private int countDown2 = NB_STEP_AVANT_PRESS_SPACE;
	private int countDownSlides = NB_STEP_SLIDE;
	private int noSlide=0;
	private Boolean endOfLevel = null;

	private IScreen screen;

	private IArea2D terrain;

//	private TextureLoader tex3;

	private List<IMonstre> monstres = new ArrayList<IMonstre>();

	private SimpleImage pressStartImage;

	private Audio xenon2Music;

	private String[] slides = new String [] {"ecran_titre.PNG","ecran_titre.PNG","ecran_titre.PNG","howtoplay1.PNG","howtoplay2.PNG","howtoplay3.PNG"};

	private SimpleImage slide;
	private int countDown1;
	public StartLevel(IScreen screen) {
		this.screen = screen;
//	    BufferedImage image3 = TextureLoader.loadImage(TempestMain.class.getResource("frogger_tiles.png"));//The path is inside the jar file
//	    tex3 = new TextureLoader(image3);
	    setArea(new Killapede());
	    try {
	    	xenon2Music = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("testdata/xenon2.ogg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

	@Override
	public void init() {
		Mouse.setGrabbed(true);
		PrimitiveMouseManager.getInstance().addListener(this);
		endOfLevel=null;
		countDown = NB_STEP_AVANT_PRESS_SPACE;
		monstres.clear();
		
		xenon2Music.playAsSoundEffect(1.0f, 0.1f, false);

	}
	
	@Override
	public boolean key(int eventKey, boolean eventKeyState) {
//		if (countDown == 0) {
//			if (eventKey == Keyboard.KEY_SPACE) {
//				endOfLevel = true;
//				return true;
//			}
//		}
		return false;
	}

	@Override
	public void mouse(int eventButton, boolean eventButtonState, Double position) {
	}

	@Override
	public boolean render(int calque) {
		if (calque == 0){
			slide.render(calque);
			return true;
		}
		// les grenouilles
		if (calque < monstres.size()+1) {
			((IRenderer) monstres.get(calque-1)).render(0);
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
		pressStartImage  = new SimpleImage(this, "press_space.png", new Point2D.Double(0, getArea().getZoneHeight()*(1.0/5.0)));
		noSlide=0;
		slide =new SimpleImage(this, slides[noSlide]);
		
//		for (int i = 0; i < 5; i++) {
//			Frogger frog = new Frogger(this);
//			frog.prepare(tex3.getIdTex(),tex3.getWidth(),tex3.getHeight());
//			//frog.prepare(killSound,modStream);
//			monstres.add(frog);
//		}
	}

	@Override
	public void step() {
		if (countDownSlides== 0) {
			noSlide++;
			if (noSlide>=slides.length) noSlide=0;
			slide =new SimpleImage(this, slides[noSlide]);
			countDownSlides = NB_STEP_SLIDE;
		} else {
			countDownSlides--;
		}
		if (countDown1 == 0) {
			if (countDown2 == 0) {
				countDown1=NB_STEP_AVANT_PRESS_SPACE;
				countDown2=NB_STEP_AVANT_PRESS_SPACE;
			} else {
				countDown2--;
			}
			// les grenouilles bouges
			for (IMonstre monstre : monstres) {
				monstre.step();
			}
		} else {
			countDown1--;
		}
		if (countDown == 0) {
			// rien
		} else {
			countDown --;
			System.out.println("Start demo countDown : "+countDown);
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
		Point3D.Double position = new Point3D.Double(0,0,0);
		Point3D.Double orientation = new Point3D.Double(0,0,1);
		// zoom relatif à la taille de l'écran et à la taille du jeu.
		double sw=getScreen().getScreenWidth();
		double sh=getScreen().getScreenHeight();
		double zw=getArea().getZoneWidth();
		double zh=getArea().getZoneHeight();
		double zoom = Math.sqrt(sw*sw+sh*sh)/Math.sqrt(zw*zw+zh*zh); 
		return new Camera(zoom,position, orientation);
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
		if (countDown == 0) {
			if (value == 1) {
				xenon2Music.stop();
				endOfLevel = true;
			}
		}
	}


	@Override
	public void mousePrimitiveMouve(String mouseName, int i, float value) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getScore() {
		return -1;
	}


	@Override
	public void setScore(int score) {
	}



}
