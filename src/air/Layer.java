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
		for(String key: layerItems.keySet()) {
			if(!key.equals("Entites")) {
			layerItems.get(key).addPos(0,0);
			layerItems.get(key).drawImage(g);
			}
		}
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
		String[] split=val.split(",");
		for(String blockStr: split) {
			String[] xySplit = blockStr.split(" ");
			double x= Double.parseDouble(xySplit[0]);
			double y= Double.parseDouble(xySplit[1]);
			Block block=new Block().addRectImage(xySplit[2],(int)x,(int)y);
			if(block.getImage().isEmpty()) block.addRect(x,y,32,32);
			if(xySplit[2].equals("Player")) block.setColor(Color.green);
			else if(xySplit[2].equals("Enemy")) block.setColor(Color.red);
			else if(xySplit[2].equals("StairUp")) block.setColor(Color.orange);
			else if(xySplit[2].equals("StairDown")) block.setColor(Color.blue);
			group.add(block);
	
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
	private Navigation nav = new Navigation();
	
	public void setupNav(Vector start, Vector bounds) {
		double scalar =32;
		bounds=bounds.div(scalar).add(new Vector(1,1));
		nav.setupGrid((int)bounds.getX(),(int)bounds.getY(),
				new Vector(16,16).add(start),new Vector(32,32));
		for(Block obj: get("Wall").get()) {
			Vector wall=new Vector((int)obj.getPoint(0).getX()/32,(int)obj.getPoint(0).getY()/32);
			nav.setObstacale((int)wall.getX(),(int)wall.getY(),true);
		}
	}
	
	public Navigation getNavigation() {
		return nav;
	}
}