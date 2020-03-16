package Latte;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class drawPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage img;
	 public void paintComponent(Graphics g) {
		 super.paintComponent(g);
		 g.drawImage(img,0,0,Window.getWidth(),Window.getHeight(),null);
	   }
	 
	 public void update(BufferedImage img) {
		this.img=img;
		this.paintImmediately(0,0,Window.getWidth(),Window.getHeight());
	 }
	 public void updateByRepaint(BufferedImage img) {
			this.img=img;
			repaint();
		 }
}