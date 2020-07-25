package Latte;

import java.awt.KeyEventDispatcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.JFrame;

import Latte.Flat.TriConsumer;
import Latte.Flat.Vector;

public class Listener
		implements ActionListener, WindowListener, KeyEventDispatcher, 
		MouseListener, MouseMotionListener, FocusListener {

	protected static int state = JFrame.NORMAL;
	protected static boolean isFocused=false;
	protected static Runnable closeFunc;
	protected static Runnable minFunc;
	protected static Runnable maxFunc;
	protected static Consumer<String> keyDownFunc;
	protected static Consumer<String> keyUpFunc;
	protected static TriConsumer<Integer,Integer,Integer> mouseDownFunc;
	protected static TriConsumer<Integer,Integer,Integer> mouseUpFunc;
	protected static BiConsumer<Integer,Integer> mouseMoveFunc;
	protected static Vector mousePos = new Vector(0, 0);
	protected static HashMap<String, Boolean> isKeyDown = new HashMap<String, Boolean>();
	protected static HashMap<Integer, Boolean> isButtonDown = new HashMap<Integer, Boolean>();

	@Override
	public void actionPerformed(ActionEvent evt) {
		state = Window.jframe.getExtendedState();

		if (closeFunc!=null && evt.getActionCommand().equals("X"))
			closeFunc.run();
		else if (evt.getActionCommand().equals("X"))
			System.exit(0);
		else if (minFunc!=null && evt.getActionCommand().equals("_"))
			minFunc.run();
		else if (evt.getActionCommand().equals("_"))
			Window.jframe.setExtendedState(JFrame.ICONIFIED);
		else if (maxFunc!=null) maxFunc.run();
		else {
			if (Window.jframe.getExtendedState() == JFrame.NORMAL)
				Window.jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
			else Window.jframe.setExtendedState(JFrame.NORMAL);
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		if (state == JFrame.MAXIMIZED_BOTH)
			Window.jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		moveWindow(e.getXOnScreen(), e.getYOnScreen());
		if (mouseMoveFunc!=null) mouseMoveFunc.accept(e.getX(), e.getY());
	}

	private void moveWindow(int x, int y) {
		if (!(mousePos.getX() == 0 && mousePos.getY() == 0)) {
			Window.jframe.setLocation(x - (int) mousePos.getX(), y - (int) mousePos.getY());
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (mouseMoveFunc!=null) mouseMoveFunc.accept(e.getX(), e.getY());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		isButtonDown.put(e.getButton(),true);
		setMousePos(e.getXOnScreen(), e.getYOnScreen(), e.getX(), e.getY());
		if (mouseDownFunc!=null) mouseDownFunc.accept(e.getX(), e.getY(), e.getButton());
	}

	private void setMousePos(int x, int y, int currX, int currY) {
		int maxX = Window.getWidth() + Window.jframe.getX();
		int maxY = Window.getHeight() + Window.jframe.getY();
		int minX = Window.jframe.getX();
		int minY = Window.jframe.getY();
		if (Window.jframe.getExtendedState() == JFrame.NORMAL
				&& (x < minX + 10 || x > maxX - 10 || y < minY + 10 || y > maxY - 10))
			mousePos.setPos(currX, currY);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		isButtonDown.put(e.getButton(),false);
		mousePos.setPos(0, 0);
		if (mouseUpFunc!=null) mouseUpFunc.accept(e.getX(), e.getY(), e.getButton());
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		isKeyDown.put(KeyEvent.getKeyText(e.getKeyCode()), (e.getID() == KeyEvent.KEY_PRESSED) ? true : false);
		if (e.getID() == KeyEvent.KEY_PRESSED) {
			if (keyDownFunc!=null)
				keyDownFunc.accept(KeyEvent.getKeyText(e.getKeyCode()));
		} else {
			if (keyUpFunc!=null)
				keyUpFunc.accept(KeyEvent.getKeyText(e.getKeyCode()));
		}
		return false;
	}

	@Override
	public void focusGained(FocusEvent e) {
		isFocused=true;
	}

	@Override
	public void focusLost(FocusEvent e) {
		isFocused=false;
	}
}