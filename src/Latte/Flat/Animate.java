package Latte.Flat;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.function.BiConsumer;

public class Animate {
	private BiConsumer<Graphics2D, Integer> func;
	private int endTicks = 500;
	private long start = 0;
	private int tick = 0;

	public Animate(int endTicks) {
		this.endTicks = endTicks;
	}
	
	public void setCaller(BiConsumer<Graphics2D, Integer> func) {
		start = System.nanoTime();
		tick = 0;
		this.func = func;
	}
	public void reset() {
		start = System.nanoTime();
		tick = 0;
	}
	public int manualDraw() {
		if(tick <= endTicks) {
			tick = (int) (System.nanoTime() - start) / 1000000;
			return tick;
		}
		return -1;
	}
	public void draw(Graphics g) {
		if (tick <= endTicks && func != null) {
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setColor(Color.black);
			func.accept(g2, tick);
			tick = (int) (System.nanoTime() - start) / 1000000;
		}
	}
	public boolean hasStopped() {
		return tick >= endTicks;
	}
	public void stop() {
		tick = endTicks + 1;
	}
}
