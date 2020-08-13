package Latte.Flat;

public class Vector {
	private double x=0;
	private double y=0;
	private double w=1;
	public Vector() {}
	public Vector(double x, double y) {
		setPos(x,y);
	}
	public Vector(double x, double y, double w) {
		setPos(x,y);
		setW(w);
	}
	public void setW(double w) {
		this.w=round(w);
	}
	public double getW() {
		return w;
	}
	public void setPos(double x, double y) {
		this.x=round(x);
		this.y=round(y);
	}
	public boolean equals(Vector vec) {
		return x==vec.getX()&&y==vec.getY();
	}
	public double getX() {return x;}
	public double getY() {return y;}

	public double dotProd(Vector vec) {
	  return (x * vec.getX()) + (y * vec.getY());
	}
	public double crossProd(Vector vec) {
		  return (x * vec.getY()) - (y * vec.getX());
		}
	public Vector normalize() {
		double mag = getMag();
		Vector tmp = new Vector();
		if (mag>0) tmp=div(mag);
		return tmp;
	}
	public double getMag() {
		return Math.sqrt(x*x+y*y);
	}
	public double getDistance(Vector other) {
		double x= getX()-other.getX();
		double y= getY()-other.getY();
		return Math.sqrt(x*x+y*y);
	}
	public int getAngle() {
		double angle= Math.atan2(y,x);
		if(angle<0)angle+=2*Math.PI;
		return (int) Math.toDegrees(angle)%360;
	}
	
	public Vector convertVector(double mag, double angle) {
		x+=mag*Math.cos(Math.toRadians(angle));
		y+=-mag*Math.sin(Math.toRadians(angle));
		return this;
	}
	public Vector sub(Vector vec) {
		double x=this.x-vec.getX();
		double y=this.y-vec.getY();
		return new Vector(x,y);
	}
	public Vector add(Vector vec) {
		double x=this.x+vec.getX();
		double y=this.y+vec.getY();
		return new Vector(x,y);
	}
	public Vector multi(double scalar) {
		double x=this.x*scalar;
		double y=this.y*scalar;
		return new Vector(x,y);
	}
	public Vector div(double scalar) {
		double x=this.x/scalar;
		double y=this.y/scalar;
		return new Vector(x,y);
	}
	private double round(double a) {
		return Math.round(a * 100.0) / 100.0;
	}
	public String toString() {
		return x+" "+y;
	}
	public static Vector parseVector(String vec) {
		String split[]=vec.split(" ");
		double x=Double.parseDouble(split[0]);
		double y=Double.parseDouble(split[1]);
		return new Vector(x,y);
	}
	public Vector setUV(double t,Vector outside, Vector inside) {
		double u= t * (outside.getX() - inside.getX()) + inside.getX();
		double v= t * (outside.getY() - inside.getY()) + inside.getY();
		double w= t * (outside.getW() - inside.getW()) + inside.getW();
		return new Vector(u,v,w);
	}
	public static Vector load(String line) {
		String split[]=line.split(" ");
		double x=Double.parseDouble(split[0]);
		double y=Double.parseDouble(split[1]);
		return new Vector(x,y);	
	}
	public Vector multi(Vector vec) {
		double x=this.x*vec.getX();
		double y=this.y*vec.getY();
		return new Vector(x,y);
	}
	public Vector div(Vector vec) {
		double x=this.x/vec.getX();
		double y=this.y/vec.getY();
		return new Vector(x,y);
	}
}