package net.stinfoservices.helias.renaud.tempest.level;

import java.awt.Color;
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
import net.stinfoservices.helias.renaud.tempest.IPosition2D;
import net.stinfoservices.helias.renaud.tempest.IScreen;
import net.stinfoservices.helias.renaud.tempest.TempestMain;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle2D;
import net.stinfoservices.helias.renaud.tempest.agent.IController;
import net.stinfoservices.helias.renaud.tempest.agent.IDangerous;
import net.stinfoservices.helias.renaud.tempest.agent.IHero;
import net.stinfoservices.helias.renaud.tempest.agent.IMove;
import net.stinfoservices.helias.renaud.tempest.agent.IMove2D;
import net.stinfoservices.helias.renaud.tempest.agent.IRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.friend.Maison;
import net.stinfoservices.helias.renaud.tempest.agent.friend.attaquant.Batterie;
import net.stinfoservices.helias.renaud.tempest.agent.friend.attaquant.Position2DAdapter;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.Bouclier;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.Frogger;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.IMonstre;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.Satellite;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.Serpent;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.ArrowModel;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile2D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.Missile;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.factory.IMissileFactory;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.factory.MissileFactory;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.special.Marqueur;
import net.stinfoservices.helias.renaud.tempest.level.terrain.IArea;
import net.stinfoservices.helias.renaud.tempest.level.terrain.IArea2D;
import net.stinfoservices.helias.renaud.tempest.level.terrain.ICase;
import net.stinfoservices.helias.renaud.tempest.level.terrain.ITerrain2D;
import net.stinfoservices.helias.renaud.tempest.level.terrain.Killapede;
import net.stinfoservices.helias.renaud.tempest.server.IPullMissiles;
import net.stinfoservices.helias.renaud.tempest.server.MissileDTO;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D;
import net.stinfoservices.helias.renaud.tempest.tools.Tools;
import net.stinfoservices.helias.renaud.tempest.tools.factory.TextureLoader;

/**
 * Level 0 original, with 2D objects
 * 
 * @author STI
 * 
 */
public class Level0 implements ILevel2D, IPullMissiles {


	private List<IMonstre> monstres = new ArrayList<IMonstre>();
	private List<IMonstre> friends = new ArrayList<IMonstre>();
	private List<IMonstre> nextMonstres = new ArrayList<IMonstre>();
	private List<IMonstre> nextFriends = new ArrayList<IMonstre>();

	private IScreen screen;

	private IArea2D terrain;

	private TextureLoader tex;
	private TextureLoader tex2;
	private TextureLoader tex3;
	private TextureLoader tex4;

	private Audio modStream;

	private Audio killSound;

	private Audio canonKillSound;
	private Audio canonFireSound;

	private TextureLoader texBunker;

	private TextureLoader texMissile;

	private TextureLoader texExplode;

	private TextureLoader texMarqueur;

	private Satellite satellite;
	private Double concernedPosition;
	private boolean concerned;



	public Level0(IScreen screen) {
		this.screen = screen;
		// do use singleton by here for texture.
		BufferedImage image = TextureLoader.loadImage(TempestMain.class.getResource("GREEN2.png"));//The path is inside the jar file
	    tex = new TextureLoader(image);
	    BufferedImage image2 = TextureLoader.loadImage(TempestMain.class.getResource("frogger_tiles3.png"));//The path is inside the jar file
	    tex2 = new TextureLoader(image2);
	    BufferedImage image3 = TextureLoader.loadImage(TempestMain.class.getResource("frogger_tiles.png"));//The path is inside the jar file
	    tex3 = new TextureLoader(image3);
	    BufferedImage image4 = TextureLoader.loadImage(TempestMain.class.getResource("capitole.png"));//The path is inside the jar file
	    tex4 = new TextureLoader(image4);
	    BufferedImage image5 = TextureLoader.loadImage(TempestMain.class.getResource("bunker.png"));//The path is inside the jar file
	    texBunker = new TextureLoader(image5);
	    BufferedImage image6 = TextureLoader.loadImage(TempestMain.class.getResource("missile.png"));//The path is inside the jar file
	    texMissile = new TextureLoader(image6);
	    BufferedImage image7 = TextureLoader.loadImage(TempestMain.class.getResource("explosion.png"));//The path is inside the jar file
	    texExplode = new TextureLoader(image7);
	    BufferedImage image8 = TextureLoader.loadImage(TempestMain.class.getResource("marqueur.png"));//The path is inside the jar file
	    texMarqueur = new TextureLoader(image8);
	    
	    try {
			modStream = AudioLoader.getStreamingAudio("MOD", ResourceLoader.getResource("testdata/sample.xm"));
			killSound = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("testdata/SmallExplosion8-Bit.ogg"));
			canonKillSound = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("testdata/EnemyDeath.ogg"));
			canonFireSound = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("testdata/Collision8-Bit.ogg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    setArea(new Killapede());
	    
	}
	

