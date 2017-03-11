package net.stinfoservices.helias.renaud.tempest.agent;

import java.awt.geom.Point2D.Double;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import net.stinfoservices.helias.renaud.tempest.IPosition2D;
import net.stinfoservices.helias.renaud.tempest.tools.NaNException;
import net.stinfoservices.helias.renaud.tempest.tools.Tools;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/**
 * C'est un carr� avec un rayon avec une image dedans.
 * @author sti
 *
 */
public abstract class CircleSpriteTextureRenderer implements ICircle2D, IPosition2D, IMove, IRenderer2D {

	private int idTex;
	private double width;
	private double height;
	private double nbSpritesX;
	private double nbSpritesY;
	private Integer choiceX = null;
	private Integer choiceY = null;

	public CircleSpriteTextureRenderer() {
		this(1);
	}
	public CircleSpriteTextureRenderer(int nbSprites) {
		this(nbSprites, 1);
	}
	public CircleSpriteTextureRenderer(int nbSpritesX, int nbSpritesY) {
		this(nbSpritesX,nbSpritesY,null,null);
	}
	public CircleSpriteTextureRenderer(int nbSpritesX, int nbSpritesY, Integer choiceX, Integer choiceY) {
		this.nbSpritesX = nbSpritesX;
		this.nbSpritesY = nbSpritesY;
		this.choiceX = choiceX;
		this.choiceY = choiceY;
	}
	@Override
	public void prepare(int idTex, int width, int height) {
		this.idTex =idTex;
		this.width=width;
		this.height=height;
	}

	@Override
	public boolean render(int calque) {
		
		GL11.glPushMatrix();
		
		
		GL11.glColor4f(1.0f,1.0f,1.0f,1.0f);

		double progression = getProgression();
		if (progression>= 1.0 || progression <0.0) {
			throw new Error("Mauvaise progression ("+progression+") sur " + this.getClass().getName());
		}
		

		Double middle = getPosition();
		
		GL11.glTranslated(middle.x,middle.y,0);

		double trusthScale=2*getRayon() ; //Math.sqrt(Math.pow(BUG*getRayon(),2)+Math.pow(BUG*getRayon(),2));
		double scale = Math.max(width/nbSpritesX, height/nbSpritesY);
		double scaleRatio = Math.sqrt((width/nbSpritesX)/(height/nbSpritesY));
		// zoom est selon le rep�re. donc des deux c�t�s � la fois, donc sqrt.
		GL11.glScaled(Math.sqrt(trusthScale/scale),Math.sqrt(trusthScale/scale),1);
		if (isOriented()) {
			Double orientationVector = getOrientation();
			if (orientationVector != null) {
				try {
					GL11.glRotated(Tools.angleDegreesZ(orientationVector), 0,0,1);
				} catch (NaNException e) {
					//nothing
				}
			}
		}

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,idTex);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		
		DoubleBuffer texCoordArray = BufferUtils.createDoubleBuffer(8);
		
		if (choiceX != null || choiceY != null) {
			progression = ((double) (choiceX+ choiceY * nbSpritesX)) / (double) (nbSpritesX*nbSpritesY);
		}
		if (progression == 1) progression = 0;
		progression *= (double)(nbSpritesX*nbSpritesY);
		
		double lineY = Math.floor(progression / (double)nbSpritesX);
		if (lineY == nbSpritesY) lineY=0;
		double lineX = Math.floor(progression- lineY*(double)nbSpritesX);
		if (lineX == nbSpritesX) lineY=0;
		
//		double currentSprite = Math.floor(progression*(double)nbSpritesX)/(double)nbSpritesX;
//		double endSprite= currentSprite+1.0/(double)nbSpritesX;
		
		texCoordArray.put(new double []{lineX/(double)nbSpritesX+ 1.0/(double)nbSpritesX, lineY/(double)nbSpritesY,
				lineX/(double)nbSpritesX,lineY/(double)nbSpritesY,
				lineX/(double)nbSpritesX+ 1.0/(double)nbSpritesX,lineY/(double)nbSpritesY+1.0/(double)nbSpritesY,
				lineX/(double)nbSpritesX,lineY/(double)nbSpritesY+1.0/(double)nbSpritesY});
		texCoordArray.flip();
		GL11.glTexCoordPointer(2, 0, texCoordArray);
		DoubleBuffer vertexArray = BufferUtils.createDoubleBuffer(8);
		vertexArray.put(new double []{getRayon()*scaleRatio,getRayon()/scaleRatio,
				-getRayon()*scaleRatio,getRayon()/scaleRatio,
				getRayon()*scaleRatio,-getRayon()/scaleRatio,
				-getRayon()*scaleRatio,-getRayon()/scaleRatio});
		vertexArray.flip();
		GL11.glVertexPointer(2,0, vertexArray);
		IntBuffer trianglesArray =  BufferUtils.createIntBuffer(6);
		trianglesArray.put(new int []{0,1,2,1,2,3});
		trianglesArray.flip();
		GL11.glDrawElements(GL11.GL_TRIANGLES,
		trianglesArray);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisable(GL11.GL_BLEND);
	    GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glPopMatrix();

		return true;
	}
	
	/**
	 * Par défaut, je suis fixe.
	 */
	@Override
	public double getProgression() {
		return 0;
	}
}
