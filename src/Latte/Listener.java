package Latte;

import java.awt.KeyEventDispatcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;

import javax.swing.JFrame;

import Latte.Flat.Vector;

public class Listener
		implements ActionListener, WindowListener, KeyEventDispatcher, MouseListener, MouseMotionListener {

	protected static int state = JFrame.NORMAL;

	protected static Caller closeCaller = new Caller();
	protected static Caller minCaller = new Caller();
	protected static Caller maxCaller = new Caller();
	protected static Caller keyDownCaller = new Caller();
	protected static Caller keyUpCaller = new Caller();
	protected static Caller mouseDownCaller = new Caller();
	protected static Caller mouseUpCaller = new Caller();
	protected static Caller mouseMoveCaller = new Caller();
	protected static Vector mousePos = new Vector(0, 0);
	protected static HashMap<String, Boolean> isKeyDown = new HashMap<String, Boolean>();
	protected static HashMap<String, Boolean> isMouseDown = new HashMap<String, Boolean>();

	@Override
	public void actionPerformed(ActionEvent evt) {
		state = Window.jframe.getExtendedState();

		if (!closeCaller.isEmpty() && evt.getActionCommand().equals("X"))
			closeCaller.call();
		else if (evt.getActionCommand().equals("X"))
			System.exit(0);
		else if (!minCaller.isEmpty() && evt.getActionCommand().equals("_"))
			minCaller.call();
		else if (evt.getActionCommand().equals("_"))
			Window.jframe.setExtendedState(JFrame.ICONIFIED);
		else if (!maxCaller.isEmpty())
			maxCaller.call();
		else {
			if (Window.jframe.getExtendedState() == JFrame.NORMAL)
				Window.jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
			else
				Window.jframe.setExtendedState(JFrame.NORMAL);
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
		if (!mouseMoveCaller.isEmpty())
			mouseMoveCaller.call(e.getX(), e.getY());
	}

	private void moveWindow(int x, int y) {
		if (!(mousePos.getX() == 0 && mousePos.getY() == 0)) {
			Window.jframe.setLocation(x - (int) mousePos.getX(), y - (int) mousePos.getY());
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!mouseMoveCaller.isEmpty())
			mouseMoveCaller.call(e.getX(), e.getY());
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
		setMousePos(e.getXOnScreen(), e.getYOnScreen(), e.getX(), e.getY());
		if (!mouseDownCaller.isEmpty())
			mouseDownCaller.call(e.getX(), e.getY(), e.getButton());
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
		mousePos.setPos(0, 0);
		if (!mouseUpCaller.isEmpty())
			mouseUpCaller.call(e.getX(), e.getY(), e.getButton());
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		isKeyDown.put(KeyEvent.getKeyText(e.getKeyCode()), (e.getID() == KeyEvent.KEY_PRESSED) ? true : false);
		if (e.getID() == KeyEvent.KEY_PRESSED) {
			if (!keyDownCaller.isEmpty())
				keyDownCaller.call(KeyEvent.getKeyText(e.getKeyCode()));
		} else {
			if (!keyUpCaller.isEmpty())
				keyUpCaller.call(KeyEvent.getKeyText(e.getKeyCode()));
		}
		return false;
	}
}