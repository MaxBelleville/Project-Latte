package Example;

import java.awt.Color;
import java.awt.Graphics;

import Latte.*;

public class Test {
	private static int x=0;
	private static int y=0;
	private static int dirX=0;
	private static int dirY=0;
	public static void main(String[] args) {
		Handler handler = new Window().hideMinMax().init();
		handler.draw("update");
		handler.onKeyDown("keyPress");
		handler.onKeyUp("keyRelease");
	}

	public void update(Graphics g) {
		g.fillRect(x, y, 20,20);
		x+=dirX;
		y+=dirY;
	}
	
	public void keyRelease(String key) {
		if (key.equals("D")||key.equals("A"))
			dirX=0;
		if (key.equals("W")||key.equals("S"))
			dirY=0;
	}
	
	public void keyPress(String key) {
		if (key.equals("D"))
			dirX=3;
		if (key.equals("A"))
			dirX=-3;
		if (key.equals("W"))
			dirY=-3;
		if (key.equals("S"))
			dirY=3;
	}
}
