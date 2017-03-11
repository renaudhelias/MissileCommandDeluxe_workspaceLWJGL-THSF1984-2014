package net.stinfoservices.helias.renaud.tempest.level;

import java.awt.geom.Point2D;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Mouse;

import net.stinfoservices.helias.renaud.tempest.Camera;
import net.stinfoservices.helias.renaud.tempest.ICamera;
import net.stinfoservices.helias.renaud.tempest.IScreen;
import net.stinfoservices.helias.renaud.tempest.agent.IRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.IMonstre;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.start.SimpleImage;
import net.stinfoservices.helias.renaud.tempest.level.properties.CalibrageProps;
import net.stinfoservices.helias.renaud.tempest.level.terrain.IArea;
import net.stinfoservices.helias.renaud.tempest.level.terrain.IArea2D;
import net.stinfoservices.helias.renaud.tempest.level.terrain.Killapede;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D;
import net.stinfoservices.helias.renaud.tempest.tools.Tools;
import net.stinfoservices.helias.renaud.tempest.tools.manager.IPrimitiveMouseListener;
import net.stinfoservices.helias.renaud.tempest.tools.manager.PrimitiveMouseManager;

/**
 * La position physique du centre du plateau est fixe. 
 * La largeur physique de l'�cran projet� est fixe mais calibrable entre deux valeurs
 * La hauteur physique de l'�cran projet� est fixe mais calibrable.
 * Le canon est align� sur le smiley au d�but du jeu (� plat), on r�gle le vid�o-proj selon
 * 
 * canon : bas, tirer, haut tirer.
 * plateau : max gauche tirer, max droite tirer
 * vitesse plateau : reperer gauche écran, tourner pour le viser physiquement, tirer (x5 fois)
 * centrer : welcome qui s'en va dès que centré
 * @author freemac
 *
 */
public class CalibrateLevel implements ILevel2D, IPrimitiveMouseListener {

	/**
	 * Permet de calibrer le zoom du vid�o projecteur avec deux points � hauteur du canon.
	 */
	public static final double LARGEUR_PORTE=CalibrageProps.getInstance().getLargeurPorte();
	public static final double LARGEUR_VP=CalibrageProps.getInstance().getLargeurVP();
	
	/**
	 * A partir du canon, le sommet.
	 */
	public static final double HAUTEUR_PORTE=CalibrageProps.getInstance().getHauteurPorte();
	
	/**
	 * 5m�tre / 2
	 */
	public static final double DISTANCE_PORTE=CalibrageProps.getInstance().getDistancePorte();
	
	enum states {CALIBRATE_ZOOM_VP,CALIBRATE_CANON_BAS,CALIBRATE_CANON_HAUT,CALIBRATE_CANON_GAUCHE,CALIBRATE_CANON_DROITE};
	
	states etat = states.CALIBRATE_ZOOM_VP;
	
	private List<IMonstre> monstres = new ArrayList<IMonstre>();
	
	
	private IScreen screen;

	private IArea2D area;
	



	private Boolean finished = null;
	private String canonMouseName;
	private Double canonMouseValue;
	private String plateauMouseName;
	private Double plateauMouseValue;
	private Point2D.Double  positionCanonBas;
	private Point2D.Double positionCanonHaut;
	private static CalibrateResult calibrate;

	public CalibrateLevel(IScreen screen) {
		this.screen = screen;
		setArea(new Killapede());
	}
	
	@Override
	public void init() {
		etat = states.CALIBRATE_ZOOM_VP;
		finished = null;
	}
	
	public static CalibrateResult getCalibrateResult() {
		return calibrate;
	}
	
	@Override
	public void generateMonstres() throws Exception {
		generateMonstres(0);
		if (finished == null) {
			PrimitiveMouseManager.getInstance().addListener(this);
		} else {
			throw new Exception("Seulement une calibration SVP");
		}

	}

	@Override
	public void generateMonstres(int phase) {
		Mouse.setGrabbed(false);
		monstres.clear();
		
		double sw=getScreen().getScreenWidth();
		double sh=getScreen().getScreenHeight();
		double zw=getArea().getZoneWidth();
		double zh=getArea().getZoneHeight();
		double zoom = Math.sqrt(sw*sw+sh*sh)/Math.sqrt(zw*zw+zh*zh); 

		// MissileLauncher3D
		double basSuzanne = getArea().getCase(10, 2).getPosition().y;
		
		switch(etat) {
			case CALIBRATE_ZOOM_VP:
				// afficher deux rond � distance LARGEUR_PORTE
				// LARGEUR_VP => sw
				// LARGEUR_PORTE => ?
				double largeurPorteScreen=LARGEUR_PORTE*sw/LARGEUR_VP;
				// zoom => screen
				// 1 => area
				double largeurPorteArea= largeurPorteScreen/zoom;
				
				
						
				
				Point2D.Double position1 = new Point2D.Double(-largeurPorteArea/2,basSuzanne);
				Point2D.Double position2 = new Point2D.Double(largeurPorteArea/2,basSuzanne);
				monstres.add(new SimpleImage(this,"Smiley.png", position1));
				monstres.add(new SimpleImage(this,"Smiley.png", position2));
				
				break;
			case CALIBRATE_CANON_BAS:
				// afficher rond en bas
				positionCanonBas = new Point2D.Double(0,basSuzanne);
				monstres.add(new SimpleImage(this,"milieu.png", positionCanonBas));
				break;
			case CALIBRATE_CANON_HAUT:
				// afficher rond en haut
				// LARGEUR_VP => sw
				// HAUTEUR_PORTE => ?
				double hauteurPorteScreen=HAUTEUR_PORTE*sw/LARGEUR_VP;
				// zoom => screen
				// 1 => area
				double hauteurPorteArea= hauteurPorteScreen/zoom;
				positionCanonHaut = new Point2D.Double(0,basSuzanne+hauteurPorteArea);
				monstres.add(new SimpleImage(this,"haut.png", positionCanonHaut));
				break;
			case CALIBRATE_CANON_GAUCHE:
				// afficher plateau droite
				Point2D.Double positionG = new Point2D.Double(0,basSuzanne);
				monstres.add(new SimpleImage(this,"gauche.png", positionG));
				break;
			case CALIBRATE_CANON_DROITE:
				// afficher physique gauche
				Point2D.Double positionD = new Point2D.Double(0,basSuzanne);
				monstres.add(new SimpleImage(this,"droite.png", positionD));
				break;
			default:
		}
	}

