package net.stinfoservices.helias.renaud.tempest.agent.friend.attaquant;

import java.awt.Color;

import org.newdawn.slick.openal.Audio;

import net.stinfoservices.helias.renaud.tempest.Camera;
import net.stinfoservices.helias.renaud.tempest.ICamera;
import net.stinfoservices.helias.renaud.tempest.agent.Circle3DRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle2D;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle3D;
import net.stinfoservices.helias.renaud.tempest.agent.IMarcheOuCreve;
import net.stinfoservices.helias.renaud.tempest.agent.ISoundRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.IMonstre;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile2D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile3D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.factory.IMissileFactory;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.special.Marqueur3D;
import net.stinfoservices.helias.renaud.tempest.level.CalibrateLevel;
import net.stinfoservices.helias.renaud.tempest.level.CalibrateResult;
import net.stinfoservices.helias.renaud.tempest.level.ILevel3D;
import net.stinfoservices.helias.renaud.tempest.level.Level2;
import net.stinfoservices.helias.renaud.tempest.level.properties.CalibrageProps;
import net.stinfoservices.helias.renaud.tempest.level.terrain.Puits;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D.Double;
import net.stinfoservices.helias.renaud.tempest.tools.Rotate3D;
import net.stinfoservices.helias.renaud.tempest.tools.Tools;
import net.stinfoservices.helias.renaud.tempest.tools.manager.IPrimitiveMouseListener;
import net.stinfoservices.helias.renaud.tempest.tools.manager.PrimitiveMouseManager;


// defense terre/air
public class MissileLauncher3D extends Circle3DRenderer implements IMonstre, ISoundRenderer, IPrimitiveMouseListener {
	private final static double POSITIVE_ZERO=0.001;

//	private final static double CALIBRAGE=120;

	private ILevel3D level;
//	private int x;
//	private int y;
//	private int z;
	private boolean death;
	private IMissileFactory factory;

	private double anglePlateau = POSITIVE_ZERO;
	private double angleCanon = POSITIVE_ZERO;

	private CalibrateResult calibrateResult;

	private double OFFSET_PLATEAU_DEBUG=0;//Math.PI/3;

	private Circle3DRenderer curseur;
	
	public MissileLauncher3D(ILevel3D level, IMissileFactory factory) {
		super(true);
		
		calibrateResult = CalibrateLevel.getCalibrateResult();
		PrimitiveMouseManager.getInstance().addListener(this);
		
		this.level = level;
		this.factory = factory;
//		this.x = x;
//		this.y = y;
//		this.z = z;
		setColor(Color.RED);
//		prepare("suzanne");
		prepare("missileSuzanne");
		
		curseur = new Circle3DRenderer(true) {

			@Override
			public double getRayon() {
				return Marqueur3D.RAYON;
			}

			@Override
			public boolean isOriented() {
				return true;
			}

			@Override
			public Double getOrientation() {
				return MissileLauncher3D.this.getOrientation();
			}

			@Override
			public Double getPosition() {
				
				
				double longueur=MissileLauncher3D.this.autoComputeMissileDistance();
				Point3D.Double destination = new Point3D.Double(getOrientation());
				destination.x*=longueur;
				destination.y*=longueur;
				destination.z*=longueur;
				destination.x+=MissileLauncher3D.this.getPosition().x;
				destination.y+=MissileLauncher3D.this.getPosition().y;
				destination.z+=MissileLauncher3D.this.getPosition().z;
				
				return destination;
			}
			
		};
		
		curseur.prepare("curseur3");
		
	}
	
	protected double autoComputeMissileDistance() {
		// selon angleCanon
		
		// proche, moyen, loin
		double i=CalibrageProps.getInstance().getAutoComputeMissileDistance(); // avec 4 probl�me carr� bords proche
		double diametreCase = 2*Tools.deduceRayon(MissileLauncher3D.this.level);
		double distance = diametreCase*i;
		double trueDistance=distance/Math.cos(angleCanon);
		return trueDistance;
	}

	@Override
	public double getRayon() {
		return Tools.deduceRayon(level)*CalibrageProps.getInstance().getRatioTireur();
	}

	@Override
	public boolean isOriented() {
		return true;
	}

	@Override
	public Point3D.Double getOrientation() {
		// orientation du canon
//		Double orientation = new Point3D.Double();
//		orientation.x = Math.sin(anglePlateau);
//		orientation.y=Math.tan(-angleCanon);
//		orientation.z = Math.cos(anglePlateau);
		
		Point3D.Double orientation = new Point3D.Double(0,0,1);
		orientation = new Rotate3D(new Point3D.Double(0,1,0),anglePlateau+OFFSET_PLATEAU_DEBUG).projeter(orientation);
		Point3D.Double axeXprojeted=new Point3D.Double(1,0,0);
		axeXprojeted=new Rotate3D(new Point3D.Double(0,1,0),anglePlateau+OFFSET_PLATEAU_DEBUG).projeter(axeXprojeted);
		orientation = new Rotate3D(axeXprojeted,angleCanon).projeter(orientation);
		
		return Tools.normalize(orientation);
	}