	@Override
	public void init() {
		// finished is dynamic here
	}


	

	@Override
	public void generateMonstres() {
		generateMonstres(Satellite.PHASES.PHASE_0.ordinal());
	}

	@Override
	public synchronized void generateMonstres(int phase) {
		
		// g�n�rateur de monstre par vagues
		if (phase == Satellite.PHASES.PHASE_0.ordinal()) {
			
			
			
			// purge
			
			for (IMonstre monstre : monstres) {
				monstre.dispose();
			}
			for (IMonstre friend : friends) {
				friend.dispose();
			}

			
			
			for (int x = 0; x < terrain.getWidth(); x++) {
				for (int y = 0; y < 3; y++) {
					Maison maison = new Maison(this,x,y);
					maison.prepare(tex4.getIdTex(),tex4.getWidth(),tex4.getHeight());
					//maison.prepare(killSound,modStream);
					friends.add(maison);
				}
			}
			
			IMissileFactory missileFactory = new MissileFactory(this);
			Batterie alpha = new Batterie(this,missileFactory ,3,0, Keyboard.KEY_I);
			Batterie delta = new Batterie(this,missileFactory,10,0, Keyboard.KEY_O);
			Batterie omega = new Batterie(this,missileFactory,17,0, Keyboard.KEY_P);
			friends.add(alpha);
			friends.add(delta);
			friends.add(omega);
			alpha.prepare(texBunker.getIdTex(),texBunker.getWidth(),texBunker.getHeight());
			delta.prepare(texBunker.getIdTex(),texBunker.getWidth(),texBunker.getHeight());
			omega.prepare(texBunker.getIdTex(),texBunker.getWidth(),texBunker.getHeight());
			alpha.prepare(canonKillSound, canonFireSound);
			delta.prepare(canonKillSound, canonFireSound);
			omega.prepare(canonKillSound, canonFireSound);
			
			missileFactory.prepareMissile(texMarqueur.getIdTex(), texMarqueur.getWidth(), texMarqueur.getHeight(), texMissile.getIdTex(), texMissile.getWidth(), texMissile.getHeight(), texExplode.getIdTex(), texExplode.getWidth(), texExplode.getHeight());
			
//			for (int i = 0; i < 30; i++) {
//				Serpent sss=new Serpent(this);
//				sss.prepare(tex.getIdTex(),tex.getWidth(),tex.getHeight());
//				sss.prepare(killSound,modStream);
//				monstres.add(sss);
//			}
//			for (int i = 0; i < 3; i++) {
//				Bouclier baf = new Bouclier(this);
//				baf.prepare(tex2.getIdTex(),tex2.getWidth(),tex2.getHeight());
//				baf.prepare(killSound,modStream);
//				monstres.add(baf);
//			}
//			for (int i = 0; i < 5; i++) {
//				Frogger frog = new Frogger(this);
//				frog.prepare(tex3.getIdTex(),tex3.getWidth(),tex3.getHeight());
//				//frog.prepare(killSound,modStream);
//				monstres.add(frog);
//			}
			
			satellite = new Satellite(this, missileFactory);
			
			
			
			for (int v = 0; v < 10; v++) {
				int victim = (int) Math.floor(Math.random()*(friends.size()-1));
				
				if (friends.get(victim) instanceof IMove) {
					// monstre qui bouge
					satellite.buildMissile(terrain.getCase((int) Math.floor(Math.random()*(terrain.getWidth()-1)), terrain.getHeight()-1), (IMove)friends.get(victim));
				} else if (friends.get(victim) instanceof IPosition2D) {
					// monstre qui ne bouge pas
					satellite.buildMissile(terrain.getCase((int) Math.floor(Math.random()*(terrain.getWidth()-1)), terrain.getHeight()-1), (IPosition2D)friends.get(victim));
				}
				
				
				canonFireSound.playAsSoundEffect(1.0f, 0.1f, false);
			}
			
			monstres.add(satellite);
			
			
		} else {
			for (int v = 0; v < 10; v++) {
				int victim = (int) Math.floor(Math.random()*(friends.size()-1));
				
				if (friends.get(victim) instanceof IMove) {
					// monstre qui bouge
					satellite.buildMissile(terrain.getCase((int) Math.floor(Math.random()*(terrain.getWidth()-1)), terrain.getHeight()-1), (IMove)friends.get(victim));
				} else if (friends.get(victim) instanceof IPosition2D) {
					// monstre qui ne bouge pas
					satellite.buildMissile(terrain.getCase((int) Math.floor(Math.random()*(terrain.getWidth()-1)), terrain.getHeight()-1), (IPosition2D)friends.get(victim));
				}
				canonFireSound.playAsSoundEffect(1.0f, 0.1f, false);
			}
		}
	}


