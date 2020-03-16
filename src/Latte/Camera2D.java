package Latte;

import java.text.DecimalFormat;

public class Camera2D {
	private static Vector2D pos = new Vector2D(0, 0);

	public static void setPos(double x, double y) {
		pos.setPos(x, y);
	}

	public static void movePos(double x, double y) {
		pos.setPos(pos.getX() + x, pos.getY() + y);
	}

	public static Vector2D getPos() {
		return pos;
	}
	public static void updateGroups(Group2D... groups) {
		for(Group2D group: groups) {
			for(Object2D obj:group.get()) {
				if(!obj.getSticky()) {
					double x =obj.getPos().getX()+pos.getX();
					double y =obj.getPos().getY()+pos.getY();
					obj.setPoints(x,y);
					obj.resetDisplacement();
				}
			}
		}
	}
	public static void updateGroups(Object2D...objs) {
			for(Object2D obj:objs) {
				if(!obj.getSticky()) {
					double x =obj.getPos().getX()+pos.getX();
					double y =obj.getPos().getY()+pos.getY();
					obj.updatePos(x,y);
					obj.resetDisplacement();
				}
			}
	}
	public String toString() {
		DecimalFormat df = new DecimalFormat("0.00");
		String tmp=df.format(pos.getX())+", "+df.format(pos.getY());
		return tmp;
	}
}