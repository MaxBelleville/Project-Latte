package Latte;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class drawPanel extends JPanel {

	private Image img;
	 public void paintComponent(Graphics g) {
		 super.paintComponent(g);
		 g.drawImage(img,0,0,Window.jframe.getWidth(), Window.jframe.getHeight(),null);
	   }
	 
	 public void update(BufferedImage img) {
		this.img=img;
		repaint();
	 }
}
