package net.stinfoservices.helias.renaud.tempest.level;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import net.stinfoservices.helias.renaud.tempest.Camera;
import net.stinfoservices.helias.renaud.tempest.ICamera;
import net.stinfoservices.helias.renaud.tempest.IPosition3D;
import net.stinfoservices.helias.renaud.tempest.IScreen;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle3D;
import net.stinfoservices.helias.renaud.tempest.agent.IController;
import net.stinfoservices.helias.renaud.tempest.agent.IDangerous;
import net.stinfoservices.helias.renaud.tempest.agent.IHero;
import net.stinfoservices.helias.renaud.tempest.agent.IMove;
import net.stinfoservices.helias.renaud.tempest.agent.IRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.friend.Maison3D;
import net.stinfoservices.helias.renaud.tempest.agent.friend.attaquant.MissileLauncher3D;
import net.stinfoservices.helias.renaud.tempest.agent.friend.attaquant.Position3DAdapter;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.IMonstre;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.Satellite;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile3D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.Missile;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.factory.MissileFactory3D;
import net.stinfoservices.helias.renaud.tempest.level.properties.Scenario;
import net.stinfoservices.helias.renaud.tempest.level.terrain.IArea;
import net.stinfoservices.helias.renaud.tempest.level.terrain.IArea3D;
import net.stinfoservices.helias.renaud.tempest.level.terrain.ICase3D;
import net.stinfoservices.helias.renaud.tempest.level.terrain.Puits;
import net.stinfoservices.helias.renaud.tempest.server.IPullMissiles;
import net.stinfoservices.helias.renaud.tempest.server.MissileDTO;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D.Integer;

/**
 * Level 0 is space invader-like.
 * 
 * @author STI
 * 
 */
public class Level2 implements ILevel3D, IPullMissiles {

//	public enum PHASES {PHASE_0, PHASE_1, PHASE_2, PHASE_3, PHASE_4, PHASE_5, PHASE_6, PHASE_7};
	public final static boolean DEMO= false;

	private List<IMonstre> monstres = new ArrayList<IMonstre>();
	private List<IMonstre> friends = new ArrayList<IMonstre>();
	private List<IMonstre> nextMonstres = new ArrayList<IMonstre>();
	private List<IMonstre> nextFriends = new ArrayList<IMonstre>();

	private IScreen screen;

	private IArea3D terrain;

	private Audio finAlertBoum;
	private Audio[] alertAlert;


	private Satellite satellite;



