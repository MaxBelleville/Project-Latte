package level;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicCheckBoxUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI;

import Latte.Handler;
import Latte.IO;
import Latte.Window;
import Latte.Flat.Block;
import Latte.Flat.Camera;
import Latte.Flat.Vector;

public class EditorMain {
	private static int moveSpeed=4;
	private static Vector mousePos=new Vector();
	private static ArrayList<Layer> layers = new ArrayList<>();
	private static String imgSelected="";
	private static String type="Floor";
	private static Vector snapPos=new Vector(32,32);
	private static Vector markPos=new Vector(0,0);
	private static Vector mouseClick=new Vector();
	private static boolean shouldRemove=false;
	private static enum EntityType {
		None,Player,Enemey,StairDown,StairUp
	}
	private static EntityType entity=EntityType.None;
	private static boolean snap=true;
	private static boolean stack=false;
	private static int rotation=0;
	private static JLabel selectedName= new JLabel();
	private static JLabel selectedType= new JLabel();
	private static JComboBox<Integer> list = new JComboBox<Integer>();
	private EditorMain() {
		list.addItem(0);
		list.setSelectedIndex(0);
		setupUI();
		Handler.draw(EditorMain::draw);
		Camera.setPos(350,64);
		Handler.onKeyDown(EditorMain::keyDown);
		Handler.onMouseMove(EditorMain::mouseMove);
		layers.add(new Layer());
	}

	public static void main(String[] args) {
		new Window().hideMenu().fullscreen().icon("bullet.png").init();
		new EditorMain();
	}
	private static void keyDown(String key) {
		if (key.equals("Equals")&&Handler.getKeyPressed("Shift")) moveSpeed++; 
		if (key.equals("Minus")) moveSpeed--;
		if (key.equals("Escape")) System.exit(0);
		if(key.equals("Q")) rotation+=90;
		if(key.equals("E")) rotation-=90;
		if(key.equals("M")) markPos.setPos(mouseClick.getX(),mouseClick.getY());
		if(snap&&!imgSelected.isEmpty()&&entity==EntityType.None) {
			if(key.equals("Up")) mouseClick=mouseClick.sub(new Vector(0,snapPos.getY()));
			if(key.equals("Down")) mouseClick=mouseClick.add(new Vector(0,snapPos.getY()));
			if(key.equals("Left")) mouseClick=mouseClick.sub(new Vector(snapPos.getX(),0));
			if(key.equals("Right")) mouseClick=mouseClick.add(new Vector(snapPos.getX(),0));
			if(key.equals("Up")||key.equals("Right")||key.equals("Left")||key.equals("Down")) {
				int x=(int)mouseClick.getX();
				int y=(int)mouseClick.getY();
				int i =list.getSelectedIndex();
				Block block=new Block().addRectImage("assets/"+imgSelected,x,y).setAngle(rotation);
				if(!shouldRemove)layers.get(i).add(type,block);
				if(shouldRemove)layers.get(i).remove(type,block);
			}
		}
	}
	private static void mouseMove(int x, int y) {mousePos=new Vector(x,y);}
	
	private static void moveCamera(Block border) {
		if(Handler.getKeyPressed("Shift")) {
			if(mousePos.getX()<20)Camera.movePos(moveSpeed,0);
			if(mousePos.getY()<20)Camera.movePos(0,moveSpeed);
			if(mousePos.getX()>Window.getWidth()-20)Camera.movePos(-moveSpeed,0);
			if(mousePos.getY()>Window.getHeight()-20)Camera.movePos(0,-moveSpeed);
		}
	}
	private void setupUI() {
		JTabbedPane tp = new JTabbedPane();
		JPanel panel= setupProperties();
		JCheckBox snapBox= new JCheckBox("Snapping");
		JCheckBox stackBox = new JCheckBox("Stacked");
		JTextField xField = new JTextField("32");
		JTextField yField = new JTextField("32");
		snapBox.setBackground(Color.black);
		stackBox.setBackground(Color.black);
		snapBox.setForeground(Color.white);
		stackBox.setForeground(Color.white);
		xField.setBackground(Color.black);
		yField.setBackground(Color.black);
		xField.setForeground(Color.white);
		yField.setForeground(Color.white);
		snapBox.setSelected(true);
		tp.setUI(new WindowsTabbedPaneUI());
		JPanel floor=loadPanel("floor.textures");
		JPanel stair=loadPanel("stair.textures");
		JPanel wall=loadPanel("wall.textures");
		JPanel entites=loadEntites();
		JPanel clickables=loadPanel("stair.textures");
		JPanel decoration=loadPanel("stair.textures");
		tp.add("Floor",floor);
		tp.add("Stair",stair);
		tp.add("Wall",wall);
		tp.add("Entites",entites);
		tp.add("Decor",decoration);
		tp.add("Clickables",clickables);
		tp.addChangeListener(e->type=tp.getTitleAt(tp.getSelectedIndex()));
		snapBox.addItemListener(e->snap = (e.getStateChange() == 1));
		stackBox.addItemListener(e->stack = (e.getStateChange() == 1));
		xField.addActionListener(e->snapPos.setPos(Integer.parseInt(e.getActionCommand()),snapPos.getY()));
		yField.addActionListener(e->snapPos.setPos(snapPos.getX(),Integer.parseInt(e.getActionCommand())));
		Handler.addElement(tp,10,10,300,Window.getHeight()-50);
		Handler.addElement(panel,Window.getWidth()-160,Window.getHeight()/2-200,150,400);
		Handler.addElement(snapBox,10,Window.getHeight()-30,80,20);
		Handler.addElement(xField,90,Window.getHeight()-30,30,20);
		Handler.addElement(yField,130,Window.getHeight()-30,30,20);
		Handler.addElement(stackBox,170,Window.getHeight()-30,80,20);
	}
	private static void save() {
		IO file=new IO();
		for(int i =0; i<layers.size();i++)
		file.save(i+" "+ layers.get(i).isHidden()+"; "+layers.get(i).toString());
		file.writePlain("map1.amap");
	}
	
