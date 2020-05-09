package Latte.Flat;


public class Light extends Block {
	private int layer=0;
	public void updateLight(double x, double y, double radius,Group...groups) {
		for(int i=0;i<360/8;i++) {
		Vector end = new Vector(x,y).convertVector(radius,i*8);
		Ray ray = new Ray();
		Vector vec=ray.updateTillCollide(new Vector(x,y),end,groups);
		addPoint(vec.getX(),vec.getY());
		}
	}
	public void setLayer(int layer) {
		this.layer=layer;
	}
}
