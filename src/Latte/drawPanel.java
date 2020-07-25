package Latte;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

public class drawPanel extends JLayeredPane {
	private static final long serialVersionUID = 1L;
	private BufferedImage img;
	 public void paintComponent(Graphics g) {
		 super.paintComponent(g);
		 g.drawImage(img,0,0,Window.getWidth(),Window.getHeight(),null);
	   }
	 
	 public void update(BufferedImage img) {
		this.img=img;
		try {
			SwingUtilities.invokeAndWait(()->paintImmediately(0,0,Window.getWidth(),Window.getHeight()));
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
}