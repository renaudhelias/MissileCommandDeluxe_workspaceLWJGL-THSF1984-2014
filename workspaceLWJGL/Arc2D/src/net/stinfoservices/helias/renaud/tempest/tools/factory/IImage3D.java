package net.stinfoservices.helias.renaud.tempest.tools.factory;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import net.stinfoservices.helias.renaud.tempest.agent.ICircle3D;
import net.stinfoservices.helias.renaud.tempest.agent.IRenderer;

public interface IImage3D extends IRenderer, ICircle3D {

	/**
	 * Nombre de figures
	 * @return
	 */
	public int getNbFigures();
	/**
	 * Texture coordinates
	 * @return
	 */
	public DoubleBuffer getGlTexCoordPointer(int i);
	
	/**
	 * Vertex
	 * @return
	 */
	public DoubleBuffer getGlVertexPointer(int i);
	
	/**
	 * Triangles
	 * @return
	 */
	public IntBuffer getGlDrawElements(String objet, String matiere);
	
	public double getWidth();
	public double getHeight();
	public double getDepth();
}
