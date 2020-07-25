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
	
	public String toString() {
		DecimalFormat df = new DecimalFormat("0.00");
		String tmp=df.format(pos.getX())+", "+df.format(pos.getY());
		return tmp;
	}
}