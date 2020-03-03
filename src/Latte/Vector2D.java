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
	public double crossProd(Vector2D vec) {
		  return (x * vec.getY()) - (y * vec.getX());
		}
	public Vector2D unitVector() {
		double mag=getMag();
		double x2=Math.round((x/mag));
		double y2=Math.round((y/mag)); 
		if(x/mag<0)x2=-Math.round(Math.abs(x/mag));
		if(y/mag<0)y2=-Math.round(Math.abs(y/mag));
		return new Vector2D(x2,y2);
	}
	public double getMag() {
		return Math.sqrt(x*x+y*y);
	}
	public double getAngle() {
		double angle= Math.atan2(y,x);
		if(angle<0)angle+=2*Math.PI;
		return angle;
	}
	
	public Vector2D convertVector(double mag, double angle) {
		x=mag*Math.cos(angle);
		y=-mag*Math.sin(angle);
		return this;
	}
}