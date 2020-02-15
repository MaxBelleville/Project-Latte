package Latte;

import java.awt.Graphics;

public class Handler {
	public Handler() {}
	
	public void draw(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Caller call = new Caller();
		call.getMethod(classStr, methodStr,Graphics.class, double.class);
		if(call.isEmpty()) call.getMethod(classStr, methodStr, Graphics.class);
		drawLoop.setCaller(call);
		Thread drawThread=new DrawThread();
		drawThread.start();
	}
	
	public void onMouseMove(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Listener.mouseMoveCaller.getMethod(classStr, methodStr,int.class, int.class);
	}
	public void onDelay(String methodStr, int delay) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Caller delayCaller=new Caller();
		delayCaller.getMethod(classStr, methodStr);
		Thread delayThread = new DelayThread(delayCaller,delay);
		delayThread.start();
	}
	
	public void onMouseDown(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Listener.mouseDownCaller.getMethod(classStr, methodStr,int.class, int.class,int.class);
	}
	
	public void onMouseUp(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Listener.mouseUpCaller.getMethod(classStr, methodStr,int.class, int.class,int.class);
	}
	
	public void onKeyDown(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Listener.keyDownCaller.getMethod(classStr, methodStr,String.class);	
	}
	
	public void onKeyUp(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Listener.keyUpCaller.getMethod(classStr, methodStr,String.class);	
	}
	
	public void onClose(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Listener.closeCaller.getMethod(classStr, methodStr,String.class);	
	}

	public void onMinimize(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Listener.minCaller.getMethod(classStr, methodStr,String.class);	
	}
	
	public void onMaximize(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Listener.maxCaller.getMethod(classStr, methodStr,String.class);	
	}
	
	public void onCollide(String methodStr, Object2D collider, Object2D collidee) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Caller collideCaller=new Caller();
		collideCaller.getMethod(classStr, methodStr,int.class,int.class);
		Vector2D vec=collidee.satDetection(collider);
		if(vec!=null)
			collideCaller.call((int)vec.getX(),(int)vec.getY());
	}
	public void onAvoid(String methodStr, Object2D collider, Object2D collidee) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Caller collideCaller=new Caller();
		collideCaller.getMethod(classStr, methodStr);
		Vector2D vec=collidee.satDetection(collider);
		if(vec==null)
			collideCaller.call();
	}
	public void onCollide(String methodStr, Object2D collider, Group2D group) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Caller collideCaller=new Caller();
		collideCaller.getMethod(classStr, methodStr,int.class,int.class);
		for (Object2D collidee: group.get()) {
		Vector2D vec=collidee.satDetection(collider);
		if(vec!=null)
			collideCaller.call((int)vec.getX(),(int)vec.getY());
		}
	}
	public void onAvoid(String methodStr, Object2D collider, Group2D group) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Caller collideCaller=new Caller();
		collideCaller.getMethod(classStr, methodStr);
		for (Object2D collidee: group.get()) {
		Vector2D vec=collidee.satDetection(collider);
		if(vec==null)
			collideCaller.call();
		}
	}
	public boolean getKeyPressed(String key) {
		if(Listener.isKeyPressed.containsKey(key))
			return Listener.isKeyPressed.get(key);
		return false;
	}
}
class DelayThread extends Thread {
	private int delay=0;
	private Caller caller;
	public DelayThread(Caller caller,int delay) {
		this.delay=delay;
		this.caller=caller;
	}
	public void run() {
		try {
			Thread.sleep(delay);
			caller.call();
		} catch (InterruptedException e) {}
	}
}
class DrawThread extends Thread {
	public void run() { new drawLoop(); }
}