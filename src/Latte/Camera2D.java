package Latte;

public class Camera2D {
	private static Vector2D pos=new Vector2D(0,0);
	public static void setPos(int x, int y) {
		pos.setPos(x,y);
	}
	public static void movePos(int x, int y) {
		pos.setPos(pos.getX()+x,pos.getY()+y);
	}
	public static Vector2D getPos() {
		return pos;
	}
}