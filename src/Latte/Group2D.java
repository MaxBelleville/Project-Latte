package Latte;

import java.awt.Graphics;
import java.util.ArrayList;

public class Group2D {
	private ArrayList<Object2D> objects = new ArrayList<Object2D>();
	public void add(Object2D obj) {
		Object2D tmp = obj.clone();
		objects.add(tmp);
	}
	public ArrayList<Object2D> get(){
		return objects;
	}
	public Object2D get(int i){
		return objects.get(i);
	}
	public int getSize() {
		return objects.size();
	}
	public void add(Object2D obj,int spacingX, int spacingY, int sizeX, int sizeY){
		for (int xi =0; xi<sizeX;xi++) {
			for (int yi =0;yi<sizeY;yi++) {
			Object2D tmp =obj.clone();
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
	public void rotate(double angle) {
		for(int i=0; i<objects.size();i++) objects.get(i).rotate(angle);
	}
}