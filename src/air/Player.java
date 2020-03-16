package air;

import java.awt.Color;
import java.awt.Graphics;

import Latte.*;

public class Player extends Object2D {
	private static int cooldown=0;
	private int layer=0;
	private boolean nearWall=false;
	private static Handler handler;
	private static Vector2D teleportPos=new Vector2D();
	private static Vector2D mouseLas=new Vector2D();
	private static Vector2D mousePos=new Vector2D();
	private static Object2D laser = new Object2D();
	private Animate2D animation= new Animate2D(300);
	private Animate2D deathAnimate= new Animate2D(1000);
	public Player(Handler handler) {
		addRect(0, 0, 20, 20).addEmptyPoint(20, 10);
		updatePos(64, 64);
		laser.addPoint(0, 0);
		laser.addPoint(10, 10);
		Player.handler = handler;
	}
	public int getLayer() {
		return layer;
	}
	public int getCooldown() {
		return cooldown;
	}
	public Vector2D getMousePos() {
		return mousePos;
	}
	public boolean getNearWall() {
		return nearWall;
	}
	public void setLayer(int layer) {
		this.layer=layer;
	}
	public void teleportStop() {
		animation.stop();
	}
	public static void cooldown() {
		cooldown--;
		if (cooldown != 0)
			handler.onDelay("cooldown", 10);
	}
	public void canTeleport(Layer layer,int indx) {
		Vector2D pos = getPos();
		if (cooldown == 0) {
			teleportPos = new Vector2D(pos.getX(), pos.getY());
			if (getLayer() == indx) handler.animate("teleport", animation);
			setLayer(indx);
			if (!nearWall) updatePos(mousePos.getX(), mousePos.getY());
			else {
				teleportPos = new Vector2D(pos.getX() * 2 - mousePos.getX(), pos.getY() * 2 - mousePos.getY());
				Camera2D.movePos(pos.getX() - mousePos.getX(), pos.getY() - mousePos.getY());
				Camera2D.updateGroups(layer.get("floor"), layer.get("wall"));
			}
			cooldown = 30;
			mouseLas.setPos(0, 0);
			handler.onDelay("cooldown", 10);
		}
	}
	public static void teleport(Graphics g, int ms) {
		int x = (int) teleportPos.getX(), y = (int) teleportPos.getY();
		g.setColor(Color.gray);
		if (ms < 60)g.fillOval(x, y, 20, 20);
		else if (ms < 120)g.fillOval(x + 2, y + 2, 16, 16);
		else if (ms < 180)g.fillOval(x + 4, y + 4, 12, 12);
		else if (ms < 240)g.fillOval(x + 6, y + 6, 8, 8);
		else g.fillRect(x + 8, y + 8, 4, 4);
	}
	public static void death(Graphics g, int ms) {
		g.setColor(new Color(1f,0,0,0.05f));
		int w = Window.getWidth();
		int h = Window.getHeight();
		int val=30;
		for(int y=0;y<=(h/val);y+=2) {
		for(int x=0;x<=(w/val)+1;x++) {
		if (ms < x*10)g.fillOval((val*x),(val*y),val,val);
		if (ms < x*10)g.fillOval(w-(val*x),(val*y)+val,val,val);
		}
		}
	}
	public void setNearWall(boolean b) {
		nearWall=b;
	}
	public static void laserAvoidWall() {
		mouseLas.setPos(mousePos.getX(), mousePos.getY());
	}
	public void draw(Graphics g,Group2D wall, int layer) {
		Vector2D vecEmpty = getEmpty(0);
		animation.draw(g);
		g.setColor(Color.black);
		if(this.layer==layer)super.fill(g);
		g.setColor(Color.red);
		laser.getPoint(0).setPos(vecEmpty.getX(), vecEmpty.getY());
		laser.getPoint(1).setPos(mousePos.getX(), mousePos.getY());
		handler.onAvoid("laserAvoidWall", laser, wall);
		if (this.layer==layer && mouseLas.getX() != 0)
			g.drawLine((int) vecEmpty.getX(), (int) vecEmpty.getY(), (int) mouseLas.getX(), (int) mouseLas.getY());
		deathAnimate.draw(g);
	}
	public void setMousePos(int x, int y) {
		mousePos.setPos(x,y);
	}
	public void reset() {
		updatePos(64, 64);
		handler.animate("death", deathAnimate);
	}
}