	@Override
	public synchronized void step() {
		if (satellite != null && !satellite.isDeath()) {
			satellite.step(); // does call generateMonsters...
		}
		for (IMonstre monstre : monstres) {
			// they are alive !
			if (!(monstre instanceof Satellite)) {
				monstre.step();
			}
		}
		for (IMonstre friend : friends) {
			// you are alive !
			friend.step();
		}
		
		junkLists();
		impactFriendsMonsters();
		
		while (killMonsters()) {
		}
		while (killFriends()) {
		}
		if (satellite != null && !satellite.isDeath()) {
			while (satellite.killMissiles()) {
			}
		}
	}
	
	private void impactFriendsMonsters() {
		for (IMonstre friend : friends) {
			for (IMonstre monstre : monstres) {
				if (!monstre.isDeath()) {
					if (!friend.isDeath() && friend instanceof ICircle2D) {
						if (monstre.touch((ICircle2D)friend)) {
							monstre.kill(friend);
							friend.kill(monstre);
						}
					}
					if (!friend.isDeath() && friend instanceof IProjectile2D) {
						if (monstre.touch((IProjectile2D)friend)) {
							monstre.kill(friend);
							friend.kill(monstre);
						}
					}
					if (!monstre.isDeath() && monstre instanceof ICircle2D) {
						if (friend.touch((ICircle2D)monstre)) {
							friend.kill(monstre);
							monstre.kill(friend);
						}
					}
					if (!monstre.isDeath() && monstre instanceof IProjectile2D) {
						if (friend.touch((IProjectile2D)monstre)) {
							friend.kill(monstre);
							monstre.kill(friend);
						}
					}
				}
			}
		}
	}

	private boolean killMonsters() {
		for (IMonstre monstre : monstres) {
			if (monstre.isDeath()) {
				monstres.remove(monstre);
				return true;
			}
		}
		return false;
	}

	private boolean killFriends() {
		for (IMonstre friend : friends) {
			if (friend.isDeath()) {
				friends.remove(friend);
				return true;
			}
		}
		return false;
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
		terrain.setColor(Color.BLACK);
	}


	@Override
	public synchronized void addMonstre(IMonstre monstre) {
		nextMonstres.add(monstre);
	}


	@Override
	public synchronized void addFriend(IMonstre friend) {
		nextFriends.add(friend);
	}


