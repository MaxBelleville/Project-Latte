package air;

import java.awt.Graphics;

import Latte.Client;
import Latte.Flat.*;

public class Bullet extends Block {
	private Vector moveVec = new Vector();
	private Animate animation;
	private int tick = 0;

	public Bullet() {
		String[] split = Client.getRecieved().split(" ");
		Client.loadNext();
		animation = new Animate(Integer.parseInt(split[0]));
		addRect(-8, -8, 16, 16);
		Vector pos = Vector.parseVector(split[1] + " " + split[2]);
		Vector camPos = Vector.parseVector(split[3] + " " + split[4]);
		Vector mousePos = Vector.parseVector(split[5] + " " + split[6]);
		moveVec = Vector.parseVector(split[7] + " " + split[8]);
		updatePos(pos.sub(camPos));
		lookAt(mousePos.sub(camPos));
		setImage("bitmap.png");
		animation.reset();
	}
	
	public void addAngle(int angle) {
		setAngle(getAngle() + angle);
	}

	public Block getObj() {
		return this;
	}

	public Vector getMovement() {
		return moveVec;
	}

	public Bullet setMovement(double forward, double side) {
		moveVec.setPos(forward, side);
		return this;
	}

	public static boolean check() {
		if (!Client.hasData())
			return false;
		String[] split = Client.getRecieved().split(" ");
		return split.length == 9;
	}

	public boolean hasStopped() {
		return animation.hasStopped();
	}

	public void update(double delta) {
		if (tick != -1) {
			tick = animation.manualDraw();
			addPosPolar(moveVec.getX() * delta);
			addPosPolar(moveVec.getY() * delta);
		}
	}

	public void draw(Graphics g) {
		if (tick != -1)
			drawImage(g, 0, 0, 16, 16);
	}

	public void moveBullet(double x, double y) {
		updatePos(getPos().getX() + x, getPos().getY() + y);
	}

	public boolean onCollide(Group group) {
		return collideWith(group) != null;
	}

	public boolean onCollide(Block obj) {
		return collideWith(obj) != null;
	}

	public int getTick() {
		return tick;
	}
}