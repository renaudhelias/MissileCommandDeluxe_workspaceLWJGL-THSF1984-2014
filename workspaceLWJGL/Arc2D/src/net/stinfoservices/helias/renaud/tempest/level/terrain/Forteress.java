package net.stinfoservices.helias.renaud.tempest.level.terrain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import net.stinfoservices.helias.renaud.tempest.TempestMain;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D;
import net.stinfoservices.helias.renaud.tempest.tools.Point3D.Double;
import net.stinfoservices.helias.renaud.tempest.tools.factory.TextureLoader;

public class Forteress implements IArea3D {

	private static final int WIDTH=20;
	private static final int HEIGHT=20;
	private static final int DEPTH=20;
	private static final int ZONE_WIDTH=512; //pixels
	private static final int ZONE_HEIGHT=512; //pixels
	private static final int ZONE_DEPTH=512; //pixels
	public static boolean AXE_Z_NEGATIVE=Puits.AXE_Z_NEGATIVE;
	
	private Map<Integer, Map<Integer, Map<Integer, ICase3D>>> cases = new HashMap<Integer, Map<Integer, Map<Integer, ICase3D>>>();
	private ByteBuffer image1;
	private ByteBuffer image2;
	private ByteBuffer image3;
	private ByteBuffer image4;
	private ByteBuffer image5;
	private ByteBuffer image6;
	private int textureID;
	
	public Forteress() {
		for (int i=0;i<WIDTH;i++) {
			cases.put(i, new HashMap<Integer, Map<Integer, ICase3D>>());
			for (int j=0;j<HEIGHT;j++) {
				cases.get(i).put(j, new HashMap<Integer, ICase3D>());
				for (int k=0;k<DEPTH;k++) {
					Point3D.Double positionZone = new Point3D.Double();
					positionZone.x=0.5*(double)ZONE_WIDTH/(double)WIDTH+(double)i*(double)ZONE_WIDTH/(double)WIDTH;
					positionZone.y=0.5*(double)ZONE_HEIGHT/(double)HEIGHT+(double)j*(double)ZONE_HEIGHT/(double)HEIGHT;
					if (AXE_Z_NEGATIVE) {
						positionZone.z=-(0.5*(double)ZONE_DEPTH/(double)DEPTH+(double)k*(double)ZONE_DEPTH/(double)DEPTH);
					} else {
						positionZone.z=0.5*(double)ZONE_DEPTH/(double)DEPTH+(double)k*(double)ZONE_DEPTH/(double)DEPTH;
					}
					cases.get(i).get(j).put(k, new Case3D(new Point3D.Integer(i,j,k),positionZone));
				}
			}
		}
		
		 BufferedImage image1 = TextureLoader.loadImage(TempestMain.class.getResource("skyboxRT.png"));//The path is inside the jar file
		 this.image1 = TextureLoader.toByteBuffer(image1);
		 BufferedImage image2 = TextureLoader.loadImage(TempestMain.class.getResource("skyboxLT.png"));//The path is inside the jar file
		 this.image2 = TextureLoader.toByteBuffer(image2);
		 BufferedImage image3 = TextureLoader.loadImage(TempestMain.class.getResource("skyboxUP.png"));//The path is inside the jar file
		 this.image3 = TextureLoader.toByteBuffer(image3);
		 BufferedImage image4 = TextureLoader.loadImage(TempestMain.class.getResource("skyboxDN.png"));//The path is inside the jar file
		 this.image4 = TextureLoader.toByteBuffer(image4);
		 BufferedImage image5 = TextureLoader.loadImage(TempestMain.class.getResource("skyboxBK.png"));//The path is inside the jar file
		 this.image5 = TextureLoader.toByteBuffer(image5);
		 BufferedImage image6 = TextureLoader.loadImage(TempestMain.class.getResource("skyboxFT.png"));//The path is inside the jar file
		 this.image6 = TextureLoader.toByteBuffer(image6);
		   
		 
			textureID = GL11.glGenTextures();
	        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID);
	        //GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	@Override
	public void setColor(Color color) {
	}

