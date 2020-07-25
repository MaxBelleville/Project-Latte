package Latte;

import java.awt.Component;
import java.awt.Graphics2D;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.swing.JLayeredPane;

import Latte.Flat.Animate;
import Latte.Flat.Group;
import Latte.Flat.TriConsumer;
import Latte.Flat.Block;
import Latte.Flat.Vector;


public class Handler {
	public Handler() {
	}
	public static void draw(Consumer<Graphics2D> func) {
		drawLoop.setCaller(func);
		Thread t = new Thread(() -> { new drawLoop(); });
		t.start();
	}
	public static void draw(BiConsumer<Graphics2D,Double> func) {
		drawLoop.setCaller(func);
		Thread t = new Thread(() -> { new drawLoop(); });
		t.start();
	}
	public static boolean isDrawing() {
		return drawLoop.isPaused();
	}
	public static void canDraw(boolean canDraw) {
		drawLoop.pause(canDraw);
		Thread t = new Thread(() -> { new drawLoop(); });
		if(canDraw)t.start();
	}
	public static void animate(BiConsumer<Graphics2D,Integer> func, Animate animation) {
		animation.setCaller(func);
	}

	public static void onMouseMove(BiConsumer<Integer,Integer> func) {
		Listener.mouseMoveFunc = func;
	}
	public static void onLoopTillComplete(Runnable loop,Predicate<Boolean> condition) {
		Thread t = new Thread(() -> { 
				while(condition.test(true)) loop.run();
		});
		t.start();
	}
	public static void onLoopTillComplete(Runnable complete,Runnable loop,Predicate<Boolean> condition) {
		Thread t = new Thread(() -> { 
				while(condition.test(true)) loop.run();
				complete.run();
		});
		t.start();
	}

	public static void onLoopTillComplete(Predicate<Boolean> condition) {
		Thread t = new Thread(() -> { while(condition.test(true)); });
		t.start();
	}
	
	public static void onDelay(Runnable func, int delay) {
		Thread t = new Thread(() -> { 
			try {
				Thread.sleep(delay);
				func.run();
			} catch (InterruptedException e) {}
		});
		t.start();
	}

	public static void onMouseDown(TriConsumer<Integer, Integer, Integer> func) {
		Listener.mouseDownFunc = func;
	}

	public static void onMouseUp(TriConsumer<Integer, Integer, Integer> func) {
		Listener.mouseUpFunc =func;
	}

	public static void onKeyDown(Consumer<String> func) {
		Listener.keyDownFunc =func;
	}

	public static void onKeyUp(Consumer<String> func) {
		Listener.keyUpFunc =func;
	}

	public static void onClose(Runnable func) {
		Listener.closeFunc =func;
	}

	public static void onMinimize(Runnable func) {
		Listener.minFunc =func;
	}

	public static void onMaximize(Runnable func) {
		Listener.maxFunc =func;
	}
	public static Component getElement(String name) {
		for(Component c: Window.panel.getComponents()) {
			if(c.getName().equals(name)) return c;
		}
		return null;
	}
	public static void clearElements() {
		Window.panel.removeAll();
	}
	public static void removeElement(Component component) {
		Window.panel.remove(component);
	}
	public static void addElement(Component component, int x, int y, int w, int h) {
		component.setBounds(x,y,w,h);
		Window.panel.add(component,JLayeredPane.PALETTE_LAYER);
	}

	public static boolean getKeyPressed(String keys) {
		String keyArr[] = keys.split(",");
		for (String key : keyArr) {
			if (Listener.isKeyDown.containsKey(key))
				if(Listener.isKeyDown.get(key)) return true;
		}
		return false;
	}
	public static boolean getMousePressed(String buttons) {
		String buttonArr[] = buttons.split(",");
		for (String button : buttonArr) {
			int val=Integer.parseInt(button);
			if (Listener.isButtonDown.containsKey(val))
				if(Listener.isButtonDown.get(val)) return true;
		}
		return false;
	}
	public static void onCollide(Consumer<Block> func, Block collider, Block collidee) {
		Block block=collider.collideWith(collidee);
		if (block != null) func.accept(block);
	}
	public static void onCollide(Consumer<Block> func, Block collider, Group group) {
		Block block=collider.collideWith(group);
		if (block != null) func.accept(block);
	}
	public static void onCollide(Consumer<Block> func, Vector colliderPoint, Block collidee) {
		Block block= collidee.collideWith(colliderPoint);
		if (block != null) func.accept(block);
	}
	public static void onCollide(Consumer<Block> func, Vector colliderPoint, Group group) {
		Block block =Block.collideWith(group,colliderPoint);
		if (block != null) func.accept(block);
	}
	public static void onAvoid(Runnable func, Block collider, Block collidee) {
		Block block=collider.collideWith(collidee);
		if (block == null) func.run();
	}
	public static void onAvoid(Runnable func, Block collider, Group group) {
		Block block=collider.collideWith(group);
		if (block == null) func.run();
	}
	public static void onAvoid(Runnable func, Vector colliderPoint, Block collidee) {
		Block block = collidee.collideWith(colliderPoint);
		if (block == null) func.run();
	}
	public static void onAvoid(Runnable func, Vector colliderPoint, Group group) {
		Block block=Block.collideWith(group,colliderPoint);
		if (block == null) func.run();
	}
}
