package net.stinfoservices.helias.renaud.tempest.tools.factory;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.stinfoservices.helias.renaud.tempest.tools.Point3D;
import net.stinfoservices.helias.renaud.tempest.tools.Tools;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Image3D implements IImage3D {

	private List<String> groupNames = new ArrayList<String>();
	private List<String> mtlNames = new ArrayList<String>();
	// par groupe, plusieurs couleurs
	private Map<String,List<String>> groupMTL = new HashMap<String,List<String>>();
	// par group, par no couleur
	private Map<String,Map<String,List<Point3D.Integer>>> triangle = new HashMap<String,Map<String,List<Point3D.Integer>>>();
	private List<List<Point3D.Double>> vertex = new ArrayList<List<Point3D.Double>>();
	private List<List<Point3D.Double>> normals = new ArrayList<List<Point3D.Double>>();
	
	private Map<String,Point3D.Double> mtlKa = new HashMap<String,Point3D.Double>();
	private Map<String,Point3D.Double> mtlKd = new HashMap<String,Point3D.Double>();
	private Map<String,Point3D.Double> mtlKs = new HashMap<String,Point3D.Double>();
//	private double rayon = 0.3;
	
	public Image3D() {
	}
	
	@Override
	public boolean render(int calque) {
		
//		GL11.glPushMatrix();
		
//		GL11.glColor4f(1.0f,1.0f,1.0f,1.0f);
		
//		if (isOriented()) {
//			Point3D.Double orientationVector = getOrientation();
//			if (orientationVector != null) {
//				try {
//					GL11.glRotated(Tools.angleDegreesZ(orientationVector), 1,1,1);
//				} catch (NaNException e) {
//					//nothing
//				}
//			}
//		}

		

		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		for (int i = 0; i < getNbFigures(); i++) {
			String objet = groupNames.get(i);
			
			DoubleBuffer normalArray = this.getGlNormalPointer(i);
			normalArray.flip();
			GL11.glNormalPointer(0, normalArray);

			
			DoubleBuffer vertexArray = this.getGlVertexPointer(i);
			vertexArray.flip();
			GL11.glVertexPointer(3,0, vertexArray);

			
//			if (groupMTL.containsKey(objet)) {
			for (String matiere : groupMTL.get(objet)) {
				if (mtlKa.containsKey(matiere)) {
					FloatBuffer ambianceLight = BufferUtils.createFloatBuffer(4);
					Point3D.Double ka=mtlKa.get(matiere);
					ambianceLight.put(new float []{(float)ka.x,(float)ka.y,(float)ka.z,1.0f});
					ambianceLight.flip();
					GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT, ambianceLight);
				}
				if (mtlKd.containsKey(matiere)) {
					FloatBuffer diffuseLight = BufferUtils.createFloatBuffer(4);
					Point3D.Double kd=mtlKd.get(matiere);
					diffuseLight.put(new float []{(float)kd.x,(float)kd.y,(float)kd.z,1.0f});
					diffuseLight.flip();
					GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_DIFFUSE, diffuseLight);
	//					GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, diffuseLight);
				}
				if (mtlKs.containsKey(matiere)) {
					FloatBuffer specularLight = BufferUtils.createFloatBuffer(4);
					Point3D.Double ks=mtlKs.get(matiere);
					specularLight.put(new float []{(float)ks.x,(float)ks.y,(float)ks.z,1.0f});
					specularLight.flip();
					GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_SPECULAR, specularLight);
				}
				
				// et plus encore !
				
				IntBuffer trianglesArray = this.getGlDrawElements(objet,matiere);
				trianglesArray.flip();
				GL11.glDrawElements(GL11.GL_TRIANGLES, trianglesArray);
				
			}
//			}
		}
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisable(GL11.GL_BLEND);

