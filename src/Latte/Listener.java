package Latte;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class Listener implements ActionListener, WindowListener, KeyListener {
	
	protected static int state=JFrame.NORMAL;
	
	protected static Caller keyDownCaller = new Caller();
	protected static Caller keyUpCaller = new Caller();
	protected static Caller mouseDownCaller = new Caller();
	protected static Caller mouseMoveCaller = new Caller();
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		state=Window.jframe.getExtendedState();
		if(evt.getActionCommand().equals("X"))
			System.exit(0);
		else if(evt.getActionCommand().equals("_"))
			Window.jframe.setExtendedState(JFrame.ICONIFIED);
		else {
			if(Window.jframe.getExtendedState()==JFrame.NORMAL)
				Window.jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
			else Window.jframe.setExtendedState(JFrame.NORMAL);
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {
		if(state==JFrame.MAXIMIZED_BOTH) 
			Window.jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!keyDownCaller.isEmpty())
		keyDownCaller.call(e.getKeyText(e.getKeyCode()));
	}

	@Override
	public void keyReleased(KeyEvent e) {	
		if (!keyUpCaller.isEmpty())
			keyUpCaller.call(e.getKeyText(e.getKeyCode()));
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
