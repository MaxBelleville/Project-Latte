package ThreeDTest;

import java.awt.Color;
import java.awt.Graphics;

import Latte.Handler;
import Latte.Window;
import Latte.Flat.Vector;
import Latte.Frothy.Camera3;
import Latte.Frothy.Mesh;
import Latte.Frothy.Vector3;

public class TestMain {
	private static Handler handler;
	private static Mesh mesh= new Mesh();
	public static void main(String[] args) {
		new Window().hideMenu().hideCursor().fullscreen().init();
		Handler.draw(TestMain::update);
		Handler.onMouseMove(TestMain::mouseMove);
		mesh.addOBJ("teapot.obj","metal.png");
	}
	public static void mouseMove(int x, int y) {
		int diffX = Window.getWidth()/2-x;
		int diffY = Window.getHeight()/2-y;
		Vector vec=new Vector(diffX,diffY).normalize();
		if(vec.getX()>0)Camera3.addYaw(-0.04);
		if(vec.getX()<0)Camera3.addYaw(0.04);
		if(vec.getY()>0)Camera3.addPitch(-0.02);
		if(vec.getY()<0)Camera3.addPitch(0.02);
	}
	
	public static void update(Graphics g,double delta) {
		Window.lockMouseCenter();
		keyDown();
		g.setColor(Color.white);
		mesh.fill(g,delta);
	}
	public static void keyDown() {
		double val=0.1;
		Vector3 forward = Camera3.getForward(val);
		Vector3 right = new Vector3(0,1,0).crossProd(forward);
		if(Handler.getKeyPressed("W"))Camera3.addPos(forward.getX(),forward.getY(),forward.getZ());
		if(Handler.getKeyPressed("S"))Camera3.addPos(-forward.getX(),-forward.getY(),-forward.getZ());
		if(Handler.getKeyPressed("A"))Camera3.addPos(right.getX(),right.getY(),right.getZ());
		if(Handler.getKeyPressed("D"))Camera3.addPos(-right.getX(),-right.getY(),-right.getZ());
	}

}
