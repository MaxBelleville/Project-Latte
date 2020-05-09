package air;

import java.awt.Graphics;

import Latte.Client;
import Latte.Flat.Block;
import Latte.Flat.Camera;
import Latte.Flat.Navigation;
import Latte.Flat.Ray;
import Latte.Flat.Vector;

public class Enemy extends Block {
	private int layerIndx=0;
	private boolean isPlayer=false;
	private boolean once=true;
	public Enemy(boolean isPlayer) {
		this.isPlayer=isPlayer;
		addRect(0, 0, 20,20);
		updatePos(300, 300);
	}
	public void updateMP(String msg) {
		if(Client.hasData()) {
			String recieved = Client.getRecieved();
			String[] split=recieved.split(" ");
			if(split.length==5)Client.loadNext();
			if(!recieved.startsWith(msg)&&split.length==5&&Client.hasData()&&isPlayer) {
				double x = Double.parseDouble(split[0]);
				double y= Double.parseDouble(split[1]);
				double x2 = Double.parseDouble(split[2]);
				double y2= Double.parseDouble(split[3]);
				updatePos(x-x2+Camera.getPos().getX(),y-y2+Camera.getPos().getY());
				int angle = Integer.parseInt(split[4]);
				setAngle(angle);
			}
			rotate();
		}
	}
	public void update(Layer[] layer,Vector rayEnd) {
		Vector enemyEnd = getPos().add(Camera.getPos());
		Navigation nav = layer[layerIndx].getNavigation();
		if(!isPlayer) {
			nav.updateGrid(new Vector(16,16).add(Camera.getPos()),new Vector(32,32));
			int x=(int)(Math.random() * 2240)+(int)Camera.getPos().getX();
			int y=(int)(Math.random() * 1600)+(int)Camera.getPos().getY();
			nav.setEnd(enemyEnd.getX(),enemyEnd.getY());
			if(nav.getParent()==null) nav.setStart(x,y);
			Ray ray  = new Ray();
			Vector end=ray.updateTillCollide(enemyEnd,rayEnd,layer[layerIndx].get("wall"));
			if(end.getDistance(rayEnd)<1)nav.setStart(rayEnd.getX(),rayEnd.getY());
			nav.solve();
			if(nav.getParent()!=null) {
			Vector moveTo=nav.getParent().sub(enemyEnd).normalize().multi(4);
			Vector shift=moveTo.add(getPos());
			updatePos(shift.getX(),shift.getY());
			Vector look = nav.getStart();
			lookAtOffset(look.getX(),look.getY(),Camera.getPos().getX(),Camera.getPos().getY());
			}
			Camera.updateGroups(this);
		}
	}
	public void drawDebugPath(Layer[] layer,Graphics g) {
		Navigation nav = layer[layerIndx].getNavigation();
		if(!isPlayer) {
		nav.debugPath(g);
		}
	}

}
