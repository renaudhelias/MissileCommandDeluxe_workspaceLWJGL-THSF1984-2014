package net.stinfoservices.helias.renaud.tempest.agent.monstre.start;

import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;

import net.stinfoservices.helias.renaud.tempest.TempestMain;
import net.stinfoservices.helias.renaud.tempest.agent.CircleSpriteTextureRenderer;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle2D;
import net.stinfoservices.helias.renaud.tempest.agent.ICircle3D;
import net.stinfoservices.helias.renaud.tempest.agent.IMarcheOuCreve;
import net.stinfoservices.helias.renaud.tempest.agent.monstre.IMonstre;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile2D;
import net.stinfoservices.helias.renaud.tempest.agent.projectile.IProjectile3D;
import net.stinfoservices.helias.renaud.tempest.level.ILevel2D;
import net.stinfoservices.helias.renaud.tempest.tools.factory.TextureLoader;

public class SimpleImage extends CircleSpriteTextureRenderer implements IMonstre {

	private TextureLoader tex2;
	private Double position;
	private ILevel2D level;

	public SimpleImage(ILevel2D level, String fileName) {
		this(level,fileName,null);
	}
	public SimpleImage(ILevel2D level, String fileName, Double position) {
	    this.level = level;
		this.position = position;
	    BufferedImage image2 = TextureLoader.loadImage(TempestMain.class.getResource(fileName));
	    tex2 = new TextureLoader(image2);
	    super.prepare(tex2.getIdTex(), tex2.getWidth(), tex2.getHeight());
	}
	
	@Override
	public double getRayon() {
		return Math.max(tex2.getWidth()/2, tex2.getHeight()/2);
	}

	@Override
	public Double getPosition() {
		if (position==null) {
			return new Double(0, level.getArea().getZoneHeight()/2);
		}
		return position;
	}

	@Override
	public boolean isOriented() {
		return false;
	}

	@Override
	public Double getOrientation() {
		return null;
	}

	@Override
	public void step() {
	}

	@Override
	public boolean isDeath() {
		return false;
	}

	@Override
	public void kill(IMarcheOuCreve killer) {
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
	public void dispose() {
	}

	@Override
	public boolean touch(IProjectile3D arrow) {
		return false;
	}

	@Override
	public boolean touch(ICircle3D canon) {
		return false;
	}
	public void setPosition(Double position) {
		this.position = position;
	}

}