	@Override
	public boolean render(int calque) {
		// LWJGL skybox https://code.google.com/p/lwjgl-water-shader/source/browse/trunk/src/edu/fhooe/mtd360/watershader/objects/WaterPlane.java?r=32
		// C pure https://code.google.com/r/zaphosmatthewnicholls-opengl-superbible-java/source/browse/OpenGLSuperBible/src/openglsuperbible/Chapter7/Cubemap.java?spec=svn4a85fd5b2ace923e39ce98a9287fbfce61beb4dc&name=default&r=4a85fd5b2ace923e39ce98a9287fbfce61beb4dc
		// super bible opengl https://code.google.com/p/oglsuperbible5/source/browse/trunk/Src/Chapter07/
		// red book https://kenai.com/nonav/projects/jogl/sources/jogl-demos-git/content/src/redbook/src/glredbook1314/cubemap.java
		// api skyrender http://hub.jmonkeyengine.org/ https://github.com/jMonkeyEngine/jmonkeyengine
		// GL13.GL_TEXTURE_CUBE_MAP
		
		GL11.glPushMatrix();
double zoom =getZoneSize().x/TextureLoader.texWidth;
//		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP,id);
//		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP,GL11.GL_TEXTURE_MAG_FILTER,GL11.GL_LINEAR);
//		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP,GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_LINEAR);
//		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP,GL11.GL_TEXTURE_WRAP_S,GL11.GL_CLAMP_TO_EDGE);
//		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP,GL11.GL_TEXTURE_WRAP_T,GL11.GL_CLAMP_TO_EDGE);
//		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP,GL11.GL_TEXTURE_WRAP_R,GL11.GL_CLAMP_TO_EDGE);
GL11.glTranslatef(getZoneSize().x/2, getZoneSize().y/2, getZoneSize().z/2);
GL11.glScaled(zoom , zoom, zoom);
		
		//This is because the last argument to glTexImage2D should be a buffer containing the pixels for the texture. So you should allocate a 20*20 buffer using LWJGL BufferUtils, fill it with your texture data and then pass this buffer to the glTexImage2D function.
//GL11.glClearColor(0.0f,0.0f,0.0f,1.0f);
		int imageSize = TextureLoader.texWidth;
				

		
		GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL13.GL_NORMAL_MAP);
		GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL13.GL_NORMAL_MAP);
		GL11.glTexGeni(GL11.GL_R, GL11.GL_TEXTURE_GEN_MODE, GL13.GL_NORMAL_MAP);
		GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
		GL11.glEnable(GL11.GL_TEXTURE_GEN_T);
		GL11.glEnable(GL11.GL_TEXTURE_GEN_R);
		GL11.glEnable(GL13.GL_TEXTURE_CUBE_MAP);
		
		
		// a partir du livre...
		GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL11.GL_RGB, imageSize, imageSize, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, image1);
		GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL11.GL_RGB, imageSize, imageSize, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, image2);
		GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL11.GL_RGB, imageSize, imageSize, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, image3);
		GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL11.GL_RGB, imageSize, imageSize, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, image4);
		GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL11.GL_RGB, imageSize, imageSize, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, image5);
		GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL11.GL_RGB, imageSize, imageSize, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, image6);
		
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP,GL11.GL_TEXTURE_WRAP_S,GL11.GL_REPEAT);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP,GL11.GL_TEXTURE_WRAP_T,GL11.GL_REPEAT);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP,GL12.GL_TEXTURE_WRAP_R,GL11.GL_REPEAT);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP,GL11.GL_TEXTURE_MAG_FILTER,GL11.GL_NEAREST);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP,GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_NEAREST);
//		
		

//		GL11.glClear (GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);// | GL11.GL_STENCIL_BUFFER_BIT);
		
		GL11.glPopMatrix();
		return true;
	}

	@Override
	public Point3D.Integer getTerrainSize() {
		return new Point3D.Integer(WIDTH, HEIGHT, DEPTH);
	}

	@Override
	public ICase3D getCase(Point3D.Integer position) {
		return cases.get(position.x).get(position.y).get(position.z);
	}

	@Override
	public Point3D.Integer getZoneSize() {
		return new Point3D.Integer(ZONE_WIDTH, ZONE_HEIGHT, ZONE_DEPTH);
	}

	@Override
	public Double getCaseSize() {
		return new Point3D.Double((double)ZONE_WIDTH/(double)WIDTH, (double)ZONE_HEIGHT/(double)HEIGHT, (double)ZONE_DEPTH/(double)DEPTH);
	}

}