	private JPanel setupProperties() {
		JPanel props = new JPanel();
		props.setBackground(Color.white);
		props.setLayout(null);
		JButton layerAdd = new JButton("+");
		layerAdd.setUI(new BasicButtonUI());
		JButton layerCopy = new JButton("C");
		layerCopy.setUI(new BasicButtonUI());
		JButton layerRemove = new JButton("-");
		layerRemove.setUI(new BasicButtonUI());
		JCheckBox stairLayer= new JCheckBox("Hidden Layer");
		stairLayer.setUI(new BasicCheckBoxUI());
		JButton save = new JButton("Save");
		save.setUI(new BasicButtonUI());
		stairLayer.setBackground(Color.white);
		list.setBackground(Color.white);
		layerAdd.setBorder(BorderFactory.createLineBorder(Color.black));
		layerCopy.setBorder(BorderFactory.createLineBorder(Color.black));
		save.setBorder(BorderFactory.createLineBorder(Color.black));
		layerRemove.setBorder(BorderFactory.createLineBorder(Color.black));
		list.setUI(new BasicComboBoxUI());
		list.setBounds(10,5,40,20);
		JSeparator separator = new JSeparator();
		separator.setBounds(5,45,140,1);
		separator.setForeground(Color.black);
		layerAdd.setBounds(60,5,20,20);
		layerCopy.setBounds(120,5,20,20);
		save.setBounds(50,370,40,20);
		layerRemove.setBounds(90,5,20,20);
		selectedType.setBounds(20,45,100,20);
		selectedName.setBounds(30,60,100,20);
		stairLayer.setBounds(25,25,100,20);
		layerAdd.addActionListener(e->{
			list.addItem(layers.size());
			layers.add(new Layer());
		});
		layerCopy.addActionListener(e->{
			list.addItem(layers.size());
			int i=list.getSelectedIndex();
			layers.add(layers.get(i).copy());
		});
		save.addActionListener(e->save());
		layerRemove.addActionListener(e->{
			if(list.getSelectedIndex()>0) {
				int i=list.getSelectedIndex();
				layers.remove(i);
				list.removeAllItems();
				for(int l =0;l<layers.size();l++)list.addItem(l);
				list.setSelectedIndex(i);
			}
		});
		list.addActionListener(e->stairLayer.setSelected(layers.get(list.getSelectedIndex()).isHidden()));
		stairLayer.addItemListener(e->layers.get(list.getSelectedIndex()).setHidden(e.getStateChange() == 1));
		props.add(stairLayer);
		props.add(separator);
		props.add(list);
		props.add(save);
		props.add(selectedType);
		props.add(selectedName);
		props.add(layerAdd);
		props.add(layerCopy);
		props.add(layerRemove);
		return props;
	}
	private JPanel loadEntites() {
		JPanel jp = new JPanel();
		jp.setBackground(Color.white);
		JButton player=createEntityButton("Player",Color.green,e->{
			imgSelected="Entity";
			entity=EntityType.Player;
			selectedName.setText("Player");
			selectedType.setText(type);
		});
		JButton enemy=createEntityButton("Enemy",Color.red,e->{
			imgSelected="Entity";
			entity=EntityType.Enemey;
			selectedName.setText("Enemy");
			selectedType.setText(type);
		});
		JButton sUp=createEntityButton("StairUp",Color.orange,e->{
			imgSelected="Entity";
			entity=EntityType.StairUp;
			selectedName.setText("StairUp");
			selectedType.setText(type);
		});
		JButton sDown=createEntityButton("StairDown",Color.blue,e->{
			imgSelected="Entity";
			entity=EntityType.StairUp;
			selectedName.setText("StairDown");
			selectedType.setText(type);
		});
		jp.add(player);
		jp.add(enemy);
		jp.add(sUp);
		jp.add(sDown);
		return jp;
	}
	
