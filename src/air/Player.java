package air;

import java.awt.Color;
import java.awt.Graphics;

import Latte.*;
import Latte.Flat.*;

public class Player extends Block {
	private static int cooldown=0;
	private int layer=0;
	private boolean nearWall=false;
	private boolean nearEdge=false;
	private static Handler handler;
	private static Vector teleportPos=new Vector();
	private static Vector mousePos=new Vector();
	private static Ray ray = new Ray();
	private Animate animation= new Animate(300);
	private static Animate deathAnimate= new Animate(200);
	public Player(Handler handler) {
		addRect(0, 0, 20,20).addEmptyPoint(20, 10);
		updatePos(64, 64);
		Player.handler = handler;
	}
	public int getLayer() {
		return layer;
	}
	public int getCooldown() {
		return cooldown;
	}
	public Vector getMousePos() {
		return mousePos;
	}
	public boolean getNearEdge() {
		return nearEdge;
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
	public void canTeleport(Layer layer,int indx,Block light) {
		Vector pos = getPos();
		if (cooldown == 0&&!nearWall) {
			teleportPos = new Vector(pos.getX(), pos.getY());
			if (getLayer() == indx) handler.animate("teleport", animation);
			setLayer(indx);
			if (!nearEdge) updatePos(mousePos.getX()-10, mousePos.getY()-10);
			else {
				teleportPos = new Vector(pos.getX() * 2 - mousePos.getX(), pos.getY() * 2 - mousePos.getY());
				Camera.movePos(pos.getX() - mousePos.getX(), pos.getY() - mousePos.getY());
				Camera.updateGroups(layer.get("floor"), layer.get("wall"));
				Camera.updateGroups(light);
			}
			cooldown = 30;
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
		g.setColor(new Color(1f,0,0,0.2f));
		int w = Window.getWidth();
		int h = Window.getHeight();
		g.fillRect(0, 0, w,h);
	}
	public void setNearEdge(boolean b) {
		nearEdge=b;
	}
	public void setNearWall(boolean b) {
		nearWall=b;
	}
	public void draw(Graphics g,Group wall, int layer) {
		Vector vecEmpty = getEmpty(0);
		animation.draw(g);
		g.setColor(Color.black);
		ray.updateTillCollide(vecEmpty,mousePos,wall);
		if(this.layer==layer)super.fill(g);
		g.setColor(Color.red);
		if(this.layer==layer)ray.draw(g);
		deathAnimate.draw(g);
		Vector vec=getPos();
		Client.send(vec.toString()+" "+Camera.getPos()+" "+getAngle());
	}
	public void setMousePos(int x, int y) {
		mousePos.setPos(x,y);
	}
	public void reset() {
		updatePos(64, 64);
		handler.animate("death", deathAnimate);
	}
}