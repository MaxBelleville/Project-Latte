package Example;
import java.awt.Color;
import java.awt.Graphics;

import Latte.*;

public class Test {
	private static Handler handler;
	private static Object2D obj =new Object2D();
	private static Vector2D vec = new Vector2D(5,5);
	public static void main(String[] args) {
		handler = new Window().displayFps().fullscreen().init();
		obj.addOval(50,50,20,20);
		handler.draw("update");
	}
	public void update(Graphics g) {
		obj.fill(g);
		obj.updatePos((int)obj.getPos().getX()+(int)vec.getX(),(int)obj.getPos().getY()+(int)vec.getY());
		obj.rotate(5);
		handler.onAvoid("Exit",obj,Window.getWalls());
	}
	public void Exit() {
		obj.updatePos((int)obj.getPos().getX()-(int)vec.getX(),(int)obj.getPos().getY()-(int)vec.getY());
		double x=((int)(Math.random() * 50)-25);
		double y=((int)(Math.random() * 50)-25);
		vec.setPos(x,y);
	}
}