	private JButton createEntityButton(String name, Color color, ActionListener listener) {
		JButton button = new JButton();
		button.setName("Player");
		button.setUI(new BasicButtonUI());
		button.setBackground(color);
		Border inside=BorderFactory.createEmptyBorder(0,0,32,32);
		Border outside=BorderFactory.createLineBorder(Color.white,10);
		button.setBorder(BorderFactory.createCompoundBorder(outside,inside));
		button.addActionListener(listener);
		return button;
	}

	private JPanel loadPanel(String path) {
		JPanel jp = new JPanel();
		jp.setBackground(Color.white);
		IO file = new IO();
		file.readPlain(path);
		while(file.hasNext())jp.add(getTileButton(file.loadNext()));
		return jp;
	}
	private JButton getTileButton(String path) {
		JButton button = new JButton();
		button.setName(path.replace("assets/",""));
		button.setIcon(new ImageIcon(path));
		button.setUI(new BasicButtonUI());
		button.setBorder(BorderFactory.createLineBorder(Color.white,10));
		button.addActionListener(e->{
			entity=EntityType.None;
			imgSelected=((JButton)e.getSource()).getName();
			selectedName.setText(imgSelected.replace("textures/","").replace(".png",""));
			selectedType.setText(type);
		});
		return button;
	}

	private static void mouseDown() {
		Vector shift=mousePos.sub(Camera.getPos());
		if(Handler.getMousePressed("1,3")&&!imgSelected.isEmpty()) {
			shouldRemove=false;
			if(Handler.getMousePressed("3"))shouldRemove=true;
			int x=(int)shift.getX();
			int y=(int)shift.getY();
			if(snap) {
				x=(int)(shift.getX()/snapPos.getX())*(int)snapPos.getX();
				y=(int)(shift.getY()/snapPos.getY())*(int)snapPos.getY();
			}
			if(!Handler.getKeyPressed("Ctrl")) {
				mouseClick.setPos(x,y);
				int i =list.getSelectedIndex();
				Block block = new Block();
				if(entity==EntityType.None)block= block.addRectImage("assets/"+imgSelected,x,y).setAngle(rotation);
				else {
					block= block.addRect(x,y,32,32);
					if(entity==EntityType.Player) block.setColor(Color.green);
					if(entity==EntityType.Enemey) block.setColor(Color.red);
					if(entity==EntityType.StairUp) block.setColor(Color.orange);
					if(entity==EntityType.StairDown) block.setColor(Color.blue);
				}
				if(!shouldRemove&&stack)layers.get(i).forceAdd(type,block);
				else if(!shouldRemove)layers.get(i).add(type,block);
				if(shouldRemove&&snap)layers.get(i).remove(type,block);
				else if(shouldRemove) layers.get(i).removeNear(type,block);
			}
			else if(entity==EntityType.None) ctrlPressed(new Vector(x,y));
		}
	}
	private static void ctrlPressed(Vector shift) {
		if(snap) {
			Vector vec=shift.div(snapPos).sub(mouseClick.div(snapPos));
			int sizeX=(int)vec.getX();
			int sizeY=(int)vec.getY();
			for(int x=0;x<=sizeX;x++) {
				for(int y=0;y<=sizeY;y++) {
					int i =list.getSelectedIndex();
					int bX= (int)mouseClick.getX()+(x*(int)snapPos.getX());
					int bY= (int)mouseClick.getY()+(y*(int)snapPos.getY());
					Block block=new Block().addRectImage("assets/"+imgSelected,bX,bY).setAngle(rotation);
					if(!shouldRemove)layers.get(i).add(type,block);
					else layers.get(i).remove(type,block);
				}
			}
		}
		mouseClick.setPos(shift.getX(),shift.getY());
	}

	private static void draw(Graphics g,double delta) {
		mouseDown();
		g.setColor(Color.white);
		int x=(int)Camera.getPos().getX();
		int y=(int)Camera.getPos().getY();
		g.drawLine(x-4,y,x+4,y);
		g.drawLine(x,y-4,x,y+4);
		Handler.onCollide(EditorMain::moveCamera, mousePos, Window.getBorder(20, 20));
		g.setColor(Color.black);
		int i =list.getSelectedIndex();
		layers.get(i).draw(g);
		g.setColor(Color.blue);
		g.drawLine((int)markPos.getX()+x-4,(int)markPos.getY()+y,(int)markPos.getX()+x+4,(int)markPos.getY()+y);
		g.drawLine((int)markPos.getX()+x,(int)markPos.getY()+y-4,(int)markPos.getX()+x,(int)markPos.getY()+y+4);
	}
}