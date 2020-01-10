package Latte;

import java.awt.Graphics;

public class Handler {
	private static Thread drawThread;
	protected Handler() {}
	
	public void draw(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Caller call = new Caller();
		call.getMethod(classStr, methodStr,Graphics.class, double.class);
		if(call.isEmpty()) call.getMethod(classStr, methodStr, Graphics.class);
		drawLoop.setCaller(call);
		drawThread=new Mutithread();
		drawThread.start();
	}
	
	public void onMouseMove(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Listener.mouseMoveCaller.getMethod(classStr, methodStr,int.class, int.class);
	}
	public void onMouseDown(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Listener.mouseDownCaller.getMethod(classStr, methodStr,int.class, int.class,int.class);
		
	}
	public void onKeyDown(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Listener.keyDownCaller.getMethod(classStr, methodStr,String.class);	
	}
	public void onKeyUp(String methodStr) {
		String classStr = new Exception().getStackTrace()[1].getClassName();
		Listener.keyUpCaller.getMethod(classStr, methodStr,String.class);	
	}
}

class Mutithread extends Thread {
	public void run() { new drawLoop(); }
}