//		GL11.glPopMatrix();

		return false;
	}


	@Override
	public double getRayon() {
		throw new Error("Not used");
//		return rayon ;
	}
	
	public void setRayon(double rayon) {
		throw new Error("Not used");
//		this.rayon = rayon;
	}

	@Override
	public Point3D.Double getPosition() {
		return new Point3D.Double();
	}

	@Override
	public boolean isOriented() {
		return true;
	}

	@Override
	public Point3D.Double getOrientation() {
		return new Point3D.Double(0,0,1);
	}
	
	@Override
	public int getNbFigures() {
		return groupNames.size();
	}

	@Override
	public DoubleBuffer getGlTexCoordPointer(int i) {
		throw new Error("Not implemented yet :p");
	}

	@Override
	public DoubleBuffer getGlVertexPointer(int i) {
		DoubleBuffer vertexArray = BufferUtils.createDoubleBuffer(vertex.get(i).size()*3);
		double [] buff =new double [vertex.get(i).size()*3];
		for (int v = 0;v<vertex.get(i).size();v++) {
			buff[v*3+0]=vertex.get(i).get(v).x;
			buff[v*3+1]=vertex.get(i).get(v).y;
			buff[v*3+2]=vertex.get(i).get(v).z;
		}
		vertexArray.put(buff);
		return vertexArray;
	}
	

	private DoubleBuffer getGlNormalPointer(int i) {
		DoubleBuffer normalArray = BufferUtils.createDoubleBuffer(normals.get(i).size()*3);
		double [] buff =new double [normals.get(i).size()*3];
		for (int v = 0;v<normals.get(i).size();v++) {
			buff[v*3+0]=normals.get(i).get(v).x;
			buff[v*3+1]=normals.get(i).get(v).y;
			buff[v*3+2]=normals.get(i).get(v).z;
		}
		normalArray.put(buff);
		return normalArray;
	}

	@Override
	public IntBuffer getGlDrawElements(String objet, String matiere) {
		IntBuffer triangleArray = BufferUtils.createIntBuffer(triangle.get(objet).get(matiere).size()*3);
		int [] buff =new int [triangle.get(objet).get(matiere).size()*3];
		for (int v = 0;v<triangle.get(objet).get(matiere).size();v++) {
			buff[v*3+0]=triangle.get(objet).get(matiere).get(v).x;
			buff[v*3+1]=triangle.get(objet).get(matiere).get(v).y;
			buff[v*3+2]=triangle.get(objet).get(matiere).get(v).z;
		}
		triangleArray.put(buff);
		return triangleArray;
	}

	@Override
	public double getWidth() {
		return 1.0;
	}

	@Override
	public double getHeight() {
		return 1.0;
	}

	@Override
	public double getDepth() {
		return 1.0;
	}

	public void welcomeObjet(String group) {
		groupNames.add(group);
		groupMTL.put(groupNames.get(groupNames.size()-1), new ArrayList<String>());
		triangle.put(groupNames.get(groupNames.size()-1),new HashMap<String,List<Point3D.Integer>>());
		vertex.add(new ArrayList<Point3D.Double>());
	}

	public void addTriangle(int a, int b, int c) {
		a--;
		b--;
		c--;
		int offset=0;
		int i = triangle.size()-1;
		for (int v=0;v<i;v++) {
			offset+=vertex.get(v).size();
		}
		if (a-offset<0 || b-offset<0 ||c-offset <0) {
			throw new Error("inconsistance (i="+i+")");
		}
		if (a-offset>vertex.get(i).size()) {
			throw new Error("inconsistance (i="+i+" : "+(a-offset)+">"+vertex.get(i).size()+")");
		}
		if (b-offset>vertex.get(i).size()) {
			throw new Error("inconsistance (i="+i+" : "+(b-offset)+">"+vertex.get(i).size()+")");
		}
		if (c-offset>vertex.get(i).size()) {
			throw new Error("inconsistance (i="+i+" : "+(c-offset)+">"+vertex.get(i).size()+")");
		}
		String objet = groupNames.get(groupNames.size()-1);
		String matiere =groupMTL.get(objet).get(groupMTL.get(objet).size()-1);
		triangle.get(objet).get(matiere).add(new Point3D.Integer(a-offset,b-offset,c-offset));
	}

	public void addVertex(double x, double y,
			double z) {
		vertex.get(vertex.size()-1).add(new Point3D.Double(x,y,z));
	}

	public void normalize() {
		System.out.println("finish him !");
		Double minX=null;
		Double minY=null;
		Double minZ=null;
		Double maxX=null;
		Double maxY=null;
		Double maxZ=null;
		
		for (int i=0;i<groupNames.size();i++) {
			for (int v=0;v<vertex.get(i).size();v++) {
				if (minX == null) {
					minX = vertex.get(i).get(v).x;
				} else {
					minX = Math.min(minX,vertex.get(i).get(v).x);
				}
				if (minY == null) {
					minY = vertex.get(i).get(v).y;
				} else {
					minY = Math.min(minY,vertex.get(i).get(v).y);
				}
				if (minZ == null) {
					minZ = vertex.get(i).get(v).z;
				} else {
					minZ = Math.min(minZ,vertex.get(i).get(v).z);
				}
				
				if (maxX == null) {
					maxX = vertex.get(i).get(v).x;
				} else {
					maxX = Math.max(maxX,vertex.get(i).get(v).x);
				}
				if (maxY == null) {
					maxY = vertex.get(i).get(v).y;
				} else {
					maxY = Math.max(maxY,vertex.get(i).get(v).y);
				}
				if (maxZ == null) {
					maxZ = vertex.get(i).get(v).z;
				} else {
					maxZ = Math.max(maxZ,vertex.get(i).get(v).z);
				}
			}
		}
		double width = maxX-minX;
		double height = maxY-minY;
		double depth = maxZ-minZ;
		
		double centerX = minX+width/2;
		double centerY = minY+height/2;
		double centerZ = minZ+depth/2;
		System.out.println("center :"+centerX+" "+ centerY+" "+ centerZ);
		for (int i=0;i<groupNames.size();i++) {
			for (int v=0;v<vertex.get(i).size();v++) {
				vertex.get(i).get(v).x-=centerX;
				vertex.get(i).get(v).y-=centerY;
				vertex.get(i).get(v).z-=centerZ;
//				System.out.println("v :"+vertex.get(i).get(v));		
			}
		}
		// zoom est selon le rep�re. donc des deux c�t�s � la fois, donc sqrt.
		// Erratum /2 car normalize sinon en (-1,1;-1,1;-1,1)
		double maxSize = Math.max(Math.max(width,height), depth)/2;
//		maxSize = Math.sqrt(maxSize);
		for (int i=0;i<groupNames.size();i++) {
			for (int v=0;v<vertex.get(i).size();v++) {
				vertex.get(i).get(v).x/=maxSize;
				vertex.get(i).get(v).y/=maxSize;
				vertex.get(i).get(v).z/=maxSize;
				System.out.println("v :"+vertex.get(i).get(v));	
			}
		}
		
		for (int i =0;i<groupNames.size();i++) {
			explodeNormals(i);
		}
		
	}

	private void explodeNormals(int i) {
		List<Point3D.Double> v= new ArrayList<Point3D.Double>();
		List<Point3D.Double> n= new ArrayList<Point3D.Double>();
		List<Point3D.Integer> t= new ArrayList<Point3D.Integer>();
		int cursor = 0;
		String objet = groupNames.get(i);
		for (String matiere : groupMTL.get(objet)) {
			for (Point3D.Integer t3 : triangle.get(objet).get(matiere)) {
				Point3D.Double p1 = vertex.get(i).get(t3.x);
				Point3D.Double p2 = vertex.get(i).get(t3.y);
				Point3D.Double p3 = vertex.get(i).get(t3.z);
				Point3D.Double normal = Tools.normalFromTriangle(p1, p2, p3);
				v.add(p1);
				n.add(normal);
				v.add(p2);
				n.add(normal);
				v.add(p3);
				n.add(normal);
				t.add(new Point3D.Integer(cursor+0,cursor+1,cursor+2));
				cursor+=3;
			}
			triangle.get(objet).get(matiere).clear();
			triangle.get(objet).get(matiere).addAll(t);
		}
		vertex.get(i).clear();
		vertex.get(i).addAll(v);
		normals.add(new ArrayList<Point3D.Double>());
		normals.get(i).addAll(n);
	}

	public void addKa(double r, double g, double b) {
		mtlKa.put(mtlNames.get(mtlNames.size()-1), new Point3D.Double(r,g,b));
	}
	public void addKd(double r, double g, double b) {
		mtlKd.put(mtlNames.get(mtlNames.size()-1), new Point3D.Double(r,g,b));
	}
	public void addKs(double r, double g, double b) {
		mtlKs.put(mtlNames.get(mtlNames.size()-1), new Point3D.Double(r,g,b));
	}

	public void welcomeUseMTL(String matiere) {
		String objet =groupNames.get(groupNames.size()-1); 
		groupMTL.get(objet).add(matiere);
		triangle.get(objet).put(matiere, new ArrayList<Point3D.Integer>());
	}

	public void welcomeNewMTL(String mtlName) {
		mtlNames.add(mtlName);
	}


}