	public Level2(IScreen screen) {
		this.screen = screen;
	    
	    try {
			finAlertBoum = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("testdata/explosion_longue_12_ sec.ogg"));
			alertAlert = new Audio[] {
					AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("testdata/alarme_voix.ogg")),
					AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("testdata/alarme_16_sec.ogg")),
					AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("testdata/voix_round.ogg"))
			};
		} catch (IOException e) {
			e.printStackTrace();
		}
	    setArea(new Puits());

	    
	}
	
	@Override
	public void init() {
		// fionished is dynamic here
		monstres.clear();
		missileLauncher3D=null;
		friends.clear();
		score = 0;
	}

	@Override
	public void generateMonstres() {
		// hide mouse :)
		//buggy lol 2
		Mouse.setGrabbed(true);
		generateMonstres(Satellite.PHASES.PHASE_0.ordinal());
	}

	MissileFactory3D missileFactory;
	
	private MissileLauncher3D missileLauncher3D;

	private int maxMaisons;

	private int maxMunitions;

	private int munitions;

	private boolean finAlert;

	private Audio currentAlertAlert;

	private int niveau =-1;
	private int vague = -1;

	private int pause;

	private int score;

	private void deduceNiveauVague(int phase) {
		int c=0;
		for (int n=0;n<Scenario.getInstance().getNbNiveaux();n++) {
			for (int v=0;v<Scenario.getInstance().getNbVagues(n);v++) {
				c++;
				if (c-1==phase){
					niveau = n;
					vague = v;
					return;
				}
			}
		}
	}
	@Override
	public synchronized void generateMonstres(int phase) {
		deduceNiveauVague(phase);
		if (vague == -1) {
			return; // EndOfGame
		} else {
			score +=Scenario.getInstance().getScoreParMaison(niveau, vague)*getMaisonsRestantes();
		}
		if (niveau == 0 && vague == 0) {
			// purge
			for (IMonstre monstre : monstres) {
				monstre.dispose();
			}
			for (IMonstre friend : friends) {
				friend.dispose();
			}

			// Maison � construire
			for (int m=0;m<Scenario.getInstance().getNbMaisons(niveau);m++) {
				Point3D.Integer coords = Scenario.getInstance().getMaisonCoords(phase, m);
				String name = Scenario.getInstance().getMaisonName(phase, m);
				double ratio = Scenario.getInstance().getMaisonRatio(phase, m);
				Maison3D maison = new Maison3D(this,coords,name, ratio);
				friends.add(maison);
			}

			missileFactory = new MissileFactory3D(this);
			
			missileLauncher3D = new MissileLauncher3D(this, missileFactory);
			friends.add(missileLauncher3D);
			
			
			satellite = new Satellite(this, missileFactory);
			monstres.add(satellite);

			maxMaisons=getMaisonsRestantes();
			
			munitions=0;
		}
		if (vague == 0) {
			munitions += Scenario.getInstance().getMunitions(niveau);
			maxMunitions = munitions;
		}
		

		
		for (int v = 0; v < Scenario.getInstance().getNbMissiles(niveau, vague); v++) {
			int victim = (int) Math.floor(Math.random()*(friends.size()-1));
			Point3D.Double victimPosition = new Point3D.Double();
			if (!(friends.get(victim) instanceof IHero)) continue;
			
			if (friends.get(victim) instanceof IMove) {
				// monstre qui bouge
				victimPosition = MissileFactory3D.adapt((IMove)friends.get(victim));
			} else if (friends.get(victim) instanceof IPosition3D) {
				// monstre qui ne bouge pas
				victimPosition = ((IPosition3D)friends.get(victim)).getPosition();
			}
			// j'ai une victime
			// il faut donc un lanceur !
			 
			// attaque de l'extérieur donc + 1-3 crant vers l'extérieur
			// après un random exterieur+droite+gauche
			double ax = terrain.getZoneSize().x/2;
			double az = terrain.getZoneSize().z/2;
			Point3D.Double position3D=new Point3D.Double(0,terrain.getZoneSize().y,0);
			position3D.x=victimPosition.x;
			position3D.z=victimPosition.z;
			
			double RAND_X=Scenario.getInstance().getMissileAngle(niveau, vague, v).x;
			double RAND_Z=Scenario.getInstance().getMissileAngle(niveau, vague, v).z;
			if (victimPosition.x<=ax && victimPosition.z<=az) {
				position3D.x-=Math.floor(Math.random()*RAND_X)*terrain.getCaseSize().x;
				position3D.z-=Math.floor(Math.random()*RAND_Z)*terrain.getCaseSize().z;
			} else if (victimPosition.x>=ax && victimPosition.z<=az) {
				position3D.x+=Math.floor(Math.random()*RAND_X)*terrain.getCaseSize().x;
				position3D.z-=Math.floor(Math.random()*RAND_Z)*terrain.getCaseSize().z;
			} else if (victimPosition.x>=ax && victimPosition.z>=az) {
				position3D.x+=Math.floor(Math.random()*RAND_X)*terrain.getCaseSize().x;
				position3D.z+=Math.floor(Math.random()*RAND_Z)*terrain.getCaseSize().z;
			} else if (victimPosition.x<=ax && victimPosition.z>=az) {
				position3D.x-=Math.floor(Math.random()*RAND_X)*terrain.getCaseSize().x;
				position3D.z+=Math.floor(Math.random()*RAND_Z)*terrain.getCaseSize().z;
			}
			// correction out of screen
			if (position3D.x<0) position3D.x = 0;
			if (position3D.z<0) position3D.z = 0;
			if (position3D.x>getArea().getZoneSize().x) position3D.x = getArea().getZoneSize().x-1;
			if (position3D.z>getArea().getZoneSize().z) position3D.z = getArea().getZoneSize().z-1;
			
			
			IPosition3D lanceur = new Position3DAdapter(position3D);
			
			
			if (friends.get(victim) instanceof IMove) {
				// monstre qui bouge
				//satellite.buildMissile(terrain.getCase(new Point3D.Integer((int) Math.floor(Math.random()*(terrain.getTerrainSize().x-1)), terrain.getTerrainSize().y-1 ,0)),(IMove)friends.get(victim));
				satellite.buildMissile(lanceur,(IMove)friends.get(victim), Scenario.getInstance().getMissilePeriod(niveau, vague, v));
			} else if (friends.get(victim) instanceof IPosition3D) {
				// monstre qui ne bouge pas
//					satellite.buildMissile(terrain.getCase(new Point3D.Integer((int) Math.floor(Math.random()*(terrain.getTerrainSize().x-1)), terrain.getTerrainSize().y-1,0)), (IPosition3D)friends.get(victim));
				satellite.buildMissile(lanceur,(IMove)friends.get(victim), Scenario.getInstance().getMissilePeriod(niveau, vague, v));
			}
		}

		
		
		
		
		
//		// g�n�rateur de monstre par vagues
//		if (phase == Satellite.PHASES.PHASE_0.ordinal()) {
//			
//			
//			
////			// purge
////			
////			for (IMonstre monstre : monstres) {
////				monstre.dispose();
////			}
////			for (IMonstre friend : friends) {
////				friend.dispose();
////			}
////
////			
////			
////			for (int x = 0; x < 20; x++) {
////				for (int y = 0; y < 4; y++) {
////					Maison3D maison = new Maison3D(this,new Point3D.Integer(x,y,19));
////					friends.add(maison);
////				}
////			}
////			
////			missileFactor)y = new MissileFactory3D(this);
////			
////			missileLauncher3D = new MissileLauncher3D(this, missileFactory);
////			friends.add(missileLauncher3D);
////			
////			
////			satellite = new Satellite(this, missileFactory);
////			monstres.add(satellite);
////			
////			
////			maxMaisons=getMaisonsRestantes();
////			
////			munitions = 20;
////			maxMunitions = munitions;
////			
//		}
//		for (int v = 0; v < 10; v++) {
//			int victim = (int) Math.floor(Math.random()*(friends.size()-1));
//			Point3D.Double victimPosition = new Point3D.Double();
//			if (!(friends.get(victim) instanceof IHero)) continue;
//			
//			if (friends.get(victim) instanceof IMove) {
//				// monstre qui bouge
//				victimPosition = MissileFactory3D.adapt((IMove)friends.get(victim));
//			} else if (friends.get(victim) instanceof IPosition3D) {
//				// monstre qui ne bouge pas
//				victimPosition = ((IPosition3D)friends.get(victim)).getPosition();
//			}
//			// j'ai une victime
//			// il faut donc un lanceur !
//			 
//			// attaque de l'extérieur donc + 1-3 crant vers l'extérieur
//			// après un random exterieur+droite+gauche
//			double ax = terrain.getZoneSize().x/2;
//			double az = terrain.getZoneSize().z/2;
//			Point3D.Double position3D=new Point3D.Double(0,terrain.getZoneSize().y,0);
//			position3D.x=victimPosition.x;
//			position3D.z=victimPosition.z;
//			
//			double RAND=4;
//			if (victimPosition.x<=ax && victimPosition.z<=az) {
//				position3D.x-=Math.floor(Math.random()*RAND)*terrain.getCaseSize().x;
//				position3D.z-=Math.floor(Math.random()*RAND)*terrain.getCaseSize().z;
//			} else if (victimPosition.x>=ax && victimPosition.z<=az) {
//				position3D.x+=Math.floor(Math.random()*RAND)*terrain.getCaseSize().x;
//				position3D.z-=Math.floor(Math.random()*RAND)*terrain.getCaseSize().z;
//			} else if (victimPosition.x>=ax && victimPosition.z>=az) {
//				position3D.x+=Math.floor(Math.random()*RAND)*terrain.getCaseSize().x;
//				position3D.z+=Math.floor(Math.random()*RAND)*terrain.getCaseSize().z;
//			} else if (victimPosition.x<=ax && victimPosition.z>=az) {
//				position3D.x-=Math.floor(Math.random()*RAND)*terrain.getCaseSize().x;
//				position3D.z+=Math.floor(Math.random()*RAND)*terrain.getCaseSize().z;
//			}
//			
//			
//			IPosition3D lanceur = new Position3DAdapter(position3D);
//			
//			
//			if (friends.get(victim) instanceof IMove) {
//				// monstre qui bouge
//				//satellite.buildMissile(terrain.getCase(new Point3D.Integer((int) Math.floor(Math.random()*(terrain.getTerrainSize().x-1)), terrain.getTerrainSize().y-1 ,0)),(IMove)friends.get(victim));
//				satellite.buildMissile(lanceur,(IMove)friends.get(victim));
//			} else if (friends.get(victim) instanceof IPosition3D) {
//				// monstre qui ne bouge pas
////					satellite.buildMissile(terrain.getCase(new Point3D.Integer((int) Math.floor(Math.random()*(terrain.getTerrainSize().x-1)), terrain.getTerrainSize().y-1,0)), (IPosition3D)friends.get(victim));
//				satellite.buildMissile(lanceur,(IMove)friends.get(victim));
//			}
//		}
//		if (leaveDangerousMonsters(friends)) {
			// BUG : music relanc� alors que level fini et ScoreLevel
		pause = Scenario.getInstance().getPause(niveau, phase);
			if (currentAlertAlert != null) currentAlertAlert.stop();
			currentAlertAlert = alertAlert[((int)(Math.random()*3))%3];
			currentAlertAlert.playAsSoundEffect(1.0f, 0.1f, false);
			finAlert=true;
//		}			
			
			
			
//		} else {
//			for (int v = 0; v < 10; v++) {
//				int victim = (int) Math.floor(Math.random()*(friends.size()-1));
//				
//				if (friends.get(victim) instanceof IMove) {
//					// monstre qui bouge
//					satellite.buildMissile(terrain.getCase(new Point3D.Integer((int) Math.floor(Math.random()*(terrain.getTerrainSize().x-1)), terrain.getTerrainSize().y-1,0)), (IMove)friends.get(victim));
//				} else if (friends.get(victim) instanceof IPosition3D) {
//					// monstre qui ne bouge pas
//					satellite.buildMissile(terrain.getCase(new Point3D.Integer((int) Math.floor(Math.random()*(terrain.getTerrainSize().x-1)), terrain.getTerrainSize().y-1,0)), (IPosition3D)friends.get(victim));
//				}
//				
//				
//				canonFireSound.playAsSoundEffect(1.0f, 0.1f, false);
//			}
//		}
	}


	@Override
	public synchronized void step() {
		if (pause>0) pause--;
		if (finAlert && (!currentAlertAlert.isPlaying() || pause==0)) {
			currentAlertAlert.stop();
			// on enchaine grave !
			finAlert = false;
			finAlertBoum.playAsSoundEffect(1.0f, 0.1f, false);
		} 
		if (satellite != null && !satellite.isDeath()) {
			satellite.step(); // does call generateMonsters...
		}
		if (!finAlert) {
			// pause de 12 sec !
			for (IMonstre monstre : monstres) {
				// they are alive !
				if (!(monstre instanceof Satellite)) {
					monstre.step();
				}
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
					
					// and zone ???????? dam'n !
					// @see Tools.touch()
					// 3D, only 3D...
					if (!friend.isDeath() && friend instanceof ICircle3D) {
						if (monstre.touch((ICircle3D)friend)) {
							monstre.kill(friend);
							friend.kill(monstre);
						}
					}
					if (!friend.isDeath() && friend instanceof IProjectile3D) {
						if (monstre.touch((IProjectile3D)friend)) {
							monstre.kill(friend);
							friend.kill(monstre);
						}
					}
					if (!monstre.isDeath() && monstre instanceof ICircle3D) {
						if (friend.touch((ICircle3D)monstre)) {
							friend.kill(monstre);
							monstre.kill(friend);
						}
					}
					if (!monstre.isDeath() && monstre instanceof IProjectile3D) {
						if (friend.touch((IProjectile3D)monstre)) {
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
	public IArea3D getArea() {
		return terrain;
	}


	@Override
	public void setArea(IArea3D terrain) {
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
		boolean concerned = false;
		for (IMonstre friend : friends) {
			if (friend instanceof IController) {
				boolean test = ((IController)friend).key(eventKey,eventKeyState);
				if (test) {
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
	public synchronized void mouse(int eventButton, boolean eventButtonState, Point2D.Double position) {
		for (IMonstre friend : friends) {
			if (friend instanceof IController) {
				((IController)friend).mouse(eventButton,eventButtonState,position);
			}
		}
		junkLists();
	}

	

	@Override
	public synchronized boolean render(int calque) {
		if (calque == 0) {
//			terrain.render(0);
			return true;
		}
		if (calque>0 && calque-1 < friends.size()) {
			((IRenderer) friends.get(calque-1)).render(0);
			return true;
		}
		int n = calque-1-friends.size();
		if (n<monstres.size()) {
			if (monstres.get(n) instanceof IRenderer) {
				if (!finAlert) {
					// pause de 12 sec !
					((IRenderer) monstres.get(n)).render(0);
				}
			}
			return true;
		}
		return false;
	}


	@Override
	public synchronized Boolean finished() {
		if (DEMO) { return null;}
		if (!leaveDangerousMonsters(monstres) || !leaveDangerousMonsters(friends)) {
			currentAlertAlert.stop();
			finAlertBoum.stop();
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
		setArea((IArea3D)area);
	}

	@Override
		public ICamera getCamera() {
			if (missileLauncher3D!= null && !missileLauncher3D.isDeath()) {
				ICamera camera = missileLauncher3D.getCamera();
				if (camera!= null) return camera;
			}
			Point3D.Double position = new Point3D.Double(getArea().getZoneSize().x/2,getArea().getZoneSize().y/2,0);
			Point3D.Double orientation = new Point3D.Double(getArea().getZoneSize().x/2,getArea().getZoneSize().y/2,10);
			return new Camera(2.0/3.0,position, orientation);
		}

	@Override
	public int getMunitionsRestantes() {
		return munitions;
	}

	@Override
	public int getMaisonsRestantes() {
		int nbMaisons = 0;
		for (IMonstre friend : friends) {
			if (friend instanceof IHero) {
				nbMaisons++;
			}
		}
		return nbMaisons;
	}

	@Override
	public int getMaxMunitions() {
		return maxMunitions;
	}

	@Override
	public int getMaxMaisons() {
		return maxMaisons;
	}

	@Override
	public void consumeMunition() {
		if (munitions>0) munitions--;
	}

	@Override
	public int getScore() {
		return score;
	}

	@Override
	public void setScore(int score) {
		// rien
	}

	

}
