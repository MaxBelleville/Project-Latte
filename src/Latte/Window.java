package Latte;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;

import javax.swing.*;

public class Window {
	private String title="My Project";
	private String iconPath="";
	protected static JFrame jframe = new JFrame();
	protected static drawPanel panel;
	private Listener listener =new Listener();
	private int width=500;
	private int height=500;
	private boolean showMin=true;
	private boolean showMax=true;
	private boolean showMenu=true;
	private boolean fullscreen=false;
	
	public Window title(String title) {
		this.title=title;
		return this;
	}
	public Window size(int width,int height) {
		this.width=width;
		this.height=height;
		return this;
	}
	public Window icon(String iconPath) {
		this.iconPath = iconPath;
		return this;
	}
	public Window hideMinMax() {
		showMin =false;
		showMax =false;
		return this;
	}
	public Window hideMax() {
		showMax =false;
		return this;
	}
	public Window hideMin() {
		showMin =false;
		return this;
	}
	public Window hideMenu() {
		showMenu=false;
		return this;
	}
	
	public Window fullscreen() {
		fullscreen=true;
		return this;
	}
	public Handler init() {
		if (fullscreen) {
			Listener.state=Window.jframe.getExtendedState();
			jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		jframe.setSize(width, height);
		jframe.setUndecorated(true);
		jframe.addWindowListener(listener);
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(listener);
		jframe.addMouseListener(listener);
		jframe.addMouseMotionListener(listener);
		jframe.setLocationRelativeTo(null);
		setupMenu();
		panel = new drawPanel();
		jframe.setContentPane(panel);
		jframe.getJMenuBar().setVisible(showMenu);
		jframe.setVisible(true);
		return new Handler();
	}
	private JButton setupButton(String title, Color hoverColor) {
		JButton button = new JButton(title);
		button.setFocusable(false);
		button.setBorder(BorderFactory.createEmptyBorder(2, 15, 3, 15));
		button.setForeground(Color.white);
		button.setBackground(Color.black);
		button.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {button.setBackground(hoverColor);}
			public void mouseExited(java.awt.event.MouseEvent evt) {button.setBackground(Color.black);}
		});
		button.addActionListener(listener);
		return button;
	}
	
	private void setupMenu() {
		Image image = new ImageIcon(iconPath).getImage();
		ImageIcon scaledImg = new ImageIcon(image.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
		JLabel icon = new JLabel();
		icon.setIcon(scaledImg);
		JMenuBar menu = new JMenuBar();
		JLabel title = new JLabel(this.title);
		JButton minButton = setupButton("_",new Color(50, 150, 255));
		JButton maxButton = setupButton("■",new Color(50, 150, 255));
		JButton closeButton = setupButton("X",new Color(255, 0, 0));
		title.setForeground(Color.white);
		menu.setBackground(Color.black);
		menu.add(icon);
		menu.add(Box.createRigidArea(new Dimension(10, 10)));
		menu.add(title);
		menu.add(Box.createHorizontalGlue());
		if (showMin) menu.add(minButton);
		if (showMax) menu.add(maxButton);
		menu.add(closeButton);
		menu.setBorder(null);
		jframe.setJMenuBar(menu);	
	}
	
}