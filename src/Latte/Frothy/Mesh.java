package Latte.Frothy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Latte.IO;
import Latte.Window;
import Latte.Flat.Vector;

public class Mesh {
	private double translate=0;
	private BufferedImage img=null;
	private ArrayList<Tri> tris = new ArrayList<Tri>();
	public void addCube() {
		//South
		tris.add(new Tri(new Vector3(),new Vector3(0,1,0),new Vector3(1,1,0),new Vector(0,1),new Vector(),new Vector(1,0)));
		tris.add(new Tri(new Vector3(),new Vector3(1,1,0),new Vector3(1,0,0),new Vector(0,1),new Vector(1,0),new Vector(1,1)));
		//East
		tris.add(new Tri(new Vector3(1,0,0),new Vector3(1,1,0),new Vector3(1,1,1),new Vector(0,1),new Vector(),new Vector(1,0)));
		tris.add(new Tri(new Vector3(1,0,0),new Vector3(1,1,1),new Vector3(1,0,1),new Vector(0,1),new Vector(1,0),new Vector(1,1)));
		//North
		tris.add(new Tri(new Vector3(1,0,1),new Vector3(1,1,1),new Vector3(0,1,1),new Vector(0,1),new Vector(),new Vector(1,0)));
		tris.add(new Tri(new Vector3(1,0,1),new Vector3(0,1,1),new Vector3(0,0,1),new Vector(0,1),new Vector(1,0),new Vector(1,1)));
		//West
		tris.add(new Tri(new Vector3(0,0,1),new Vector3(0,1,1),new Vector3(0,1,0),new Vector(0,1),new Vector(),new Vector(1,0)));
		tris.add(new Tri(new Vector3(0,0,1),new Vector3(0,1,0),new Vector3(),new Vector(0,1),new Vector(1,0),new Vector(1,1)));
		//Top
		tris.add(new Tri(new Vector3(0,1,0),new Vector3(0,1,1),new Vector3(1,1,1),new Vector(0,1),new Vector(),new Vector(1,0)));
		tris.add(new Tri(new Vector3(0,1,0),new Vector3(1,1,1),new Vector3(1,1,0),new Vector(0,1),new Vector(1,0),new Vector(1,1)));
		//Bottom
		tris.add(new Tri(new Vector3(1,0,1),new Vector3(0,0,1),new Vector3(),new Vector(0,1),new Vector(),new Vector(1,0)));
		tris.add(new Tri(new Vector3(1,0,1),new Vector3(),new Vector3(1,0,0),new Vector(0,1),new Vector(1,0),new Vector(1,1)));
	}
	public void loadImg(String path) {
		try {
		    img = ImageIO.read(new File(path));
		} catch (IOException e) {}
	}
	public void addOBJ(String objPath, String texPath) {
		if(!texPath.isEmpty()) loadImg(texPath);
		ArrayList<Vector3> vecs=new ArrayList<Vector3>();
		ArrayList<Vector> texs=new ArrayList<Vector>();
		IO file = new IO();
		file.readPlain(objPath);
		while(file.hasNext()) {
		String line = file.loadNext();
		if(!line.isEmpty()) {
			if(line.charAt(0)=='v') {
				if(line.charAt(1)=='t') {
					line=line.replace("vt ","");
					texs.add(new Vector(1,1).sub(Vector.load(line)));
				}
				else {
					line=line.replace("v ","");
					vecs.add(Vector3.load(line));
				}
			}
			if(line.charAt(0)=='f') {
				String group[]=line.replace("f ","").split(" ");
				int arr[] = new int[12];
				for(int i=0;i<group.length;i++) {
					String split[]=group[i].split("/");
					arr[(i*3)]=Integer.parseInt(split[0])-1;
					if(split.length>1)
					arr[(i*3)+1]=Integer.parseInt(split[1])-1;
					if(split.length>2)
					arr[(i*3)+2]=Integer.parseInt(split[2])-1;
				}
				if(!texs.isEmpty())
				tris.add(new Tri(vecs.get(arr[0]), vecs.get(arr[3]), vecs.get(arr[6]),
						texs.get(arr[1]),texs.get(arr[4]),texs.get(arr[7])));
				else
					tris.add(new Tri(vecs.get(arr[0]), vecs.get(arr[3]), vecs.get(arr[6])));
			}
			
		}
		}
	}
	
