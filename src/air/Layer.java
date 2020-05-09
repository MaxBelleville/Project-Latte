package air;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Latte.Flat.Block;
import Latte.Flat.Group;
import Latte.Flat.Navigation;
import Latte.Flat.Vector;

public class Layer {
	private HashMap<String,Group> layerItems = new HashMap<String,Group>();
	private Navigation nav = new Navigation();
	public Layer(String...initItems) {
		for(String str: initItems)
		layerItems.put(str,new Group());
	}
	public void setupNav() {
		Group group = get("wall");
		Block start=group.get(0);
		Block end=group.get(group.getSize()-1);
		double scalar=end.getPoint(3).getDistance(end.getPoint(0));
		Vector bounds=end.getPoint(0).sub(start.getPoint(0));
		Vector offset=start.getPoint(0);
		bounds=bounds.div(scalar).add(new Vector(1,1));
		nav.setupGrid((int)bounds.getX(),(int)bounds.getY(),
				new Vector(16,16).add(offset),new Vector(32,32));
		for(Block obj: get("wall").get()) {
			Vector wall=obj.getPoint(0).div(scalar);
			nav.setObstacale((int)wall.getX(),(int)wall.getY(),true);
		}
	}
	public Group get(String key) {
		return layerItems.get(key);
	}
	public void draw(Graphics g) {
		g.setColor(Color.white);
		for(Group group: layerItems.values()) {
			if(group!=layerItems.get("wall"))group.fill(g);
		}
		g.setColor(Color.gray);
		layerItems.get("wall").fill(g);
	}
	public Layer load(String text) {
		String[] split= text.split("\t");
		for(String line:split) {
			String[] keyVal=line.split(":");
			layerItems.put(keyVal[0],getGroup(keyVal[1]));
		}
		return this;
	}
	
	private Group getGroup(String val) {
		Group group = new Group();
		String[] split=val.split(", ");
		for(String block: split) {
			String[] xySplit = block.split(" ");
			double x= Double.parseDouble(xySplit[0]);
			double y= Double.parseDouble(xySplit[1]);
			group.add(new Block().addRect(x,y,32,32));
		}
		return group;
	}
	public String toString(){
		String tmp="";
		Iterator<Map.Entry<String,Group>> it=layerItems.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String,Group> pair =it.next(); 
			tmp+=pair.getKey()+":";
			Group value = pair.getValue();
			for(int i=0; i< value.get().size()-1;i++)
				tmp+=value.get(i).getPoint(0)+", ";
			tmp+=value.get(value.get().size()-1).getPoint(0);
			tmp+="\t";
		}
		return tmp;
	}
	public Navigation getNavigation() {
		return nav;
	}
}