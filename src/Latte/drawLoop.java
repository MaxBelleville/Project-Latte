package Latte;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class drawLoop {
	private long lastLoopTime = System.nanoTime();
	private final int TARGET_FPS = 60;
	private final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
	private double fps=0;
	private double lastFpsTime=0;
	private Graphics g;
	private boolean running=true;
	private int width=0;
	private int height=0;
	private BufferedImage img=null;
	public static Caller caller;
	protected static void setCaller(Caller caller) {
		drawLoop.caller = caller;
	}
	protected drawLoop() {
		 String title= Window.titleLabel.getText();
		 while (running)
		   {
		      if(width!=Window.getWidth()||height!=Window.getHeight()) {
		    	  width=Window.getWidth();
		    	  height=Window.getHeight();
		    	  img = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		    	  g= img.getGraphics();
		    	  g.setColor(Color.white);
			      g.fillRect(0, 0, width, height);
		      }
		      long now = System.nanoTime();
		      long updateLength = now - lastLoopTime;
		      lastLoopTime = now;
		      double delta = updateLength / ((double)OPTIMAL_TIME);
		      lastFpsTime += updateLength;
		      fps++;
		      if (lastFpsTime >= 1000000000)
		      {
		         if(Window.displayFps)Window.titleLabel.setText(title+" - FPS: "+fps);
		         lastFpsTime = 0;
		         fps = 0;
		      }
		      g.setColor(new Color(1f,1f,1f,0.01f));//Still have no idea how this works.
		      g.fillRect(0, 0, width, height);
		      g.setColor(Color.white);
		      g.fillRect(0,0,width,height);
		      g.setColor(Color.black);
		      caller.call(g,delta);
		      caller.call(g);
		      Window.panel.update(img);
		      try{
		    	  Thread.sleep((lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000);
		    	 }
		      catch(Exception e) { }
		   }
	}
}