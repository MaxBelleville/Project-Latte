package Latte;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class drawLoop {
	private long lastLoopTime = System.nanoTime();
	private final int TARGET_FPS = 60;
	private final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
	private double fps=0;
	private double lastFpsTime=0;
	private Graphics2D g;
	private static boolean running=true;
	private int width=0;
	private int height=0;
	private BufferedImage img=null;
	private static Caller caller;
	public static Caller animator;
	public static long animationTime=0;
	public static long endTime=0;
	protected static void pause(boolean canDraw) {
		running=canDraw;
		new drawLoop(canDraw);
	}
	protected static boolean isPaused() {
		return running;
	}
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
		    	  img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			      g= img.createGraphics();
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
			  if(delta<1) {
		      g.setBackground(new Color(255,255,255,0));
		      g.clearRect(0, 0, width, height);
		      g.setColor(Color.black);
		      caller.call(g,delta);
		      caller.call(g);
		      Window.panel.update(img);
			  }
		   }
	}
	public drawLoop(boolean canDraw) {
		if(!canDraw) {
			 width=Window.getWidth();
	    	  height=Window.getHeight();
	    	  img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			g= img.createGraphics();
			g.setColor(Window.windowBack);
		     g.fillRect(0, 0, (width), (height));
		     caller.call(g);
		     g.setColor(Color.black);
		     Window.panel.updateByRepaint(img);
		}
	}
}