	@Override
	public double getProgression() {
		return 0;
	}
	
	@Override
	public Point3D.Double getPosition() {
		
		return new Point3D.Double(level.getArea().getZoneSize().x/2,0,level.getArea().getZoneSize().z/2.0);
		
//		return level.getArea().getCase(new Point3D.Integer(x, y, z)).getPosition();
	}

		

	@Override
	public void step() {
	}

	@Override
	public boolean isDeath() {
		return death;
	}

	@Override
	public void kill(IMarcheOuCreve killer) {
//		death = true;
	}

	@Override
	public boolean touch(IProjectile2D arrow) {
		return false;
	}

	@Override
	public boolean touch(ICircle2D canon) {
		return false;
	}
	
	@Override
	public void prepare(Audio kill, Audio action) {
	}

	@Override
	public void dispose() {
		kill(null);
	}

	@Override
	public boolean touch(IProjectile3D arrow) {
		return false;
	}

	@Override
	public boolean touch(ICircle3D canon) {
		return false;
	}

	public ICamera getCamera() {
		//Légèrement au dessus de suzanne.
		Point3D.Double position = new Point3D.Double(getPosition());
		
		// ici seul le plateau tourne la camera !
		Point3D.Double orientation = new Point3D.Double();
		orientation.x = Math.sin(anglePlateau);
		orientation.y = 0;
		orientation.z = Math.cos(anglePlateau);
		// zoom relatif à la taille de l'écran et à la taille du jeu.
		double sw=level.getScreen().getScreenWidth();
		double sh=level.getScreen().getScreenHeight();
		double zh=level.getArea().getZoneSize().y;
		double zoom = sh/zh;////Math.sqrt(sw*sw+sh*sh)/Math.sqrt(zw*zw+zh*zh);
		
		// d�pend de : orientation, rayon.
		// rayon => DISTANCE_PORTE
		// zw    => LARGEUR_VP
		
		double zw= (1/zoom)*sw;
		double rayon = (zw*CalibrateLevel.DISTANCE_PORTE)/CalibrateLevel.LARGEUR_VP;
		Point3D.Double positionCamera = new Point3D.Double(orientation);
		positionCamera.x*=rayon;
		positionCamera.y*=rayon;
		positionCamera.z*=rayon;
		positionCamera.x+=position.x;
		positionCamera.y+=position.y;
		positionCamera.z+=position.z;
		
		positionCamera.y-=Puits.ZONE_HEIGHT/20;
		
		return new Camera(zoom,positionCamera, orientation);
	}

	@Override
	public boolean render(int calque) {
//		GL11.glPushMatrix();
		boolean retour = super.render(calque);
		
		retour = curseur.render(calque);
		
//		GL11.glPopMatrix();
		return retour;
	}

	@Override
	public void mousePrimitiveClick(String mouseName, int i, int value) {
		if (value == 1) {
			if (level.getMunitionsRestantes()>0 || Level2.DEMO) {
				level.consumeMunition();
				// proche, moyen, loin
				double longueur=autoComputeMissileDistance();
				Point3D.Double destination = new Point3D.Double(getOrientation());
				destination.x*=longueur;
				destination.y*=longueur;
				destination.z*=longueur;
				destination.x+=getPosition().x;
				destination.y+=getPosition().y;
				destination.z+=getPosition().z;
				
//				JOptionPane.showMessageDialog(null, new Position3DAdapter(this));
//				JOptionPane.showMessageDialog(null, new Position3DAdapter(destination));
				
				factory.launcheMissile(new Position3DAdapter(this), new Position3DAdapter(destination), false,CalibrageProps.getInstance().getForceMissileTir(),0);
//				factory.launcheMissile(new Position3DAdapter(new Point3D.Double(257,1,257)), new Position3DAdapter(new Point3D.Double(303,129,104)), true);
			}
		}
	}

	@Override
	public void mousePrimitiveMouve(String mouseName, int i, float value) {
		String fullName= mouseName+"_"+i;
		if (fullName.equals(calibrateResult.getPlateauMouseName())) {
			// X
			
			double angle = value*Math.toRadians(calibrateResult.getAnglePlateau())/calibrateResult.getPlateauMouseValue();
			
			// WARNING CALIBRATE DONE WITH TRUE PLATFORM
			anglePlateau-=angle;
			while (anglePlateau>Math.PI*2) {
				anglePlateau-=Math.PI*2;
			}
			while (anglePlateau<0) {
				anglePlateau+=Math.PI*2;
			}
//			System.out.println(calibrateResult);
//			System.out.println("anglePlateau="+anglePlateau);
		}
		if (fullName.equals(calibrateResult.getCanonMouseName())) {
			// Y
			//division par 4 car ça tourne 4 fois moins que le plateau ! 
			double angle = value*Math.toRadians(calibrateResult.getAngleCanon())/calibrateResult.getCanonMouseValue();
			
			angleCanon-=angle;
			while (angleCanon>Math.PI*2) {
				angleCanon-=Math.PI*2;
			}
			while (angleCanon<0) {
				angleCanon+=Math.PI*2;
			}
			
//			System.out.println("angleCanon="+angleCanon);
		}
	}

}
