package Latte;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Object2D {
	private ArrayList<Vector2D> vecs = new ArrayList<Vector2D>();
	private ArrayList<Boolean> emptyPoint = new ArrayList<Boolean>();
	private ArrayList<Vector2D> vecsOrg = new ArrayList<Vector2D>();
	private Vector2D displacement = new Vector2D();
	private boolean isSticky=false;
	private Vector2D pos = new Vector2D(0, 0);
	private BufferedImage img = null;
	private String path = "";
	private double angle = 0;

	public Vector2D getPos() {
		return pos;
	}
	public void setSticky(boolean isSticky) {
		this.isSticky=isSticky;
	}
	public boolean getSticky() {
		return isSticky;
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

	public void setPoint(int i, double x, double y) {
		vecs.set(i, new Vector2D(x, y));
		vecsOrg.set(i, new Vector2D(x, y));
	}

	public void addPosPolar(double mag) {
		Vector2D vec = new Vector2D().convertVector(mag, angle);
		double x =(vec.getX() + pos.getX());
		double y =(vec.getY() + pos.getY());
		updatePos(x, y);
	}

	public void addPosPolar(double mag, double angle) {
		Vector2D vec = new Vector2D().convertVector(mag, angle);
		double x = (vec.getX() + pos.getX());
		double y = (vec.getY() + pos.getY());
		updatePos(x, y);
	}
	public int getAngle() {
		return (int) Math.toDegrees(angle);
	}
	public void updatePos(double x, double y) {
		displacement.setPos(displacement.getX()+(x - pos.getX()), 
				displacement.getY()+(y - pos.getY()));
		pos.setPos(x, y);
		for (int i = 0; i < getSize(); i++) {
			vecs.set(i,new Vector2D(vecsOrg.get(i).getX() + x, 
					vecsOrg.get(i).getY() + y));
		}
		if(Math.toDegrees(angle)%90!=0)rotate();
	}

	public Object2D addOval(double x, double y, double width, double height) {
		for (int i = 0; i < 360; i++) {
			double vecX = new Vector2D().convertVector(width, i).getX();
			double vecY = new Vector2D().convertVector(height, i).getY();
			addPoint( vecX + x, vecY + y);
		}
		return this;
	}

	public Object2D addRect(double x, double y, double w, double h) {
		addPoint(x, y);
		addPoint(x + w, y);
		addPoint(x + w, y + h);
		addPoint(x, y + h);
		return this;
	}
	public Object2D addRect(double x, double y,double dx, double dy, double w, double h) {
		x+=dx*w;
		y+=dy*h;
		addPoint(x, y);
		addPoint(x + w, y);
		addPoint(x + w, y + h);
		addPoint(x, y + h);
		return this;
	}

	public Object2D addEmptyPoint(double x, double y) {
		vecs.add(new Vector2D(vecs.get(0).getX() + x, vecs.get(0).getY() + y));
		vecsOrg.add(new Vector2D(x,y));
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

	public ArrayList<Vector2D> getPoints() {
		return vecs;
	}

	public Object2D addPoint(double x, double y) {
		vecs.add(new Vector2D(x, y));
		vecsOrg.add(new Vector2D(x, y));
		emptyPoint.add(false);
		return this;
	}
	private boolean checkQuad(ArrayList<Vector2D> vec) {
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

	public Vector2D collideWith(Vector2D point) {
		// May not actually be sat.
		ArrayList<Vector2D> vecs = getNonEmpty(this.vecs);
		boolean collide = false;
		for (int a = 0; a < vecs.size(); a++) {
			Vector2D vc = vecs.get(a);
			Vector2D vn = vecs.get(0);
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
		return point;
	}
	public boolean checkLine(ArrayList<Vector2D> vecs2) {
		ArrayList<Vector2D> vecs = getNonEmpty(this.vecs);
		double x1=vecs2.get(0).getX();
		double y1=vecs2.get(0).getY();
		double x2=vecs2.get(1).getX();
		double y2=vecs2.get(1).getY();
		for (int a = 0; a < vecs.size(); a++) {
			Vector2D vc = vecs.get(a);
			Vector2D vn = vecs.get(0);
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
	public static Vector2D collideWith(Group2D group,Vector2D vec) {
		Vector2D dis;
		for(Object2D obj: group.get()) {
			dis =obj.collideWith(vec);
			if (dis != null)
				return dis;
		}
		return null;
	}
	public Vector2D collideWith(Group2D group) {
		Vector2D vec;
		for(Object2D obj: group.get()) {
			vec =obj.collideWith(this);
			if (vec != null)
				return vec;
		}
		return null;
	}

	public Vector2D collideWith(Object2D obj) {
		ArrayList<Vector2D> vecs = getNonEmpty(this.vecs);
		ArrayList<Vector2D> vecs2 = obj.getNonEmpty(obj.getPoints());
		if (vecs.size() == 4 && vecs2.size() == 4 && Math.toDegrees(angle)%90 == 0) {
			if (checkQuad(vecs2))
				return obj.getDisplacement();
		}
		if (vecs2.size() == 2) {
			if (checkLine(vecs2))
				return obj.getDisplacement();
			return null;
		}
		for (int a = 0; a < vecs.size(); a++) {
			int b = (a + 1) % vecs.size();
			Vector2D axis = new Vector2D(-(getPoint(b).getY() - getPoint(a).getY()),
					getPoint(b).getX() - getPoint(a).getX());
			double min = 100000000;
			double max = -100000000;
			for (int p = 0; p < getSize(); p++) {
				double dot = getPoint(p).dotProd(axis);
				min = Math.min(min, dot);
				max = Math.max(max, dot);
			}
			double min2 = 100000000;
			double max2 = -100000000;
			for (int p = 0; p < vecs2.size(); p++) {
				double dot = vecs2.get(p).dotProd(axis);
				min2 = Math.min(min2, dot);
				max2 = Math.max(max2, dot);
			}
			if (!(max2 >= min && max >= min2))
				return null;
		}
		return obj.getDisplacement();
	}

	private Polygon getPoly() {
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

	public Vector2D getEmpty(int indx) {
		ArrayList<Vector2D> empties = new ArrayList<Vector2D>();
		for (int i = 0; i < getSize(); i++) {
			if (emptyPoint.get(i))
				empties.add(vecs.get(i));
		}
		if (indx < empties.size())
			return empties.get(indx);
		return null;
	}
	public void fill(Graphics g) {
		displacement.setPos(0,0);
		Polygon poly = getPoly();
		g.fillPolygon(poly);
		g.drawPolygon(poly);
	}

	public void draw(Graphics g) {
		displacement.setPos(0,0);
		Polygon poly = getPoly();
		g.drawPolygon(poly);
	}

	public Object2D setImage(String path) {
		this.path = path;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
		}
		return this;
	}

	public void drawImage(Graphics g) {
		displacement.setPos(0,0);
		if (img != null) {
			Rectangle r = new Rectangle(0, 0, img.getWidth(), img.getHeight());
			drawImage(g, r);
		}
	}
	
	public void drawImage(Graphics g, int x, int y, int w, int h) {
		displacement.setPos(0,0);
		Rectangle r = new Rectangle(x, y, w, h);
		drawImage(g, r);
	}

	private void drawImage(Graphics g, Rectangle clip) {
		displacement.setPos(0,0);
		Polygon poly = getOrgPoly();
		Rectangle rect = poly.getBounds();
		poly.translate(-rect.x, -rect.y);
		BufferedImage out = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_ARGB);
		double x = clip.width / 2;
		double y = clip.height / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(-angle,x,y);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		Graphics g2 = out.getGraphics();
		g2.setClip(poly);
		g2.drawImage(op.filter(img, null), clip.x, clip.y, clip.width, clip.height, null);
		g.drawImage(out, (int) pos.getX() + rect.x, (int) pos.getY() + rect.y, null);
	}
	public void resetDisplacement() {
		displacement.setPos(0,0);
	}
	public Object2D setAngle(double angle) {
		this.angle = Math.toRadians(angle % 360);
		return this;
	}

	public void rotate(double angle) {
		this.angle += Math.toRadians(angle % 360);
		rotate();
	}

	public void lookAt(double x, double y) {
		double distX = pos.getX() - x;
		double distY = pos.getY() - y;
		angle = new Vector2D(-distX, distY).getAngle();
		rotate();
	}

	public void rotate() {
		Vector2D vec = getCenter();
		for (int i = 0; i < getSize(); i++) {
			double x=vecsOrg.get(i).getX()-vec.getX();
			double y=vecsOrg.get(i).getY()-vec.getY();
			double xRot = (x * Math.cos(angle) - y * Math.sin(angle));
			double yRot = -(x * Math.sin(angle) + y * Math.cos(angle));
			vecs.set(i, new Vector2D((vec.getX() + xRot)+pos.getX(),
					(vec.getY() + yRot)+pos.getY()));
		}
	}

	private ArrayList<Vector2D> getNonEmpty(ArrayList<Vector2D> vecs) {
		ArrayList<Vector2D> notEmpties = new ArrayList<Vector2D>();
		for (int i = 0; i < getSize(); i++) {
			if (!emptyPoint.get(i))
				notEmpties.add(vecs.get(i));
		}
		return notEmpties;
	}

	private Vector2D getCenter() {
		ArrayList<Vector2D> vecs = getNonEmpty(vecsOrg);
		double x = 0;
		double y = 0;
		for (Vector2D vec: vecs) {
			x += vec.getX();
			y += vec.getY();
		}
		x /= vecs.size();
		y /= vecs.size();
		return new Vector2D(x, y);
	}
	
	public Object2D clone() {
		Object2D tmp = new Object2D();
		for (int i = 0; i < getSize(); i++) {
			if (!emptyPoint.get(i))
				tmp.addPoint( getOrgPoint(i).getX(),  getOrgPoint(i).getY());
			else
				tmp.addEmptyPoint( getOrgPoint(i).getX(),  getOrgPoint(i).getY());
			//tmp.updatePos(getPoint(i).getX(), getPoint(i).getY());
			tmp.setAngle(angle);
			tmp.setImage(path);
		}
		return tmp;
	}
	public void setPoints(double x, double y) {
		for (int i = 0; i < getSize(); i++) {
			vecs.set(i,new Vector2D(vecsOrg.get(i).getX() + x, 
					vecsOrg.get(i).getY() + y));
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
	public Object2D load(String line) {
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
}