	public void draw(Graphics g, double delta) {
		ArrayList<Tri> world= transform(tris,delta);
		ArrayList<Tri> projs= Camera3.project(world);
		for (int i=0; i<projs.size();i++) {
			ArrayList<Tri> tempTris = new ArrayList<Tri>();
			tempTris.add(projs.get(i));
			tempTris = clipProjection(tempTris);
			for(Tri tri: tempTris) g.drawPolygon(getPoly(tri));
		}
	}
	public void fill(Graphics g, double delta) {
		ArrayList<Tri> world= transform(tris,delta);
		ArrayList<Tri> projs= Camera3.project(world);
		Color c=g.getColor();
		Camera3.clear();
		for (int i=0; i<projs.size();i++) {
			ArrayList<Tri> tempTris = new ArrayList<Tri>();
			tempTris.add(projs.get(i));
			tempTris = clipProjection(tempTris);
			for(Tri tri: tempTris) {
				int red= (int)(c.getRed()*tri.color);
				int green= (int)(c.getGreen()*tri.color);
				int blue=  (int)(c.getBlue()*tri.color);			
				g.setColor(new Color(red,green,blue));
				//g.fillPolygon(getPoly(tri));
				tri.TexturedTriangle(g,img);
				//g.setColor(Color.red);
				//g.drawPolygon(getPoly(tri));
			}
		}
	}

	private ArrayList<Tri> clipProjection(ArrayList<Tri> tempTris) {
		int nNewTriangles = 1;

		for (int p = 0; p < 4; p++)
		{
			ArrayList<Tri> clipped=new ArrayList<Tri>();
			while (nNewTriangles > 0)
			{
				Tri test = tempTris.get(0);
				tempTris.remove(0);
				nNewTriangles--;

				switch (p)
				{
				case 0:	clipped = test.clipping(new Vector3(), new Vector3(0,1,0)); break;
				case 1:	clipped = test.clipping(new Vector3(0, Window.getHeight() - 1,0),new Vector3(0,-1,0)); break;
				case 2:	clipped = test.clipping(new Vector3(), new Vector3(1,0,0)); break;
				case 3:	clipped = test.clipping(new Vector3(Window.getWidth() - 1, 0,0), new Vector3(-1,0,0)); break;
				}
				for (Tri clippedTri: clipped)
					tempTris.add(clippedTri);
			}
			nNewTriangles = tempTris.size();
		}
		return tempTris;
	}

	private ArrayList<Tri> transform(ArrayList<Tri> tris, double delta) {
		//Fix this so it works.
		ArrayList<Tri> newTris = new ArrayList<Tri>();
		//translate+=delta*0.02;
		Matrix rotX=Matrix.rotateX(translate);
		Matrix rotZ=Matrix.rotateZ(translate*0.5);
		Matrix world = Matrix.identity();
		world=rotZ.multi(rotX);
		for(int t=0; t<tris.size();t++) {
			Tri transformed = new Tri();
			for(int i =0;i<3;i++) {
				transformed.p[i] = world.multiVec(tris.get(t).p[i]);
				transformed.p[i] =tris.get(t).p[i].add(new Vector3(0,0,3));
				transformed.t[i]=tris.get(t).t[i];
			}
			newTris.add(transformed);
		}
		return newTris;
	}
	private Polygon getPoly(Tri tri) {
		int xArr[] = new int[3];
		int yArr[] = new int[3];
		for(int i =0;i<3;i++) {
		xArr[i]=(int)tri.p[i].getX();
		yArr[i]=(int)tri.p[i].getY();
		}
		return new Polygon(xArr,yArr,3);
	}

}