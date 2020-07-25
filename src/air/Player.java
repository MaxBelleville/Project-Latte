package air;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import Latte.*;
import Latte.Flat.*;

public class Player extends Block {
	private static int cooldown=0;
	private int layer=0;
	private boolean nearWall=false;
	private boolean nearEdge=false;
	private static Vector teleportPos=new Vector();
	private Vector mousePos=new Vector();
	private Ray ray = new Ray();
	private Vector spawn=new Vector(64,64);
	private Animate animation= new Animate(300);
	private Animate deathAnimate= new Animate(200);
	public Player() {
		addRect(0, 0, 16,16).addEmptyPoint(16, 8);
		setSticky(true);
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
			Handler.onDelay(Player::cooldown, 10);
	}
	public void canTeleport(int indx) {
		Vector pos = getPos();
		if (cooldown == 0&&!nearWall) {
			teleportPos = new Vector(pos.getX(), pos.getY());
			if (getLayer() == indx) Handler.animate(Player::teleport, animation);
			setLayer(indx);
			if (!nearEdge) updatePos(mousePos.getX()-8, mousePos.getY()-8);
			else {
				teleportPos = new Vector(pos.getX() * 2 - mousePos.getX(), pos.getY() * 2 - mousePos.getY());
				Camera.movePos((pos.getX()-mousePos.getX())/2,(pos.getY()-mousePos.getY())/2);
			}
			cooldown = 30;
			Handler.onDelay(Player::cooldown, 10);
		}
	}
	public void setSpawn(Vector pos) {
		updatePos(pos.getX(),pos.getY());
		spawn=pos;
		
	}
	public static void teleport(Graphics g, int ms) {
		Random rand = new Random();
		int x = (int) teleportPos.getX()+rand.nextInt(7)-3, y = (int) teleportPos.getY()+rand.nextInt(7)-3;
		int c=255-(ms/3);
		g.setColor(new Color((ms/3),(ms/3),(ms/3),c));
		if (ms < 60) g.fillRect(x, y, 20, 20);
		else if (ms < 120) g.fillOval(x + 2, y + 2, 16, 16);
		else if (ms < 180) g.fillOval(x + 4, y + 4, 12, 12);
		else if (ms < 240) g.fillOval(x + 6, y + 6, 8, 8);
		else g.fillRect(x + 8, y + 8, 4, 4);
	}
	public static void death(Graphics g, int ms) {
		g.setColor(new Color(1f,0,0,0.1f));
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
		if(wall!=null)
		ray.updateTillCollide(vecEmpty,mousePos,5,wall);
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
		updatePos(spawn.getX(),spawn.getY());
		Handler.animate(Player::death, deathAnimate);
	}
}