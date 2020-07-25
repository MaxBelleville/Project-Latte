package Latte.Flat;

import Latte.Window;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Block {
	private ArrayList<Vector> vecs = new ArrayList<Vector>();
	private ArrayList<Boolean> emptyPoint = new ArrayList<Boolean>();
	private ArrayList<Vector> vecsOrg = new ArrayList<Vector>();
	private Vector displacement = new Vector();
	private Vector offset = new Vector();
	private boolean isSticky=false;
	private Color blockColor;
	private Vector pos = new Vector(0, 0);
	private BufferedImage img = null;
	private String path = "";
	private int angle = 0;

	public Vector getPos() {
		return pos;
	}
	public void setSticky(boolean isSticky) {
		this.isSticky=isSticky;
	}
	public boolean getSticky() {
		return isSticky;
	}
	public Vector getDisplacement() {
		return displacement;
	}

	public Vector getPoint(int i) {
		return vecs.get(i);
	}

	public Vector getOrgPoint(int i) {
		return vecsOrg.get(i);
	}

	public int getSize() {
		return vecs.size();
	}
	
	public void setPoint(int i, double x, double y) {
		vecs.set(i, new Vector(x, y));
		vecsOrg.set(i, new Vector(x, y));
	}

	public void addPosPolar(double mag) {
		Vector vec = new Vector().convertVector(mag, angle);
		double x =(vec.getX() + pos.getX());
		double y =(vec.getY() + pos.getY());
		updatePos(x, y);
	}

	public void addPosPolar(double mag, double angle) {
		Vector vec = new Vector().convertVector(mag, angle%360);
		double x = (vec.getX() + pos.getX());
		double y = (vec.getY() + pos.getY());
		updatePos(x, y);
	}
	public int getAngle() {
		return angle;
	}
	public void updatePos(double x, double y) {
		displacement.setPos(displacement.getX()+(x - pos.getX()), 
				displacement.getY()+(y - pos.getY()));
		pos.setPos(x, y);
		for (int i = 0; i < getSize(); i++) {
			vecs.set(i,new Vector(vecsOrg.get(i).getX() + x+offset.getX(), 
					vecsOrg.get(i).getY() + y+offset.getY()));
		}
		if(angle%90!=0)rotate();
	}
	
	public Block addOval(double x, double y, double width, double height) {
		for (int i = 0; i < 360/8; i++) {
			double vecX = new Vector().convertVector(width, i*8).getX();
			double vecY = new Vector().convertVector(height, i*8).getY();
			addPoint( vecX + x, vecY + y);
		}
		return this;
	}

	public Block addRect(double x, double y, double w, double h) {
		addPoint(x, y);
		addPoint(x + w, y);
		addPoint(x + w, y + h);
		addPoint(x, y + h);
		return this;
	}
	public Block addRect(double x, double y,double dx, double dy, double w, double h) {
		x+=dx*w;
		y+=dy*h;
		addPoint(x, y);
		addPoint(x + w, y);
		addPoint(x + w, y + h);
		addPoint(x, y + h);
		return this;
	}

	public Block addEmptyPoint(double x, double y) {
		vecs.add(new Vector(vecs.get(0).getX() + x, vecs.get(0).getY() + y));
		vecsOrg.add(new Vector(x,y));
		emptyPoint.add(true);
		return this;
	}

	public boolean hasEmpties() {
		for (boolean empty : emptyPoint) {
			if (empty)
				return true;
		}
		return false;
	}

	public ArrayList<Vector> getPoints() {
		return vecs;
	}

	public Block addPoint(double x, double y) {
		vecs.add(new Vector(x, y));
		vecsOrg.add(new Vector(x, y));
		emptyPoint.add(false);
		return this;
	}
	private boolean checkQuad(ArrayList<Vector> vec) {
		double xMin = getPoint(0).getX();
		double xMax = getPoint(2).getX();
		double yMin = getPoint(0).getY();
		double yMax = getPoint(2).getY();
		double xMin2 = vec.get(0).getX();
		double xMax2 = vec.get(2).getX();
		double yMin2 = vec.get(0).getY();
		double yMax2 = vec.get(2).getY();
		if (xMin < xMax2 && xMax > xMin2 && yMin < yMax2 && yMax > yMin2)
			return true;
		return false;
	}
	public Block collideWith(Vector point) {
		// May not actually be sat.
		ArrayList<Vector> vecs = getNonEmpty(this.vecs);
		boolean collide = false;
		for (int a = 0; a < vecs.size(); a++) {
			Vector vc = vecs.get(a);
			Vector vn = vecs.get(0);
			if (a + 1 != vecs.size())
				vn = vecs.get(a + 1);
			if (((vc.getY() > point.getY()) != (vn.getY() > point.getY()))
					&& (point.getX() < (vn.getX() - vc.getX()) * (point.getY() - vc.getY()) / (vn.getY() - vc.getY())
							+ vc.getX())) {
				collide = !collide;
			}
		}
		if (!collide)
			return null;
		return this;
	}
	public boolean checkLine(ArrayList<Vector> vecs2) {
		ArrayList<Vector> vecs = getNonEmpty(this.vecs);
		double x1=vecs2.get(0).getX();
		double y1=vecs2.get(0).getY();
		double x2=vecs2.get(1).getX();
		double y2=vecs2.get(1).getY();
		for (int a = 0; a < vecs.size(); a++) {
			Vector vc = vecs.get(a);
			Vector vn = vecs.get(0);
			if (a + 1 != vecs.size())
				vn = vecs.get(a + 1);
			double x3 = vc.getX();
			double y3 = vc.getY();
			double x4 = vn.getX();
			double y4 = vn.getY();
			double uA = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));
			double uB = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));

			  // if uA and uB are between 0-1, lines are colliding
			  if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1)
			    return true;
		}
		return false;
	}
	public static Block collideWith(Group group,Vector vec) {
		Block block;
		for(Block obj: group.get()) {
			block =obj.collideWith(vec);
			if (block != null)
				return block;
		}
		return null;
	}
	public Block collideWith(Group group) {
		Block block;
		for(Block obj: group.get()) {
			block =obj.collideWith(this);
			if (block != null)
				return block;
		}
		return null;
	}

	public Block collideWith(Block obj) {
		ArrayList<Vector> vecs = getNonEmpty(this.vecs);
		ArrayList<Vector> vecs2 = obj.getNonEmpty(obj.getPoints());
		if (vecs.size() == 4 && vecs2.size() == 4 && angle%90 == 0) {
			if (checkQuad(vecs2))
				return this;
		}
		if (vecs2.size() == 2) {
			if (checkLine(vecs2))
				return this;
			return null;
		}
		for (int a = 0; a < vecs.size(); a++) {
			int b = (a + 1) % vecs.size();
			Vector axis = new Vector(-(getPoint(b).getY() - getPoint(a).getY()),
					getPoint(b).getX() - getPoint(a).getX());
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;
			for (int p = 0; p < getSize(); p++) {
				double dot = getPoint(p).dotProd(axis);
				min = Math.min(min, dot);
				max = Math.max(max, dot);
			}
			double min2 = Double.MAX_VALUE;
			double max2 = Double.MIN_VALUE;
			for (int p = 0; p < vecs2.size(); p++) {
				double dot = vecs2.get(p).dotProd(axis);
				min2 = Math.min(min2, dot);
				max2 = Math.max(max2, dot);
			}
			if (!(max2 >= min && max >= min2))
				return null;
		}
		return this;
	}

	public Polygon getPoly() {
		Polygon poly = new Polygon();
		for (int i = 0; i < vecs.size(); i++) {
			if (!emptyPoint.get(i))
				poly.addPoint((int) getPoint(i).getX(), (int) getPoint(i).getY());
		}
		return poly;
	}

	private Polygon getOrgPoly() {
		Polygon poly = new Polygon();
		for (int i = 0; i < vecs.size(); i++) {
			if (!emptyPoint.get(i))
				poly.addPoint((int) vecsOrg.get(i).getX(), (int) vecsOrg.get(i).getY());
		}
		return poly;
	}

	public Vector getEmpty(int indx) {
		ArrayList<Vector> empties = new ArrayList<Vector>();
		for (int i = 0; i < getSize(); i++) {
			if (emptyPoint.get(i))
				empties.add(vecs.get(i));
		}
		if (indx < empties.size())
			return empties.get(indx);
		return null;
	}
	public void fill(Graphics g) {
		if(!isSticky)updateOffset();
		displacement.setPos(0,0);
		Polygon poly = getPoly();
		if(outsideWindow(poly.getBounds())) return;
		Color oldColor=g.getColor();
		if(blockColor!=null)g.setColor(blockColor);
		g.fillPolygon(poly);
		g.drawPolygon(poly);
		g.setColor(oldColor);
	}
	private boolean outsideWindow(Rectangle rect) {
		Rectangle bounds=new Rectangle(0,0,Window.getWidth(),Window.getHeight());
		return !rect.intersects(bounds);
	}
	public void updateOffset() {
		offset.setPos(Camera.getPos().getX(),Camera.getPos().getY());
	}
	public void draw(Graphics g) {
		if(!isSticky)updateOffset();
		displacement.setPos(0,0);
		Polygon poly = getPoly();
		if(outsideWindow(poly.getBounds())) return;
		Color oldColor=g.getColor();
		if(blockColor!=null)g.setColor(blockColor);
		g.drawPolygon(poly);
		g.setColor(oldColor);
	}

	public Block setImage(String path) {
		try {
			img = ImageIO.read(new File(path));
			this.path = path;
		} catch (IOException e) {
		}
		return this;
	}
	public void drawRectImg(Graphics g) {
		if(img==null) {
			fill(g);
			return;
		}
		if(!isSticky)updateOffset();
		displacement.setPos(0,0);
		if(outsideWindow(getPoly().getBounds())) return;
		Polygon poly = getOrgPoly();
		Rectangle rect = poly.getBounds();
		if (img != null) {
			double locX = img.getWidth() / 2;
			double locY = img.getHeight() / 2;
			AffineTransform tx = AffineTransform.getRotateInstance(-Math.toRadians(angle), locX, locY);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			g.drawImage(op.filter(img, null),(int)(pos.getX()+offset.getX())+rect.x,(int)(pos.getY()+offset.getY())+rect.y,null);
		}
	}
	public void drawImage(Graphics g) {
		if(!isSticky)updateOffset();
		displacement.setPos(0,0);
		if(vecs.size()==4) drawRectImg(g);
		else {
			Rectangle r = new Rectangle(0, 0, img.getWidth(), img.getHeight());
			drawImage(g, r);
		}
	}
	
	public void drawImage(Graphics g, int x, int y, int w, int h) {
		if(!isSticky)updateOffset();
		displacement.setPos(0,0);
		if(vecs.size()==4) drawRectImg(g);
		Rectangle r = new Rectangle(x, y, w, h);
		drawImage(g, r);
	}

	private void drawImage(Graphics g, Rectangle clip) {
		if(img==null) {
			fill(g);
			return;
		}
		if(outsideWindow(getPoly().getBounds())) return;
		Polygon poly = getOrgPoly();
		Rectangle rect = poly.getBounds();
		poly.translate(-rect.x, -rect.y);
		BufferedImage out = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_ARGB);
		double x = clip.width / 2;
		double y = clip.height / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(-Math.toRadians(angle),x,y);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		Graphics g2 = out.getGraphics();
		g2.setClip(poly);
		g2.drawImage(op.filter(img, null), clip.x, clip.y, clip.width, clip.height, null);
		g.drawImage(out, (int) (pos.getX()+offset.getX()) + rect.x, (int) (pos.getY()+offset.getY()) + rect.y, null);
	}
	public void resetDisplacement() {
		displacement.setPos(0,0);
	}
	public Block setAngle(int angle) {
		this.angle = angle % 360;
		return this;
	}

	public void rotate(int angle) {
		this.angle = (this.angle+angle)% 360;
		rotate();
	}

	public void lookAt(double x, double y) {
		double dx=x-pos.add(offset).getX();
		double dy=(y-pos.add(offset).getY());
		double angle=Math.atan2(dy, dx);
		if (angle < 0)angle = Math.abs(angle);
		else angle= 2 * Math.PI - angle;
		this.angle= (int)Math.toDegrees(angle)%360;
		rotate();
	}

	public void rotate() {
		Vector vec = getCenter();
		for (int i = 0; i < getSize(); i++) {
			double x=vecsOrg.get(i).getX()-vec.getX();
			double y=vecsOrg.get(i).getY()-vec.getY();
			double xRot = (x * Math.cos(Math.toRadians(angle)) - y * Math.sin(Math.toRadians(angle)));
			double yRot = -(x * Math.sin(Math.toRadians(angle)) + y * Math.cos(Math.toRadians(angle)));
			vecs.set(i, new Vector((vec.getX() + xRot)+pos.getX()+offset.getX(),
					(vec.getY() + yRot)+pos.getY()+offset.getY()));
		}
	}
	
	private ArrayList<Vector> getNonEmpty(ArrayList<Vector> vecs) {
		ArrayList<Vector> notEmpties = new ArrayList<Vector>();
		for (int i = 0; i < getSize(); i++) {
			if (!emptyPoint.get(i))
				notEmpties.add(vecs.get(i));
		}
		return notEmpties;
	}

	private Vector getCenter() {
		ArrayList<Vector> vecs = getNonEmpty(vecsOrg);
		double x = 0;
		double y = 0;
		for (Vector vec: vecs) {
			x += vec.getX();
			y += vec.getY();
		}
		x /= vecs.size();
		y /= vecs.size();
		return new Vector(x, y);
	}
	
	public Block clone() {
		Block tmp = new Block();
		for (int i = 0; i < getSize(); i++) {
			if (!emptyPoint.get(i)) tmp.addPoint( getOrgPoint(i).getX(),  getOrgPoint(i).getY());
			else tmp.addEmptyPoint( getOrgPoint(i).getX(),  getOrgPoint(i).getY());
			tmp.setAngle(getAngle());
			tmp.setImage(path);
			tmp.setColor(blockColor);
			tmp.setSticky(getSticky());
		}
		return tmp;
	}
	public void setPoints(double x, double y) {
		for (int i = 0; i < getSize(); i++) {
			vecs.set(i,new Vector(vecsOrg.get(i).getX() + x+offset.getX(), 
					vecsOrg.get(i).getY() + y+offset.getY()));
		}
	}
	public String toString() {
		DecimalFormat df = new DecimalFormat("0.00");
		String tmp=(isSticky?1:0)+", "+getAngle()+", "+df.format(getPos().getX())+
				", "+df.format(getPos().getY())+", "+path;
		for(int i=0;i<getSize();i++)
			tmp+=", "+df.format(vecsOrg.get(i).getX())+", "+df.format(vecsOrg.get(i).getY())+", "+(emptyPoint.get(i)?1:0);
		return tmp;
	}
	public Block load(String line) {
		String msg[]=line.split(", ");
		double x=0,y=0;
		if(!msg[0].isEmpty())isSticky=Boolean.parseBoolean(msg[0]);
		setAngle(Integer.parseInt(msg[1]));
		x=Double.parseDouble(msg[2]);
		y=Double.parseDouble(msg[3]);
		if(!msg[4].isEmpty())setImage(msg[4]);
		for(int i=5;i<msg.length;i+=3) {
			double tmpX=Double.parseDouble(msg[i]);
			double tmpY=Double.parseDouble(msg[i+1]);
			boolean empty =Boolean.parseBoolean(msg[i+2]);
			if(empty) addEmptyPoint(tmpX,tmpY);
			else addPoint(tmpX,tmpY);
		}
		updatePos(x,y);
		return this;
	}
	public Vector getOffset() {
		return offset;
	}
	public Block addRectImage(String path, int x, int y) {
		setImage(path);
		if(img!=null) addRect(x,y,img.getWidth(),img.getHeight());
		return this;
	}
	public boolean isNew() {
		return vecs.isEmpty();
	}
	public String getImage() {
		return path;
	}
	public Color getColor() {
		return blockColor;
	}
	
	public Block setColor(Color color) {
		blockColor=color;
		return this;
	}
}