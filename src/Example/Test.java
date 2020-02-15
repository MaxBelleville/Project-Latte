package Example;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;

import Latte.*;

public class Test {
	private static Handler handler;
	public static void main(String[] args) {
		handler = new Window().init();
		handler.draw("update");
	}
	public void update(Graphics g) {
		
	}
	
}