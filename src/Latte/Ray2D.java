package Latte;

public class Ray2D {
	private Object2D ray = new Object2D();
	private boolean canUpdate=true;
	public Ray2D(double x, double y) {
		ray.addPoint(x, y);
		ray.addPoint(0, 0);
	}
	public void update(double x, double y) {
		if(canUpdate)ray.setPoint(1,x,y);
	}
	
	public void collideWith(Group2D...groups) {
		for(Group2D group: groups) {
			if(ray.collideWith(group)!=null)canUpdate=false;
			else canUpdate=true;
		}
	}
	public void collideWith(Object2D...objs) {
		for(Object2D obj: objs)  {
			if(ray.collideWith(obj)!=null) canUpdate=false;
			else canUpdate=true;
		}
	}
}
