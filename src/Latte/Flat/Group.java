package Latte.Flat;

import java.awt.Graphics;
import java.util.ArrayList;

public class Group {
	private ArrayList<Block> objects = new ArrayList<Block>();
	public void add(Block obj) {
		Block tmp = obj.clone();
		objects.add(tmp);
	}
	public ArrayList<Block> get(){
		return objects;
	}
	public Block get(int i){
		return objects.get(i);
	}
	public int getSize() {
		return objects.size();
	}
	public void add(Group group) {
		for (Block obj: group.get())
			objects.add(obj);
	}
	public void add(Block obj,int spacingX, int spacingY, int sizeX, int sizeY){
		for (int yi =0; yi<sizeY;yi++) {
			for (int xi =0;xi<sizeX;xi++) {
			Block tmp =obj.clone();
			tmp.updatePos(spacingX*xi,spacingY*yi);
			objects.add(tmp);
			}
		}
	}
	public void draw(Graphics g) {
		for(int i=0; i<objects.size();i++) objects.get(i).draw(g);
	}
	public void fill(Graphics g) {
		for(int i=0; i<objects.size();i++) objects.get(i).fill(g);
	}
	public void drawImage(Graphics g) {
		for(int i=0; i<objects.size();i++) objects.get(i).drawImage(g);
	}
	public void drawImage(Graphics g, int x, int y, int w, int h ) {
		for(int i=0; i<objects.size();i++) objects.get(i).drawImage(g,x,y,w,h);
	}
	public void addPos(int x, int y) {
		for(int i=0; i<objects.size();i++) {
			int posX= (int)objects.get(i).getPos().getX();
			int posY= (int)objects.get(i).getPos().getY();
			objects.get(i).updatePos(x+posX,y+posY);
		}
	}
	public void setAngle(double angle) {
		for(int i=0; i<objects.size();i++) objects.get(i).setAngle(angle);
	}
	public void rotate() {
		for(int i=0; i<objects.size();i++) objects.get(i).rotate();
	}
	public Vector getEmpty(int indx) {
		ArrayList<Vector> empties = new ArrayList<Vector>();
		for(int i=0; i<objects.size();i++) {
			if(objects.get(i).hasEmpties()) {
				Block obj=objects.get(i);
				for(int v=0; v<obj.getSize();v++) {
					Vector empty = obj.getEmpty(v);
					if(empty!=null) empties.add(empty);
				}
			}
		}
		if(indx<empties.size()) return empties.get(indx);
		return null;
	}
	public void rotate(double angle) {
		for(int i=0; i<objects.size();i++) objects.get(i).rotate(angle);
	}
	public Group load(String line) {
		String[] objsStr=line.split("\\]\\[");
		for(String obj: objsStr) {
			Block tmp=new Block().load(obj);
			objects.add(tmp);
		}
		return this;
	}
	public String toString() {
		String tmp="";
		for(int i=0; i<objects.size()-1;i++) tmp+=objects.get(i)+"][";
		tmp+=objects.get(objects.size()-1);
		return tmp;
	}
}