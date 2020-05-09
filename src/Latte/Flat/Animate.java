package Latte.Flat;

import java.awt.Color;
import java.awt.Graphics;

import Latte.Caller;

public class Animate {
	private Caller caller;
	private int endTicks = 500;
	private long start = 0;
	private int tick = 0;

	public Animate(int endTicks) {
		this.endTicks = endTicks;
	}
	
	public void setCaller(Caller caller) {
		start = System.nanoTime();
		tick = 0;
		this.caller = caller;
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
		if (tick <= endTicks && caller != null) {
			Graphics g2 = g.create();
			g2.setColor(Color.black);
			caller.call(g2, tick);
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
