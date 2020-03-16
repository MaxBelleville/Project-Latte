package air;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicButtonUI;

import Latte.*;

public class GameMain {
	protected static Handler handler;
	private static Player player;
	private static int layI = 0;
	private static Sound2D sound = new Sound2D("Music/1.mp3");
	private static ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private static Layer layer[] = { new Layer("floor", "wall"), new Layer("floor", "wall") };
	
	public GameMain() {
		handler = new Window().hideMenu().icon("bullet.png").setBackground(Color.black).fullscreen().init();
		player = new Player(handler);
		handler.onMouseMove("mouseMove");
		handler.onMouseDown("mouseDown");
		handler.onKeyDown("keyDown");
		IO2D.readPlain("level1-1.txt");
		layer[0].load(IO2D.loadNext());
		layer[1]=layer[0];
		handler.draw("update");
		for(int i=2;i<9;i++)
			sound.addToQueue("Music/"+i+".mp3");
		sound.randomize();
		sound.play();
	}
	
	public static void main(String[] args) {
		new GameMain();
	}

	private static JButton setupButton(String name) {
		JButton button = new JButton(name);
		Font font = new Font("Arial Black", Font.BOLD, 18);
		button.setFont(font);
		button.setBorder(BorderFactory.createLineBorder(Color.white, 3));
		button.setUI(new BasicButtonUI());
		button.setBackground(Color.black);
		button.setForeground(Color.white);
		button.setVisible(true);
		return button;
	}

	public static void keyDown(String key) {
		if (key.equals("Escape")) {
			handler.canDraw(!handler.isDrawing());
			if (!handler.isDrawing()) {
				sound.pause();
				JButton button = setupButton("Continue");
				JButton settings = setupButton("Settings");
				JButton exit = setupButton("Exit");
				handler.addElement(button, (Window.getWidth() / 2) - 100, 100, 200, 40);
				handler.addElement(settings, (Window.getWidth() / 2) - 100, 300, 200, 40);
				handler.addElement(exit, (Window.getWidth() / 2) - 100, 500, 200, 40);
				button.addActionListener(e -> {
					handler.canDraw(!handler.isDrawing());
					handler.clearElements();
				});
				exit.addActionListener(e -> {System.exit(0);});
			} else {
				sound.play();
				handler.clearElements();
			}
		}
		if (handler.isDrawing()) {
			if (key.equals("Up") && layI < layer.length - 1)layI++;
			if (key.equals("Down") && layI > 0)layI--;
		}
	}

	public static void mouseMove(int x, int y) {
		player.lookAt(x, y);
		player.setMousePos(x, y);
	}

	public static void mouseDown(int x, int y, int button) {
		if (handler.isDrawing()) {
			if (button == 1 && layI == player.getLayer())
				bullets.add(new Bullet(2500, player.getEmpty(0), new Vector2D(x, y)).setMovement(12, 0));
			if (button == 3) {
				player.setNearWall(false);
				handler.onCollide("mouseInside", new Vector2D(x, y), Window.getBorder(50, 50));
				handler.onCollide("canTeleport", new Vector2D(x + 10, y), layer[layI].get("floor"));
			}
		}
	}

	public static void mouseInside(int x, int y) {
		player.setNearWall(true);
	}

	public static void touchWall(int x, int y) {
		if (Math.abs(x) > 5 || Math.abs(y) > 5)player.teleportStop();
		player.updatePos((player.getPos().getX() - x), (player.getPos().getY() - y));
	}

	public static void exit(int x, int y) {
		Vector2D pos = player.getPos();
		Camera2D.movePos(-x, -y);
		player.setLayer(layI);
		Camera2D.updateGroups(layer[layI].get("floor"), layer[layI].get("wall"));
		player.updatePos((pos.getX() - x), (pos.getY() - y));
		for (int i = 0; i < bullets.size(); i++)bullets.get(i).moveBullet(-x, -y);
	}

	public static void canTeleport(int x, int y) {
		if(player.getNearWall()&&player.getCooldown()==0) {
			for (int i = 0; i < bullets.size(); i++)
				bullets.get(i).moveBullet(player.getPos().getX() - player.getMousePos().getX(), 
						player.getPos().getY() - player.getMousePos().getY());
		}
		player.canTeleport(layer[layI],layI);
	}
	public static void update(Graphics g) {
		int speed = 3;
		if (handler.getKeyPressed("Shift"))speed = 6;
		if (handler.getKeyPressed("W"))player.updatePos(player.getPos().getX(), player.getPos().getY() - speed);
		if (handler.getKeyPressed("A"))player.updatePos(player.getPos().getX() - speed, player.getPos().getY());
		if (handler.getKeyPressed("D"))player.updatePos(player.getPos().getX() + speed, player.getPos().getY());
		if (handler.getKeyPressed("S"))player.updatePos(player.getPos().getX(), player.getPos().getY() + speed);
		handler.onCollide("touchWall", player, layer[layI].get("wall"));
		handler.onCollide("exit", player, Window.getBorder(50, 50));
		sound.updateQueue();
		layer[layI].draw(g);
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update();
			if (layI == player.getLayer())bullets.get(i).draw(g);
			Vector2D vec = bullets.get(i).getMovement();
			for (int j = 0; j < bullets.size(); j++) {
				if (bullets.get(i).onCollide(bullets.get(j).getObj()) && i != j) {
					bullets.get(i).addAngle(90);
					bullets.get(i).addAngle(-90);
				}
			}
			if (Math.abs(vec.getX()) < 1 && Math.abs(vec.getY()) < 1)bullets.remove(i);
			else if (bullets.get(i).onCollide(player) && bullets.get(i).getTick() >= 100) {
				bullets.clear();
				player.reset();
			} else if (bullets.get(i).onCollide(layer[layI].get("wall")))bullets.remove(i);
			else if (bullets.get(i).hasStopped())bullets.remove(i);
		}
		player.draw(g,layer[layI].get("wall"),layI);
		if (!handler.isDrawing()) {
			g.setColor(new Color(0, 0, 0, 0.75f));
			g.fillRect(0, 0, Window.getWidth(), Window.getHeight());
		}
	}
}