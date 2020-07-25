package Latte;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.LineBorder;

import Latte.Flat.Group;
import Latte.Frothy.Camera3;
import Latte.Flat.Block;


public class Window {
	private String title="My Project";
	private String iconPath="";
	protected static JLabel titleLabel;
	protected static boolean displayFps=false;
	protected static JFrame jframe = new JFrame();
	protected static drawPanel panel;
	protected static JLayeredPane mainPanel = new JLayeredPane();
	private Listener listener =new Listener();
	private Color closeColor=new Color(255, 0, 0);
	private Color buttonColor=new Color(50, 150, 255);
	private Color textColor =Color.white;
	private Color background=Color.black;
	protected static Color windowBack=Color.white;
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
	public static int getWidth() {
		return jframe.getWidth();
	}
	public static int getHeight() {
		return jframe.getHeight();
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
	public void init() {
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
		jframe.addFocusListener(listener);
		jframe.setLocationRelativeTo(null);
		jframe.setBackground(windowBack);
		panel = new drawPanel();
		panel.setBorder(new LineBorder(new Color(0,0,0,0.5f),1));
		jframe.setContentPane(panel);
		setupMenu();
		jframe.getJMenuBar().setVisible(showMenu);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true);
		Camera3.setupProjection();
	}
	public Window hideCursor() {
		jframe.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB ),
                new Point(),
                null));
		return this;
	}
	public Window setCursor(Cursor cur) {
		jframe.setCursor(cur);
		return this;
	}
	public static void lockMouseCenter() {
		Robot bot;
		if(Listener.isFocused) {
		try {
			bot = new Robot();
			bot.mouseMove(jframe.getWidth()/2,jframe.getHeight()/2);
		} catch (AWTException e) {}
		}
	}
	private JButton setupButton(String title, Color hoverColor) {
		JButton button = new JButton(title);
		button.setFocusable(false);
		button.setBorder(BorderFactory.createEmptyBorder(2, 15, 3, 15));
		button.setForeground(textColor);
		button.setBackground(background);
		button.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {button.setBackground(hoverColor);}
			public void mouseExited(java.awt.event.MouseEvent evt) {button.setBackground(Color.black);}
		});
		button.addActionListener(listener);
		return button;
	}
	
	private void setupMenu() {
		Image image = new ImageIcon(iconPath).getImage();
		jframe.setIconImage(image);
		ImageIcon scaledImg = new ImageIcon(image.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
		JLabel icon = new JLabel();
		icon.setIcon(scaledImg);
		JMenuBar menu = new JMenuBar();
		titleLabel = new JLabel(this.title);
		JButton minButton = setupButton("_",buttonColor);
		JButton maxButton = setupButton("[=]",buttonColor);
		JButton closeButton = setupButton("X",closeColor);
		titleLabel.setForeground(textColor);
		menu.setBackground(background);
		menu.add(icon);
		menu.add(Box.createRigidArea(new Dimension(10, 10)));
		menu.add(titleLabel);
		menu.add(Box.createHorizontalGlue());
		if (showMin) menu.add(minButton);
		if (showMax) menu.add(maxButton);
		menu.add(closeButton);
		menu.setBorder(null);
		jframe.setJMenuBar(menu);	
	}
	public Window displayFps() {
		displayFps=true;
		return this;
	}
	public static Block getWindowBox() {
		return new Block().addRect(0,0,getWidth(),getHeight());
	}
	public static Group getBorder(int dx, int dy) {
		Group group = new Group();
		group.add(new Block().addRect(0,0,dx,getHeight()));
		group.add(new Block().addRect(0,0,getWidth(),dy));
		group.add(new Block().addRect(0,getHeight()-dy,getWidth(),dy));
		group.add(new Block().addRect(getWidth()-dy,0,dx,getHeight()));
		return group;
	}
	public Window setBackground(Color color) {
		windowBack=color;
		return this;
	}
}