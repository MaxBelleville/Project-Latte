package air;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicButtonUI;

import Latte.*;
import Latte.Flat.*;

public class GameMain {
	private static Player player;
	private static int layI = 0;
	private static boolean reading=false;
	private static Sound sound = new Sound("Music/1.mp3");
	private static ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private static ArrayList<Layer> layers = new ArrayList<Layer>();
	private static Enemy enemy = new Enemy(true);
	
	public GameMain() {
		Handler.clearElements();
		player = new Player();
		Handler.onMouseMove(GameMain::mouseMove);
		Handler.onMouseDown(GameMain::mouseDown);
		Handler.onKeyDown(GameMain::keyDown);
		IO file = new IO();
		file.readPlain("map1.amap");
		while(file.hasNext()) {
			String[] split =file.loadNext().split("; ");
			Layer layer =new Layer();
			layer.setHidden(split[0].endsWith("true"));
			layer.load(split[1]);
			layers.add(layer);
		}
		layers.get(0).setupNav(new Vector(15,6),new Vector(1984,864));
		Vector playerVec=layers.get(0).get("Entites").get(0).getOrgPoint(0);
		player.setSpawn(playerVec);
		Handler.draw(GameMain::update);
		for(int i=2;i<16;i++)
			sound.addToQueue("Music/"+i+".mp3");
		sound.randomize();
		sound.setVolume(0.1);
		sound.play();
	}
	
