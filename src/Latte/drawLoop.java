package Latte;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import Latte.Frothy.Camera3;

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
	private static String title="";
	private BufferedImage img=null;
	private static BiConsumer<Graphics2D,Double> funcWithDelta;
	private static Consumer<Graphics2D> func;
	public static long animationTime=0;
	public static long endTime=0;
	protected static void pause(boolean canDraw) {
		running=canDraw;
	}
	protected static boolean isPaused() {
		return running;
	}
	protected static void setCaller(BiConsumer<Graphics2D,Double> func) {
		drawLoop.funcWithDelta=func;
	}
	protected static void setCaller(Consumer<Graphics2D> func) {
		drawLoop.func=func;
	}
	protected drawLoop() {
		if(title.isEmpty())title= Window.titleLabel.getText();
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
		      g.setColor(Window.jframe.getBackground());
		      g.clearRect(0,0,Window.getWidth(),Window.getHeight());
		      g.setColor(Color.black);
		      Camera3.clear();
		      if(funcWithDelta!=null)funcWithDelta.accept(g,delta);
		      if(func!=null)func.accept(g);
		      Window.panel.update(img);
		      try{Thread.sleep( (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000 );}
		      catch(Exception e) {}
		   }
		   if(funcWithDelta!=null)funcWithDelta.accept(g,0.0);
		   if(func!=null)func.accept(g);
		   Window.panel.update(img);
	}
}