	@Override
	public void step() {
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
	public Boolean finished() {
		return finished;
	}

	@Override
	public void setArea(IArea area) {
		setArea((IArea2D)area);
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
	}

	@Override
	public boolean key(int eventKey, boolean eventKeyState) {
		return false;
	}

	@Override
	public void mouse(int eventButton, boolean eventButtonState, Point2D.Double position) {
	}

	@Override
	public boolean render(int calque) {
		for (IMonstre monstre : monstres) {
			if (monstre instanceof IRenderer) {
				((IRenderer) monstre).render(0);
			}
		}
		return false;
	}

	@Override
	public IArea2D getArea() {
		return area;
	}

	@Override
	public void setArea(IArea2D area) {
		this.area = area;
	}
	@Override
	public void mousePrimitiveClick(String mouseName, int i, int value) {
		System.out.println(mouseName+" click");
		if (value == 1) {
			if (etat==states.CALIBRATE_CANON_HAUT) {
				canonMouseName = getMaxMove();
				canonMouseValue = mouseMove.get(canonMouseName);
			} else if (etat==states.CALIBRATE_CANON_DROITE) {
				plateauMouseName = getMaxMove();
				plateauMouseValue = mouseMove.get(plateauMouseName);
			}
			if (etat != states.values()[states.values().length-1]) {
				etat = states.values()[etat.ordinal()+1];
				generateMonstres(etat.ordinal());
				System.out.println(etat+" ... waiting to action");
			} else {
				// thanks
				double sw=getScreen().getScreenWidth();
				double sh=getScreen().getScreenHeight();
				double zw=getArea().getZoneWidth();
				double zh=getArea().getZoneHeight();
				double zoom = Math.sqrt(sw*sw+sh*sh)/Math.sqrt(zw*zw+zh*zh); 

				// MissileLauncher3D
				double basSuzanne = getArea().getCase(10, 2).getPosition().y;
				// LARGEUR_VP => sw
				// LARGEUR_PORTE => ?
				double distancePorteScreen=DISTANCE_PORTE*sw/LARGEUR_VP;
				// zoom => screen
				// 1 => area
				double distancePorteArea= distancePorteScreen/zoom;
				Point3D.Double origine= new Point3D.Double(0,basSuzanne,-distancePorteArea);
				//cos(444/sqrt(444*444+(470-151)*(470-151)))*360/(2*PI) = 39°
				//o=(0,151,-444) bas=(0,151,0) haut=(0,470,0) 
				double angleCanon = (360.0/(Math.PI*2.0))*Tools.angleVectorVector(Tools.vector3D(origine, new Point3D.Double(positionCanonBas)), Tools.vector3D(origine, new Point3D.Double(positionCanonHaut)));
//				if (angleCanon>90) {
//					angleCanon = 180-angleCanon;
//				}
				System.out.println("Position canon : "+origine);
				System.out.println("Canon mouse "+angleCanon+"� : " +canonMouseName+" , value : "+canonMouseValue);
				System.out.println("Plateau mouse 180� : " +plateauMouseName+" , value : "+plateauMouseValue);
				calibrate = new CalibrateResult(origine,angleCanon, canonMouseName,canonMouseValue,180.0,plateauMouseName,plateauMouseValue);
				
				finished  = true;
			}
		}
	}
	
	Map<String,Double> mouseMove = new HashMap<String,Double>();
	
	private String getMaxMove() {
		String candidate = null;
		double max = 0;
		for (String key : mouseMove.keySet()) {
			if (candidate==null) {
				candidate=key;
				max =Math.abs(mouseMove.get(key));
			} else {
				if (Math.abs(mouseMove.get(key))>max) {
					candidate=key;
					max =Math.abs(mouseMove.get(key));
				}
			}
		}
		return candidate;
	}
	
	@Override
	public void mousePrimitiveMouve(String mouseName, int i, float value) {
		switch (etat) {
		case CALIBRATE_ZOOM_VP:
		case CALIBRATE_CANON_BAS:
		case CALIBRATE_CANON_GAUCHE:
			// nothing
			mouseMove.clear();
			break;
			case CALIBRATE_CANON_HAUT:
			case CALIBRATE_CANON_DROITE:
				if (mouseMove.containsKey(mouseName+"_"+i)) {
					Double oldValue = mouseMove.get(mouseName+"_"+i);
					mouseMove.put(mouseName+"_"+i,oldValue+value);
					System.out.println("adding "+value);
				} else {
					mouseMove.put(mouseName+"_"+i,(double)value);
					System.out.println("adding "+value);
				}
			default :
				break;	
		}
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
	}

	@Override
	public int getScore() {
		return -1;
	}

	@Override
	public void setScore(int score) {
	}

	

}
