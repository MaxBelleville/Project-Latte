package Example;

import java.awt.Color;
import java.awt.Graphics;

import Latte.*;

public class Test {
	private static int x=0;
	private static int y=0;
	private static int dirX=0;
	private static int dirY=0;
	private static int mouseX=0;
	private static int mouseY=0;
	private static Color color=Color.red;
	public static void main(String[] args) {
		Handler handler = new Window().fullscreen().hideMinMax().init();
		handler.draw("update");
		handler.onKeyDown("keyPress");
		handler.onKeyUp("keyRelease");
		handler.onMouseMove("mouseMove");
		handler.onMouseDown("mouseDown");
		handler.onMouseUp("mouseUp");
	}
	
	public void mouseDown(int x, int y, int button) {
		color=Color.green;
	}
	public void mouseUp(int x, int y, int button) {
		color=Color.red;
	}
	
	public void mouseMove(int x, int y) {
		mouseX=x;
		mouseY=y;
	}
	
	public void update(Graphics g) {
		x+=dirX;
		y+=dirY;
		g.setColor(Color.black);
		g.fillRect(x, y, 20,20);
		g.setColor(color);
		g.fillRect(mouseX-10, mouseY-30, 20,20);
	}
	
	public void keyRelease(String key) {
		if (key.equals("D")||key.equals("A"))dirX=0;
		if (key.equals("W")||key.equals("S"))dirY=0;
	}
	
	public void keyPress(String key) {
		if (key.equals("D"))dirX=3;
		if (key.equals("A"))dirX=-3;
		if (key.equals("W"))dirY=-3;
		if (key.equals("S"))dirY=3;
	}
}
