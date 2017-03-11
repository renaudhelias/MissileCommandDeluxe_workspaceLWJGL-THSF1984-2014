package net.stinfoservices.helias.renaud.tempest.tools.factory;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

import static org.lwjgl.opengl.GL11.*;

public class TextureLoader {
    private final int BYTES_PER_PIXEL = 4;//3 for RGB, 4 for RGBA
	private int width;
	private int height;
	private int idTex;
	
	public TextureLoader(BufferedImage image) {
		idTex = this.loadTexture(image);
	}
       private int loadTexture(BufferedImage image){
System.out.println("width :"+image.getWidth()+ "height"+ image.getHeight());
          int[] pixels = new int[image.getWidth() * image.getHeight()];
            image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

            ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB

            for(int y = 0; y < image.getHeight(); y++){
                for(int x = 0; x < image.getWidth(); x++){
                    int pixel = pixels[y * image.getWidth() + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                    buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                    buffer.put((byte) (pixel & 0xFF));               // Blue component
                    buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
//                    System.out.println("alpha : "+((pixel >> 24) & 0xFF));
                }
            }

            buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS

            // You now have a ByteBuffer filled with the color data of each pixel.
            // Now just create a texture ID and bind it. Then you can load it using 
            // whatever OpenGL method you want, for example:

          int textureID = glGenTextures(); //Generate texture ID
//            glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID
          glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID


            //Setup wrap mode
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

            //Setup texture scaling filtering
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            //Send texel data to OpenGL
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

            //Return the texture ID so we can bind it later again
            width=image.getWidth();
            height=image.getHeight();
          return textureID;
       }

       public static BufferedImage loadImage(URL loc)
       {
            try {
               return ImageIO.read(loc);
            } catch (IOException e) {
                //Error Handling Here
            }
           return null;
       }

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	public int getIdTex() {
		return idTex;
	}

	private static ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
            new int[] {8,8,8,8},
            true,
            false,
            ComponentColorModel.TRANSLUCENT,
            DataBuffer.TYPE_BYTE);

	private static ColorModel glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
            new int[] {8,8,8,0},
            false,
            false,
            ComponentColorModel.OPAQUE,
            DataBuffer.TYPE_BYTE);
	
	       
public  static  int texWidth = 2;
public  static  int texHeight = 2;

	public static ByteBuffer toByteBuffer(BufferedImage bufferedImage) {
		 ByteBuffer imageBuffer;
	        WritableRaster raster;
	        BufferedImage texImage;


		        // find the closest power of 2 for the width and height
		        // of the produced texture
		        while (texWidth < bufferedImage.getWidth()) {
		            texWidth *= 2;
		        }
		        while (texHeight < bufferedImage.getHeight()) {
		            texHeight *= 2;
		        }


		        // create a raster that can be used by OpenGL as a source
		        // for a texture
		        if (bufferedImage.getColorModel().hasAlpha()) {
		            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,4,null);
		            texImage = new BufferedImage(glAlphaColorModel,raster,false,new Hashtable());
		        } else {
		            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,3,null);
		            texImage = new BufferedImage(glColorModel,raster,false,new Hashtable());
		        }

		        // copy the source image into the produced image
		        Graphics g = texImage.getGraphics();
		        g.setColor(new Color(0f,0f,0f,0f));
		        g.fillRect(0,0,texWidth,texHeight);
		        if (texWidth != texHeight) throw new Error("texWidth != texHeight");
		        g.drawImage(bufferedImage,0,0,null);

		        // build a byte buffer from the temporary image
		        // that be used by OpenGL to produce a texture.
		        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();

		        // Exception in thread "main" java.lang.IllegalArgumentException: Number of remaining buffer elements is 196608, must be at least 262144. Because at most 262144 elements can be returned, a buffer with at least 262144 elements is required, regardless of actual returned element count
//		        imageBuffer = ByteBuffer.allocateDirect(data.length);
		        imageBuffer = BufferUtils.createByteBuffer(data.length);
		        imageBuffer.order(ByteOrder.nativeOrder());
		        imageBuffer.put(data, 0, data.length);
		        imageBuffer.flip();

		        return imageBuffer;

	}
}