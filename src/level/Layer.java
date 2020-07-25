package level;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Latte.Flat.Block;
import Latte.Flat.Group;

public class Layer {
	private HashMap<String,Group> layerItems = new HashMap<String,Group>();
	private boolean isHidden=false;
	public Group get(String key) {
		return layerItems.get(key);
	}
	public boolean isHidden() {
		return isHidden;
	}
	public void setHidden(boolean hidden) {
		isHidden=hidden;
	}
	public void draw(Graphics g) {
		for(Group group: layerItems.values()) {
			group.addPos(0,0);
			group.drawImage(g);
		}
	}
	public void add(String key, Block block) {
		if(!layerItems.containsKey(key)) layerItems.put(key,new Group());
		if(layerItems.get(key).find(block,1).isNew())
		layerItems.put(key,layerItems.get(key).add(block));
	}

	
	public Block find(String key, Block block) {
		return layerItems.get(key).find(block,1);
	}
	public void remove(String key, Block block) {
		if(layerItems.containsKey(key))
		layerItems.put(key,layerItems.get(key).remove(block));
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
			double w= Double.parseDouble(xySplit[2]);
			double h= Double.parseDouble(xySplit[3]);
			group.add(new Block().addRect(x,y,w,h).setImage("assets/"+xySplit[4]));
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
				tmp+=value.get(i).getPoint(0)+" "+value.get(i).getImage()+",";
			tmp+=value.get(value.get().size()-1).getPoint(0)+" "+value.get(value.get().size()-1).getImage();
			tmp+="\t";
		}
		return tmp;
	}
	public void removeNear(String key, Block block) {
		Block found=layerItems.get(key).find(block,20);
		if(!found.isNew())
		layerItems.put(key,layerItems.get(key).remove(found));
	}
	public void forceAdd(String key, Block block) {
		if(!layerItems.containsKey(key)) layerItems.put(key,new Group());
		layerItems.put(key,layerItems.get(key).add(block));
	}
	public Layer copy() {
		Layer layer = new Layer();
		for (String key: layerItems.keySet()) {
			for(Block block: layerItems.get(key).get()) {
				layer.forceAdd(key,block);
			}
		}
		return layer;
	}
}