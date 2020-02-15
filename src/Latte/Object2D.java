package Latte;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Object2D {
	private ArrayList<Vector2D> vecs = new ArrayList<Vector2D>();
	private ArrayList<Vector2D> vecsOrg = new ArrayList<Vector2D>();
	private Vector2D displacement = new Vector2D();
	private Vector2D pos = new Vector2D(0,0);
	private BufferedImage img=null;
	private double angle=0;
	
	public Vector2D getDisplacement() {
		return displacement;
	}
	public Vector2D getVector(int i) {
		return vecs.get(i);
	}
	public int getSize() {
		return vecs.size();
	}
	public void setPoint(int i,int x, int y) {
		vecs.set(i,new Vector2D(x,y));
		vecsOrg.set(i,new Vector2D(x,y));
	}
	public void updatePos(int x, int y) {
		displacement.setPos(x-pos.getX(),y-pos.getY());
		pos.setPos(x,y);
		for (int i=0; i<getSize();i++) {
			vecs.set(i,new Vector2D(vecsOrg.get(i).getX()+x,vecsOrg.get(i).getY()+y));
		}
	}
	
	public void addRect(int x, int y, int w, int h) {
		addPoint(x,y);
		addPoint(x+w,y);
		addPoint(x+w,y+h);
		addPoint(x,y+h);
	}
		
	public void addPoint(int x, int y) {
		vecs.add(new Vector2D(x,y));
		vecsOrg.add(new Vector2D(x,y));
	}
	
	private boolean checkQuad(Object2D obj) {
		double xMin=100000000; double yMin=100000000;
		double xMax=-100000000; double yMax=-100000000;
		double xMin2=100000000; double yMin2=100000000;
		double xMax2=-100000000; double yMax2=-100000000;
		xMin=getVector(0).getX();
		xMax=getVector(2).getX();
		yMin=getVector(0).getY();
		yMax=getVector(2).getY();
		xMin2=obj.getVector(0).getX();
		xMax2=obj.getVector(2).getX();
		yMin2=obj.getVector(0).getY();
		yMax2=obj.getVector(2).getY();
		if (xMin<xMax2&&xMax>xMin2&&yMin<yMax2&&yMax>yMin2)
			return true;
		return false;
	}
	protected Vector2D satDetection(Object2D obj) {
		if(vecs.size()==4&&obj.getSize()==4) {
			if(checkQuad(obj)) return obj.getDisplacement();
		}
		for (int a=0;a<vecs.size();a++) {
			int b = (a+1) % vecs.size();
			Vector2D axis = new Vector2D(-(getVector(b).getY()-getVector(a).getY()),
					getVector(b).getX()-getVector(a).getX());
			double min = 100000000; double max =-100000000;
			for (int p=0;p<getSize();p++) {
				double dot=getVector(p).dotProd(axis);
				min=Math.min(min,dot);
				max=Math.max(max,dot);
			}
			double min2 = 100000000; double max2 =-100000000;
			for (int p=0;p<obj.getSize();p++) {
				double dot=obj.getVector(p).dotProd(axis);
				min2=Math.min(min2,dot);
				max2=Math.max(max2,dot);
			}
			if(!(max2>=min && max>=min2))
				return null;
		}
		return obj.getDisplacement();
	}
	
	private Polygon getPoly() {
		Polygon poly=new Polygon();
		for(int i=0;i<vecs.size();i++) 
		poly.addPoint((int)getVector(i).getX(),(int)getVector(i).getY());
		return poly;
	}
	private Polygon getOrgPoly() {
		Polygon poly=new Polygon();
		for(int i=0;i<vecs.size();i++) 
		poly.addPoint((int)vecsOrg.get(i).getX(),(int)vecsOrg.get(i).getY());
		return poly;
	}
	
	public void fill(Graphics g) {
		Polygon poly=getPoly();
		g.fillPolygon(poly);
	}
	public void draw(Graphics g) {
		Polygon poly = getPoly();
		g.drawPolygon(poly);
	}
	public void setImage(String path) {
		try {
		    img = ImageIO.read(new File(path));
		} catch (IOException e) {
		}
	}
	public void drawImage(Graphics g) {
		if (img!=null) {
		Rectangle r = new Rectangle(0,0,img.getWidth(),img.getHeight());
		drawImage(g,r);
		}
	}
	public void drawImage(Graphics g,int x,int y,int w, int h) {
		Rectangle r = new Rectangle(x,y,w,h);
		drawImage(g,r);
	}
	private void drawImage(Graphics g, Rectangle clip) {
		Polygon poly = getOrgPoly();
		Rectangle rect = poly.getBounds();
		poly.translate(-rect.x,-rect.y);
		BufferedImage out = new BufferedImage(rect.width,rect.height, BufferedImage.TYPE_INT_ARGB );
		Graphics g2 = out.getGraphics();
		g2.setClip(poly);
		g2.drawImage( img, clip.x,clip.y,clip.width,clip.height, null );
		g.drawImage(out,(int)pos.getX()+rect.x,(int)pos.getY()+rect.y,null);
	}

	public void draw(Graphics g, BasicStroke stroke) {
		Polygon poly = getPoly();
		Graphics2D g2d=(Graphics2D)g;
		g2d.setStroke(stroke);
		g2d.drawPolygon(poly);
	}
}
