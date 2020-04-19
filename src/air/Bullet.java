package air;

import java.awt.Color;
import java.awt.Graphics;

import Latte.*;
public class Bullet {
	private Object2D bullet = new Object2D();
	private Vector2D moveVec = new Vector2D();
	private Animate2D animation;
	private int tick=0;
	
	public Bullet(int endTime, Vector2D startPos,Vector2D lookAt) {
		animation = new Animate2D(endTime);
		bullet.addRect(-8,-8,16,16);
		bullet.updatePos(startPos.getX(),startPos.getY());
		bullet.lookAt(lookAt.getX(),lookAt.getY());
		bullet.setImage("bitmap.png");
		animation.reset();
	}
	public void addAngle(double angle) {
		bullet.setAngle(Math.toDegrees(bullet.getAngle())+angle);
	}
	public Object2D getObj() {
		return bullet;
	}
	public Vector2D getMovement() {
		return moveVec;
	}
	public Bullet setMovement(double forward, double side) {
		moveVec.setPos(forward,side);
		return this;
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
			bullet.addPosPolar(moveVec.getX()*delta);
			bullet.addPosPolar(moveVec.getY()*delta,bullet.getAngle()+90);
		}
	}
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		if(tick!=-1)bullet.drawImage(g,0,0,16,16);
	}
	public void moveBullet(double x, double y) {
		bullet.updatePos(bullet.getPos().getX()+x,bullet.getPos().getY()+y);
	}
	public boolean onCollide(Group2D group) {
		return bullet.collideWith(group)!=null;
	}
	public boolean onCollide(Object2D obj) {
		return bullet.collideWith(obj)!=null;
	}
	public int getTick() {
		return tick;
	}
}