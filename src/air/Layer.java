package air;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Latte.*;
public class Layer {
	private HashMap<String,Group2D> layerItems = new HashMap<String,Group2D>();
	public Layer(String...initItems) {
		for(String str: initItems)
		layerItems.put(str,new Group2D());
	}
	public Group2D get(String key) {
		return layerItems.get(key);
	}
	public void draw(Graphics g) {
		g.setColor(Color.white);
		for(Group2D group: layerItems.values()) {
			group.fill(g);
		}
		g.setColor(Color.black);
		layerItems.get("wall").draw(g);
	}
	public Layer load(String text) {
		String[] split= text.split("\t");
		for(String line:split) {
			String[] keyVal=line.split(":");
			layerItems.put(keyVal[0],new Group2D().load(keyVal[1]));
		}
		return this;
	}
	
	public String toString(){
		String tmp="";
		Iterator<Map.Entry<String,Group2D>> it=layerItems.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String,Group2D> pair =it.next(); 
			tmp+=pair.getKey()+":"+pair.getValue()+"\t";
		}
		return tmp;
	}
}