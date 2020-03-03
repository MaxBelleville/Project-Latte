package Example;
import java.awt.Graphics;

import Latte.*;

public class Test {
	private static Handler handler;
	private static Object2D obj =new Object2D();
	public static void main(String[] args) {
		handler = new Window().displayFps().title("Project Supersoft")
				.hideMax().fullscreen().init();
		handler.draw("update");
		handler.onMouseMove("mouseMove");
		handler.onMouseDown("mouseDown");
		obj.addRect(0, 0, 20, 20);
	}
	public void mouseMove(int x, int y) {
		obj.lookAt(x, y);
	}
	public void mouseDown(int x, int y, int button) {
		if(button==3) {
			obj.updatePos(x-10, y-20);
		}
	}
	public void update(Graphics g) {
		if(handler.getKeyPressed("W"))obj.addPosPolar(5);
		if(handler.getKeyPressed("A"))obj.addPosPolar(0,3);
		if(handler.getKeyPressed("D"))obj.addPosPolar(0,-3);
		if(handler.getKeyPressed("S"))obj.addPosPolar(-5);
		obj.rotate();
		obj.fill(g);
	}
}