	@Override
	public synchronized boolean key(int eventKey, boolean eventKeyState) {
		concerned = false;
		for (IMonstre friend : friends) {
			if (friend instanceof IController) {
				boolean test = ((IController)friend).key(eventKey,eventKeyState);
				if (test) {
					if (friend instanceof IMove2D) {
						concernedPosition = ((ICircle2D)friend).getPosition();
					}
					concerned = true;
				}
			}
		}
		junkLists();
		return concerned;
	}


	private void junkLists() {
		monstres.addAll(nextMonstres);
		nextMonstres.clear();
		friends.addAll(nextFriends);
		nextFriends.clear();
	}


	@Override
	public synchronized void mouse(int eventButton, boolean eventButtonState, Point2D.Double positionO) {
		// FIXME projection Controle
		Point2D.Double position;
		if (mouseSource()!=null) {
			position= Tools.projectionControle(new Point2D.Double(mouseSource().x,mouseSource().y), positionO, getArea());
		} else {
			position= Tools.projectionControle(null, positionO, getArea());
		}
		for (IMonstre friend : friends) {
			if (friend instanceof IController) {
				((IController)friend).mouse(eventButton,eventButtonState,position);
			}
		}
		junkLists();
	}

	public Double mouseSource() {
		if (concerned) {
			return concernedPosition;
		} else {
			return null;
		}
	}

	@Override
	public synchronized boolean render(int calque) {
		if (calque == 0) {
			terrain.render(0);
			return true;
		}
		if (calque>0 && calque-1 < friends.size()) {
			((IRenderer) friends.get(calque-1)).render(0);
			return true;
		}
		int n = calque-1-friends.size();
		if (n<monstres.size()) {
			if (monstres.get(n) instanceof IRenderer) {
				((IRenderer) monstres.get(n)).render(0);
			}
			return true;
		}
		return false;
	}


	@Override
	public synchronized Boolean finished() {
		if (!leaveDangerousMonsters(monstres) || !leaveDangerousMonsters(friends)) {
			if (!leaveDangerousMonsters(friends)) {
				return false;
			} else {
				return true;
			}
		} else {
			return null;
		}

	}
	private boolean leaveDangerousMonsters(List<IMonstre> monstres) {
		for (IMonstre monstre : monstres) {
			if (monstre instanceof IDangerous && !monstre.isDeath()) {
				return true;
			}
		}
		return false;
	}


	/**
	 * 
	 * @return serializable stuff
	 */
	public synchronized ArrayList<MissileDTO> getMissiles() {
		ArrayList<MissileDTO> missiles = new ArrayList<MissileDTO>();
		for (IMonstre monstre : monstres) {
			if (monstre instanceof Missile) {
				missiles.add(new MissileDTO((Missile)monstre));
			}
		}
		for (IMonstre friend : friends) {
			if (friend instanceof Missile) {
				missiles.add(new MissileDTO((Missile)friend));
			}
		}
		return missiles;
	}

	@Override
	public void setArea(IArea area) {
		setArea((IArea2D)area);
	}

	@Override
	public ICamera getCamera() {
		Point3D.Double position = new Point3D.Double(getArea().getZoneWidth()/2,0,0);
		Point3D.Double orientation = new Point3D.Double(0,0,1);
		// zoom relatif à la taille de l'écran et à la taille du jeu.
		double sw=getScreen().getScreenWidth();
		double sh=getScreen().getScreenHeight();
		double zw=getArea().getZoneWidth();
		double zh=getArea().getZoneHeight();
		double zoom = Math.sqrt(sw*sw+sh*sh)/Math.sqrt(zw*zw+zh*zh); 
		return new Camera(zoom,position, orientation);
	}

	@Override
	public int getMunitionsRestantes() {
		return 0;
	}

	@Override
	public int getMaisonsRestantes() {
		return 0;
	}

	@Override
	public int getMaxMunitions() {
		return 0;
	}

	@Override
	public int getMaxMaisons() {
		return 0;
	}

	@Override
	public void consumeMunition() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getScore() {
		return 0;
	}


	@Override
	public void setScore(int score) {
	}






}
