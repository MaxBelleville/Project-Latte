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
	private String path="";
	private double angle=0;
	public Vector2D getPos() {
		return pos;
	}
	public Vector2D getDisplacement() {
		return displacement;
	}
	public Vector2D getPoint(int i) {
		return vecs.get(i);
	}
	public Vector2D getOrgPoint(int i) {
		return vecsOrg.get(i);
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
		Vector2D camPos = Camera2D.getPos();
		for (int i=0; i<getSize();i++) {
			vecs.set(i,new Vector2D(vecsOrg.get(i).getX()+x+camPos.getX(),vecsOrg.get(i).getY()+y+camPos.getY()));
		}
	}
	public Object2D addOval(int x,int y,double width,double height) {
		for(int i=0; i<360;i++) {
			double vecX=new Vector2D().convertVector(width,i).getX();
			double vecY=new Vector2D().convertVector(height,i).getY();
			addPoint((int)vecX+x,(int)vecY+y);
		}
		return this;
	}
	public Object2D addRect(int x, int y, int w, int h) {
		addPoint(x,y);
		addPoint(x+w,y);
		addPoint(x+w,y+h);
		addPoint(x,y+h);
		return this;
	}
		
	public Object2D addPoint(int x, int y) {
		vecs.add(new Vector2D(x,y));
		vecsOrg.add(new Vector2D(x,y));
		return this;
	}
	
	private boolean checkQuad(Object2D obj) {
		double xMin=100000000; double yMin=100000000;
		double xMax=-100000000; double yMax=-100000000;
		double xMin2=100000000; double yMin2=100000000;
		double xMax2=-100000000; double yMax2=-100000000;
		xMin=getPoint(0).getX();
		xMax=getPoint(2).getX();
		yMin=getPoint(0).getY();
		yMax=getPoint(2).getY();
		xMin2=obj.getPoint(0).getX();
		xMax2=obj.getPoint(2).getX();
		yMin2=obj.getPoint(0).getY();
		yMax2=obj.getPoint(2).getY();
		if (xMin<xMax2&&xMax>xMin2&&yMin<yMax2&&yMax>yMin2)
			return true;
		return false;
	}
	protected Vector2D satDetection(Object2D obj) {
		if(vecs.size()==4&&obj.getSize()==4 && angle%90==0) {
			if(checkQuad(obj)) return obj.getDisplacement();
		}
		for (int a=0;a<vecs.size();a++) {
			int b = (a+1) % vecs.size();
			Vector2D axis = new Vector2D(-(getPoint(b).getY()-getPoint(a).getY()),
					getPoint(b).getX()-getPoint(a).getX());
			double min = 100000000; double max =-100000000;
			for (int p=0;p<getSize();p++) {
				double dot=getPoint(p).dotProd(axis);
				min=Math.min(min,dot);
				max=Math.max(max,dot);
			}
			double min2 = 100000000; double max2 =-100000000;
			for (int p=0;p<obj.getSize();p++) {
				double dot=obj.getPoint(p).dotProd(axis);
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
		poly.addPoint((int)getPoint(i).getX(),(int)getPoint(i).getY());
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
	public Object2D setImage(String path) {
		this.path=path;
		try {
		    img = ImageIO.read(new File(path));
		} catch (IOException e) {
		}
		return this;
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
	public Object2D setAngle(double angle) {
		this.angle=Math.toRadians(angle%360);
		return this;
	}
	public void rotate(double angle) {
		this.angle+=Math.toRadians(angle%360);
		rotate();
	}
	public void rotate() {
		Vector2D vec = getCenter();
		Vector2D camPos = Camera2D.getPos();
		for (int i=0; i<getSize();i++) {
			double x=vecsOrg.get(i).getX()-vec.getX();
			double y=vecsOrg.get(i).getY()-vec.getY();
			double xRot = x*Math.cos(angle)-y*Math.sin(angle);
			double yRot = x*Math.sin(angle)+y*Math.cos(angle);
			vecs.set(i,new Vector2D(vec.getX()+xRot+pos.getX()+camPos.getX(),vec.getY()+yRot+pos.getY()+camPos.getY()));
		}
	}
	private Vector2D getCenter() {
		double x=0;
		double y=0;
		for (int i=0; i<getSize();i++) {
			x+=vecsOrg.get(i).getX();
			y+=vecsOrg.get(i).getY();
		}
		x/=getSize();
		y/=getSize();
		return new Vector2D(x,y);
	}
	public Object2D clone() {
		Object2D tmp =new Object2D();
		for(int i=0;i<getSize();i++) {
			tmp.addPoint((int)getOrgPoint(i).getX(),(int)getOrgPoint(i).getY());
			tmp.setAngle(angle);
			tmp.setImage(path);
		}
		return tmp;
	}
}