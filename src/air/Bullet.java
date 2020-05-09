package air;

import java.awt.Color;
import java.awt.Graphics;

import Latte.Client;
import Latte.Flat.*;

public class Bullet extends Block {
	private Vector moveVec = new Vector();
	private Animate animation;
	private int tick=0;
	
	public Bullet() {
		String[] split =Client.getRecieved().split(" ");
		Client.loadNext();
			animation = new Animate(Integer.parseInt(split[0]));
			addRect(-8,-8,16,16);
			double x = Double.parseDouble(split[1]);
			double y = Double.parseDouble(split[2]);
			double x2 = Double.parseDouble(split[3]);
			double y2 = Double.parseDouble(split[4]);
			double x3 = Double.parseDouble(split[5]);
			double y3 = Double.parseDouble(split[6]);
			updatePos(x,y);
			lookAt(x2,y2);
			setImage("bitmap.png");
			animation.reset();
			moveVec.setPos(x3,y3);
	}
	
	public Bullet(int endTime, Vector startPos,Vector lookAt) {
		animation = new Animate(endTime);
		addRect(-8,-8,16,16);
		updatePos(startPos.getX(),startPos.getY());
		lookAt(lookAt.getX(),lookAt.getY());
		setImage("bitmap.png");
		animation.reset();
		
	}
	public void addAngle(double angle) {
		setAngle(Math.toDegrees(getAngle())+angle);
	}
	public Block getObj() {
		return this;
	}
	public Vector getMovement() {
		return moveVec;
	}
	public Bullet setMovement(double forward, double side) {
		moveVec.setPos(forward,side);
		return this;
	}
	public static boolean check() {
		if(!Client.hasData()) return false;
		String[] split =Client.getRecieved().split(" ");
		return split.length==7;
	}
	public boolean hasStopped() {
		return animation.hasStopped();
	}
	public void update() {
		update(1);
	}
	public void update(double delta) {
		if(tick!=-1) {
			tick =animation.manualDraw();
			addPosPolar(moveVec.getX()*delta);
			addPosPolar(moveVec.getY()*delta,getAngle()+90);
		}
	}
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		if(tick!=-1)drawImage(g,0,0,16,16);
	}
	public void moveBullet(double x, double y) {
		updatePos(getPos().getX()+x,getPos().getY()+y);
	}
	public boolean onCollide(Group group) {
		return collideWith(group)!=null;
	}
	public boolean onCollide(Block obj) {
		return collideWith(obj)!=null;
	}
	public int getTick() {
		return tick;
	}
}