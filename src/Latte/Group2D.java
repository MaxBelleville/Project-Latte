package Latte;

import java.util.ArrayList;

public class Group2D {
	private ArrayList<Object2D> objects = new ArrayList<Object2D>();
	public void add(Object2D obj) {
		objects.add(obj);
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
	public void add(Object2D obj, int count,Vector2D unitVector){
		for (int i =0; i<count;i++) {
			for(int v = 0; v<obj.getSize();v++) {
				Vector2D vec= obj.getVector(v);
				int xMulti=(i*(int)unitVector.getX());
				int yMulti=(i*(int)unitVector.getY());
				obj.setPoint(v,(int)vec.getX()*xMulti,(int)vec.getY()*yMulti);
			}
			objects.add(obj);
		}
	}
}
