package Latte;

public class Vector2D {
	private double x=0;
	private double y=0;
	public Vector2D() {}
	public Vector2D(double x, double y) {
		this.x=x;
		this.y=y;
	}
	public void setPos(double x, double y) {
		this.x=x;
		this.y=y;
	}
	public double getX() {return x;}
	public double getY() {return y;}

	public double dotProd(Vector2D vec) {
	  return (x * vec.getX()) + (y * vec.getY());
	}
	public Vector2D unitVector() {
		double mag=Math.sqrt(x*x+y*y);
		return new Vector2D(Math.round(x/mag),Math.round(y/mag));
	}
}
