package Latte.Flat;

import java.text.DecimalFormat;

public class Camera {
	private static Vector pos = new Vector(0, 0);

	public static void setPos(double x, double y) {
		pos.setPos(x, y);
	}

	public static void movePos(double x, double y) {
		pos.setPos(pos.getX() + x, pos.getY() + y);
	}

	public static Vector getPos() {
		return pos;
	}
	public static void updateGroups(Group... groups) {
		for(Group group: groups) {
			for(Block obj:group.get()) {
				if(!obj.getSticky()) {
					Vector vec = pos.add(obj.getPos());
					obj.setAndRotate(vec.getX(),vec.getY());
					obj.resetDisplacement();
				}
			}
		}
	}
	public static void updateGroups(Block...objs) {
			for(Block obj:objs) {
				if(!obj.getSticky()) {
					Vector vec = pos.add(obj.getPos());
					obj.setAndRotate(vec.getX(),vec.getY());
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