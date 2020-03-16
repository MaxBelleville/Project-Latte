package Latte;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JComponent;

public class Handler {
	public Handler() {
	}

	public void draw(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Caller call = new Caller();
		call.getMethod(classStr, methodStr, Graphics.class, double.class);
		if (call.isEmpty())
			call.getMethod(classStr, methodStr, Graphics.class);
		drawLoop.setCaller(call);
		Thread drawThread = new DrawThread();
		drawThread.start();
	}
	public boolean isDrawing() {
		return drawLoop.isPaused();
	}
	public void canDraw(boolean canDraw) {
		drawLoop.pause(canDraw);
		Thread drawThread = new DrawThread();
		if(canDraw)drawThread.start();
	}
	public void animate(String methodStr, Animate2D animation) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Caller call = new Caller();
		call.getMethod(classStr, methodStr, Graphics.class, int.class);
		animation.setCaller(call);
	}

	public void onMouseMove(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Listener.mouseMoveCaller.getMethod(classStr, methodStr, int.class, int.class);
	}

	public void onDelay(String methodStr, int delay) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Caller delayCaller = new Caller();
		delayCaller.getMethod(classStr, methodStr);
		Thread delayThread = new DelayThread(delayCaller, delay);
		delayThread.start();
	}

	public void onMouseDown(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Listener.mouseDownCaller.getMethod(classStr, methodStr, int.class, int.class, int.class);
	}

	public void onMouseUp(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Listener.mouseUpCaller.getMethod(classStr, methodStr, int.class, int.class, int.class);
	}

	public void onKeyDown(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Listener.keyDownCaller.getMethod(classStr, methodStr, String.class);
	}

	public void onKeyUp(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Listener.keyUpCaller.getMethod(classStr, methodStr, String.class);
	}

	public void onClose(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Listener.closeCaller.getMethod(classStr, methodStr, String.class);
	}

	public void onMinimize(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Listener.minCaller.getMethod(classStr, methodStr, String.class);
	}

	public void onMaximize(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Listener.maxCaller.getMethod(classStr, methodStr, String.class);
	}

	public void onCollide(String methodStr, Vector2D colliderPoint, Object2D collidee) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Caller collideCaller = new Caller();
		collideCaller.getMethod(classStr, methodStr, int.class, int.class);
		Vector2D vec = collidee.collideWith(colliderPoint);
		if (vec != null)
			collideCaller.call((int) vec.getX(), (int) vec.getY());
	}
	public Component getElement(String name) {
		for(Component c: Window.panel.getComponents()) {
			if(c.getName().equals(name)) return c;
		}
		return null;
	}
	public void clearElements() {
		Window.panel.removeAll();
	}
	public void removeElement(Component component) {
		Window.panel.remove(component);
	}
	public void addElement(Component component, int x, int y, int w, int h) {
		((JComponent)component).setLayout(null);
		component.setBounds(x,y,w,h);
		Window.panel.add(component);
	}
		
	public void onCollide(String methodStr, Object2D collider, Object2D collidee) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Caller collideCaller = new Caller();
		collideCaller.getMethod(classStr, methodStr, int.class, int.class);
		Vector2D vec = collidee.collideWith(collider);
		if (vec != null)
			collideCaller.call((int) vec.getX(), (int) vec.getY());
	}

	public void onCollide(String methodStr, Vector2D colliderPoint, Group2D group) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Caller collideCaller = new Caller();
		collideCaller.getMethod(classStr, methodStr, int.class, int.class);
		Vector2D vec =Object2D.collideWith(group,colliderPoint);
		if (vec != null)
			collideCaller.call((int) vec.getX(), (int) vec.getY());
	}

	public void onCollide(String methodStr, Object2D collider, Group2D group) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Caller collideCaller = new Caller();
		collideCaller.getMethod(classStr, methodStr, int.class, int.class);
		Vector2D vec=collider.collideWith(group);
		if (vec != null)	
			collideCaller.call((int) vec.getX(), (int) vec.getY());
	}

	public void onAvoid(String methodStr, Object2D collider, Object2D collidee) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Caller collideCaller = new Caller();
		collideCaller.getMethod(classStr, methodStr);
		Vector2D vec = collidee.collideWith(collider);
		if (vec == null)
			collideCaller.call();
	}

	public void onAvoid(String methodStr, Vector2D colliderPoint, Object2D collidee) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Caller collideCaller = new Caller();
		collideCaller.getMethod(classStr, methodStr);
		Vector2D vec = collidee.collideWith(colliderPoint);
		if (vec == null)
			collideCaller.call();
	}

	public void onAvoid(String methodStr, Vector2D colliderPoint, Group2D group) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Caller collideCaller = new Caller();
		collideCaller.getMethod(classStr, methodStr);
		Vector2D vec =Object2D.collideWith(group,colliderPoint);
		if (vec == null)
			collideCaller.call();
	}

	public void onAvoid(String methodStr, Object2D collider, Group2D group) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Caller collideCaller = new Caller();
		collideCaller.getMethod(classStr, methodStr);
		Vector2D vec =collider.collideWith(group);
		if (vec == null)
			collideCaller.call();
	}

	public boolean getKeyPressed(String key) {
		String keys[] = key.split(",");
		for (String k : keys) {
			if (Listener.isKeyDown.containsKey(k))
				return Listener.isKeyDown.get(k);
		}
		return false;
	}
}

class DelayThread extends Thread {
	private int delay = 0;
	private Caller caller;

	public DelayThread(Caller caller, int delay) {
		this.delay = delay;
		this.caller = caller;
	}

	public void run() {
		try {
			Thread.sleep(delay);
			caller.call();
		} catch (InterruptedException e) {
		}
	}
}

class DrawThread extends Thread {
	public void run() {
		new drawLoop();
	}
}