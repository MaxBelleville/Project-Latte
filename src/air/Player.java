package air;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.Stack;

import Latte.*;
import Latte.Flat.*;

public class Player extends Block {
	private static int cooldown = 0;
	private int layer = 0;
	private boolean nearWall = false;
	private static Vector currTelePos = new Vector();
	private boolean nearEdge = false;
	private Ray ray = new Ray();
	private static boolean goBack = false;
	private Vector spawn = new Vector(64, 64);
	private Animate animation = new Animate(300);
	private Animate deathAnimate = new Animate(200);
	private static Stack<Vector> telePos = new Stack<Vector>();

	public Player() {
		addRect(0, 0, 16, 16).addEmptyPoint(16, 8);
		setSticky(true);
	}

	public int getLayer() {
		return layer;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public void teleportStop() {
		animation.stop();
	}

	public static void cooldown() {
		cooldown--;
		if (cooldown != 0)
			Handler.onDelay(Player::cooldown, 10);
		else if (goBack) {
			telePos.pop();
			goBack = false;
		} else {
			telePos.push(currTelePos);
		}
	}

	public void teleportBack(Layer layer, int indx) {
		if (!telePos.isEmpty() && !goBack) {
			tryTeleport(layer, telePos.lastElement(), indx);
			goBack = true;
		}
	}

	public void teleport(Vector toPos, int indx) {
		Vector fromPos = getPos();
		if (cooldown == 0 && !nearWall) {
			if (!nearEdge) {
				currTelePos = new Vector(fromPos.getX(), fromPos.getY());
				updatePos(toPos.getX() - 8, toPos.getY() - 8);
			} else {
				currTelePos = new Vector(fromPos.getX() * 2 - toPos.getX(), fromPos.getY() * 2 - toPos.getY());
				Camera.movePos((fromPos.getX() - toPos.getX()) / 2, (fromPos.getY() - toPos.getY()) / 2);
			}
			if (getLayer() == indx)
				Handler.animate(Player::animateTeleport, animation);
			setLayer(indx);
			cooldown = 30;
			Handler.onDelay(Player::cooldown, 10);
		}
	}

	public void setSpawn(Vector pos) {
		updatePos(pos.getX(), pos.getY());
		spawn = pos;

	}

	public static void animateTeleport(Graphics g, int ms) {
		Random rand = new Random();
		int x = (int) currTelePos.getX() + rand.nextInt(7) - 3, y = (int) currTelePos.getY() + rand.nextInt(7) - 3;
		int c = 255 - (ms / 3);
		g.setColor(new Color((ms / 3), (ms / 3), (ms / 3), c));
		if (ms < 60)
			g.fillRect(x, y, 20, 20);
		else if (ms < 120)
			g.fillOval(x + 2, y + 2, 16, 16);
		else if (ms < 180)
			g.fillOval(x + 4, y + 4, 12, 12);
		else if (ms < 240)
			g.fillOval(x + 6, y + 6, 8, 8);
		else
			g.fillRect(x + 8, y + 8, 4, 4);
	}

	public static void death(Graphics g, int ms) {
		g.setColor(new Color(1f, 0, 0, 0.1f));
		int w = Window.getWidth();
		int h = Window.getHeight();
		g.fillRect(0, 0, w, h);
	}

	public void draw(Graphics g, Layer layer, Vector mousePos) {
		Vector vecEmpty = getEmpty(0);
		if (animation.hasStopped() && goBack) {
			telePos.pop();
			goBack = false;
		}
		animation.draw(g);
		g.setColor(Color.black);
		ray.updateTillCollide(vecEmpty, mousePos, 5, layer.get("Wall"));
		super.fill(g);
		g.setColor(Color.red);
		ray.draw(g);
		deathAnimate.draw(g);
		Vector vec = getPos();
		Client.send(vec.toString() + " " + Camera.getPos() + " " + getAngle());
	}

	public void reset() {
		updatePos(spawn.getX(), spawn.getY());
		Handler.animate(Player::death, deathAnimate);
	}

	public void tryTeleport(Layer layer, Vector toPos, int indx) {
		Block tempBlock = new Block().addRect(toPos.getX() - 10, toPos.getY() - 10, 20, 20);
		nearWall = false;
		nearEdge = false;
		if (layer.get("Wall") != null)
			Handler.onCollide(wall -> nearWall = true, tempBlock, layer.get("Wall"));
		Handler.onCollide(border -> nearEdge = true, tempBlock, Window.getBorder(60, 60));
		Handler.onCollide(floor -> teleport(toPos, indx), tempBlock, layer.get("Floor"));
	}
}