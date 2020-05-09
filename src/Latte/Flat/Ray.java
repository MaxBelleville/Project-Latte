package Latte.Flat;

import java.awt.Graphics;

public class Ray {
	private Block ray = new Block();
	private Vector oldRay=new Vector();
	private Vector oldDir=new Vector();
	public Ray() {
		ray.addPoint(0, 0);
		ray.addPoint(0, 0);
	}
	public Ray(double x, double y) {
		ray.addPoint(x, y);
		ray.addPoint(0, 0);
	}
	public Ray(double x, double y,double x2, double y2) {
		ray.addPoint(x, y);
		ray.addPoint(x2, y2);
	}
	public Vector getEnd() {
		return ray.getPoint(1);
	}
	public Vector getStart() {
		return ray.getPoint(0);
	}
	
	public void update(double x, double y) {
		update(x,y,0,0);
	}
	
	public void update(double x, double y, double x2, double y2) {
		oldRay.setPos(getEnd().getX(),getEnd().getY());
		ray.setPoint(0,x,y);
		ray.setPoint(1,x2,y2);
	}
	
	public void draw(Graphics g) {
		ray.draw(g);
	}
	
	public Vector updateTillCollide(Vector start, Vector end, Group...groups) {
		Vector dir=(end.sub(start)).normalize();
		double dist = end.getDistance(start);
		if(oldDir!=dir) {
		ray.setPoint(0,start.getX(),start.getY());
		ray.setPoint(1,start.getX(),start.getY());
		loop: for(int i=0;i<dist;i++) {
			for(Group group: groups) {
				if(ray.collideWith(group)!=null) break loop;
			}
			ray.setPoint(1,start.getX()+dir.getX()*i,start.getY()+dir.getY()*i);
		}
		}
		oldDir=dir;
		return getEnd();
	}
	
	public void collideWith(Group...groups) {
		boolean canUpdate=true;
		for(Group group: groups) {
			if(ray.collideWith(group)!=null)canUpdate=false;
		}
		if(!canUpdate)ray.setPoint(1,oldRay.getX(),oldRay.getY());
	}
	public void collideWith(Block...objs) {
		boolean canUpdate=true;
		for(Block obj: objs)  {
			if(ray.collideWith(obj)!=null) canUpdate=false;
		}
		if(!canUpdate)ray.setPoint(1,oldRay.getX(),oldRay.getY());
	}
}
