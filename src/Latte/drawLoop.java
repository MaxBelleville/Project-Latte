package Latte;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class drawLoop {
	private long lastLoopTime = System.nanoTime();
	private final int TARGET_FPS = 60;
	private final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
	private boolean running=true;
	private long lastFpsTime=0;
	private int fps=0;
	private int width=0;
	private int height=0;
	private BufferedImage img;
	public static Caller caller;
	
	protected static void setCaller(Caller caller) {
		drawLoop.caller = caller;
	}
	
	protected drawLoop() {
		 while (running)
		   {
		      long now = System.nanoTime();
		      long updateLength = now - lastLoopTime;
		      lastLoopTime = now;
		      double delta = updateLength / ((double)OPTIMAL_TIME);
		      lastFpsTime += updateLength;
		      fps++;
//		      if (lastFpsTime >= 1000000000)
//		      {
//		         System.out.println("(FPS: "+fps+")");
//		         lastFpsTime = 0;
//		         fps = 0;
//		      }
		      if(width!=Window.jframe.getWidth()||height!=Window.jframe.getHeight()) {
		    	  width=Window.jframe.getWidth();
		    	  height=Window.jframe.getHeight();
		    	  img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		      }
		      Graphics g= img.getGraphics();
		      g.fillRect(0, 0, width, height);
		      g.setColor(Color.black);
		      caller.call(g,delta);
		      caller.call(g);
		      Window.panel.update(img);
		      try{
		    	  Thread.sleep((lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000 );
		    	 }
		      catch(Exception e) { }
		   }
	}
}
