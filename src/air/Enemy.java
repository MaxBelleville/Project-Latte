package air;

import java.util.ArrayList;

import Latte.Client;
import Latte.Handler;
import Latte.Flat.Block;
import Latte.Flat.Camera;
import Latte.Flat.Navigation;
import Latte.Flat.Ray;
import Latte.Flat.Vector;

public class Enemy extends Block {
	private int layerIndx = 0;
	private boolean isPlayer = false;
	private static int cooldown = 0;

	public Enemy(boolean isPlayer) {
		this.isPlayer = isPlayer;
		addRect(0, 0, 16, 16).addEmptyPoint(16, 8);
		updatePos(600, 600);
	}

	public boolean getIsPlayer() {
		return isPlayer;
	}

	public void updateMP(String msg) {
		if (Client.hasData()) {
			String recieved = Client.getRecieved();
			String[] split = recieved.split(" ");
			if (split.length == 5)
				Client.loadNext();
			if (!recieved.startsWith(msg) && split.length == 5 && Client.hasData() && isPlayer) {
				Vector pos = Vector.parseVector(split[0] + " " + split[1]);
				Vector camPos = Vector.parseVector(split[2] + " " + split[3]);
				updatePos(pos.sub(camPos));
				int angle = Integer.parseInt(split[4]);
				setAngle(angle);
			}
			rotate();
		}
	}

	public void update(ArrayList<Layer> layers, Vector rayEnd, ArrayList<Bullet> bullets) {
		Vector enemyEnd = getPos().add(Camera.getPos());
		Navigation nav = layers.get(layerIndx).getNavigation();
		if (!isPlayer) {
			nav.updateGrid(Camera.getPos(), new Vector(32, 32));
			int x = (int) (Math.random() * 601) - 300;
			int y = (int) (Math.random() * 601) - 300;
			nav.setEnd(enemyEnd.getX(), enemyEnd.getY());
			if (nav.getParent() == null)
				nav.setStart(enemyEnd.getX() + x, enemyEnd.getY() + y);
			Ray ray = new Ray();
			Vector end = ray.updateTillCollide(enemyEnd, rayEnd, 5, layers.get(layerIndx).get("Wall"));
			if (end.getDistance(rayEnd) < 1)
				nav.setStart(rayEnd.getX(), rayEnd.getY());
			nav.solve();
			if (nav.getParent() != null) {
				Vector moveTo = nav.getParent().sub(enemyEnd).normalize().multi(6);
				Vector shift = moveTo.add(getPos());
				if (end.getDistance(rayEnd) >= 1 || getPos().getDistance(rayEnd) >= 200) {
					updatePos(shift);
				} else if (cooldown == 0) {
					cooldown = 30;
					Client.send("2500 " + getEmpty(0) + " " + Camera.getPos() + " " + rayEnd + " 8 0");
					Handler.onDelay(Enemy::delayBullet, 15);
				}
				Vector look = nav.getStart();
				lookAt(look);
			}
			rotate();
		}
	}

	public static void delayBullet() {
		if (cooldown > 0) {
			cooldown--;
			Handler.onDelay(Enemy::delayBullet, 15);
		}
	}
}