	public static void main(String[] args) {
		new Window().setBackground(Color.black).size(500,500).icon("bullet.png").init();
		startMenu();
	}
	private static void startMenu() {
		JButton button = setupButton("Start");
		JButton settings = setupButton("Settings");
		JButton exit = setupButton("Exit");
		Handler.addElement(button, (Window.getWidth() / 2) - 100, 100, 200, 40);
		Handler.addElement(settings, (Window.getWidth() / 2) - 100, (Window.getHeight() / 2), 200, 40);
		Handler.addElement(exit, (Window.getWidth() / 2) - 100, (Window.getHeight())-100, 200, 40);
		button.addActionListener(e -> {
			if(!Server.serverExists("192.168.50.206",8000))Server.start(8000);
			Client.start();
			Client.connect("192.168.50.206",8000);
			new GameMain();
		});
		exit.addActionListener(e -> {System.exit(0);});
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
			Handler.canDraw(!Handler.isDrawing());
			if (!Handler.isDrawing()) {
				JButton button = setupButton("Continue");
				JButton settings = setupButton("Settings");
				JButton exit = setupButton("Exit");
				Handler.addElement(button, (Window.getWidth() / 2) - 100, 100, 200, 40);
				Handler.addElement(settings, (Window.getWidth() / 2) - 100, (Window.getHeight() / 2), 200, 40);
				Handler.addElement(exit, (Window.getWidth() / 2) - 100, (Window.getHeight())-100, 200, 40);
				button.addActionListener(e -> {
					Handler.canDraw(!Handler.isDrawing());
					sound.play();
					Handler.clearElements();
				});
				exit.addActionListener(e -> {System.exit(0);});
			} else {
				sound.play();
				Handler.clearElements();
			}
		}
		if (Handler.isDrawing()) {
			if (key.equals("Up") && layI < layers.size() - 1) {
				if(!layers.get(layI+1).isHidden())layI++;
				else if(layI < layers.size() - 2)layI+=2;
			}
			if (key.equals("Down") && layI > 0) {
				if(!layers.get(layI-1).isHidden())layI--;
				else if(layI >-1)layI-=2;
			}
		}
	}

	public static void mouseMove(int x, int y) {
		player.lookAt(x, y);
		player.setMousePos(x, y);
	}

	public static void mouseDown(int x, int y, int button) {
		if (Handler.isDrawing()) {
			if (button == 1 && layI == player.getLayer()) {
				Client.send("2500 "+player.getEmpty(0)+" "+new Vector(x, y)+" 8 0");
			}
			if (button == 3) {
				player.setNearWall(false);
				player.setNearEdge(false);
				Block tempBlock=new Block().addRect(x-10,y-10,20,20);
				if(layers.get(layI).get("Wall")!=null)Handler.onCollide(GameMain::stopTeleport, tempBlock, layers.get(layI).get("Wall"));
				Handler.onCollide(GameMain::mouseInside, tempBlock, Window.getBorder(60, 60));
				Handler.onCollide(GameMain::canTeleport, tempBlock, layers.get(layI).get("Floor"));
			}
		}
	}
	public static void stopTeleport(Block wall) {
		player.setNearWall(true);
	}
	public static void mouseInside(Block border) {
		player.setNearEdge(true);
	}

	public static void touchWall(Block wall) {
		double x= wall.getDisplacement().getX();
		double y= wall.getDisplacement().getY();
		Vector disp=player.getDisplacement();
		if (Math.abs(x) > 5 || Math.abs(y) > 5)player.teleportStop();
		player.updatePos((player.getPos().getX() - disp.getX()), (player.getPos().getY() - disp.getY()));
	}
	
	public static void exit(Block border) {
		Vector disp=player.getDisplacement();
		Vector pos = player.getPos();
		Camera.movePos(-disp.getX(), -disp.getY());
		player.setLayer(layI);
		player.updatePos((pos.getX() - disp.getX()), (pos.getY() - disp.getY()));
		for (int i = 0; i < bullets.size(); i++)bullets.get(i).moveBullet(-disp.getX(), -disp.getY());
	}
	public static void touchEntity(Block entity) {
		if(entity.getColor()==Color.orange) {
			layI++;
			player.setLayer(layI);
		}
		if(entity.getColor()==Color.blue) {
			layI--;
			player.setLayer(layI);
		}
	}
	public static void canTeleport(Block floor) {
		if(player.getNearEdge()&&player.getCooldown()==0) {
			for (int i = 0; i < bullets.size(); i++)
				bullets.get(i).moveBullet(player.getPos().getX() - player.getMousePos().getX(), 
						player.getPos().getY() - player.getMousePos().getY());
		}
		player.canTeleport(layI);
	}
	public static void update(Graphics g,double delta) {
		double speed = 3*delta;
		if (Handler.getKeyPressed("Shift"))speed = 4*delta;
		if (Handler.getKeyPressed("W"))player.updatePos(player.getPos().getX(), player.getPos().getY() - speed);
		if (Handler.getKeyPressed("A"))player.updatePos(player.getPos().getX() - speed, player.getPos().getY());
		if (Handler.getKeyPressed("D"))player.updatePos(player.getPos().getX() + speed, player.getPos().getY());
		if (Handler.getKeyPressed("S"))player.updatePos(player.getPos().getX(), player.getPos().getY() + speed);
		if(layers.get(layI).get("Wall")!=null)Handler.onCollide(GameMain::touchWall, player, layers.get(layI).get("Wall"));
		if(layers.get(layI).get("Entites")!=null)Handler.onCollide(GameMain::touchEntity,player, layers.get(layI).get("Entites"));
		Handler.onCollide(GameMain::exit, player, Window.getBorder(60, 60));
		sound.updateQueue();
		sound.setVolume(0.1);
		layers.get(layI).draw(g);
		end: for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update(delta);
			if (layI == player.getLayer())bullets.get(i).draw(g);
			Vector vec = bullets.get(i).getMovement();
			for (int j = 0; j < bullets.size(); j++) {
				if (bullets.get(i).onCollide(bullets.get(j).getObj()) && i != j) {
					bullets.get(i).addAngle(20);
					bullets.get(j).addAngle(-20);
					break end;
				}
			}
			if (Math.abs(vec.getX()) < 1 && Math.abs(vec.getY()) < 1)bullets.remove(i);
			else if (bullets.get(i).onCollide(player) && bullets.get(i).getTick() >= 100) {
				bullets.clear();
				player.reset();
				System.out.println("Death");
				Camera.setPos(0,0);
			} else if (bullets.get(i).onCollide(layers.get(layI).get("Wall")))bullets.remove(i);
			else if(bullets.get(i).onCollide(enemy) && bullets.get(i).getTick() >= 100) {
				System.out.println("Kill");
				bullets.remove(i);
			}
			else if (bullets.get(i).hasStopped())bullets.remove(i);
		}
		g.setColor(Color.black);
		enemy.update(layers,player.getPos(),bullets);
		enemy.fill(g);
		player.draw(g,layers.get(layI).get("Wall"),layI);
		if (!Handler.isDrawing()) {
			sound.pause();
			g.setColor(new Color(0, 0, 0, 0.5f));
			g.fillRect(0, 0, Window.getWidth(), Window.getHeight());
		}
		if(Client.hasData()&&!reading) {
			reading=true;
			Handler.onLoopTillComplete(GameMain::finishedReading,GameMain::readServer, p->(Client.hasData()==p));
		}
	}
	
	public static void finishedReading() {
		reading=false;
	}
	
	public static void readServer() {
		Vector vec = player.getPoint(0);
		enemy.updateMP(vec.toString());
		if(Bullet.check())
			bullets.add(new Bullet());
	}
}