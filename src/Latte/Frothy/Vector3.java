package Latte.Frothy;

public class Vector3 {
	private double x=0;
	private double y=0;
	private double z=0;
	private double w=1;
	protected static double t=0;
	public Vector3() {}
	public Vector3(double x, double y, double z) {
		setPos(x,y,z);
	}
	public Vector3(double x, double y, double z, double w) {
		setPos(x,y,z);
		setW(w);
	}
	public Vector3 setPos(double x, double y,double z) {
		this.x=round(x);
		this.y=round(y);
		this.z=round(z);
		return this;
	}
	public boolean equals(Vector3 vec) {
		return x==vec.getX()&&y==vec.getY()&&z==vec.getZ();
	}
	public double getX() {return x;}
	public double getY() {return y;}
	public double getZ() {return z;}

	public double dotProd(Vector3 vec) {
		return (x * vec.getX()) + (y * vec.getY())+ (z * vec.getZ());
	}
	public Vector3 crossProd(Vector3 vec) {
		double cx=(y * vec.getZ()) - (z * vec.getY());
		double cy=(z * vec.getX()) - (x * vec.getZ());
		double cz=(x * vec.getY()) - (y * vec.getX());
		return new Vector3(cx,cy,cz);
	}
	public Vector3 normalize() {
		double mag = getMag();
		return div(mag);
	}
	public double getMag() {
		return Math.sqrt(dotProd(this));
	}
	public double getDistance(Vector3 other) {
		double x= getX()-other.getX();
		double y= getY()-other.getY();
		double z= getZ()-other.getZ();
		return Math.sqrt(x*x+y*y+z*z);
	}
	public double getAngle() {
		double angle= Math.atan2(y,x);
		if(angle<0)angle+=2*Math.PI;
		return Math.toDegrees(angle);
	}
	public double getAngle2() {
		double calc=Math.sqrt(x*x+y*y);
		double angle= Math.atan2(calc,z);
		if(angle<0)angle+=2*Math.PI;
		return Math.toDegrees(angle);
	}

	public Vector3 convertVector(double mag, double angle) {
		x+=mag*Math.cos(Math.toRadians(angle));
		y+=-mag*Math.sin(Math.toRadians(angle));
		return this;
	}
	public Vector3 sub(Vector3 vec) {
		double x=this.x-vec.getX();
		double y=this.y-vec.getY();
		double z=this.z-vec.getZ();
		return new Vector3(x,y,z);
	}
	public Vector3 add(Vector3 vec) {
		double x=this.x+vec.getX();
		double y=this.y+vec.getY();
		double z=this.z+vec.getZ();
		return new Vector3(x,y,z);
	}
	public Vector3 multi(double scalar) {
		double x=this.x*scalar;
		double y=this.y*scalar;
		double z=this.z*scalar;
		return new Vector3(x,y,z);
	}
	public Vector3 multi(Vector3 vec) {
		double x=this.x*vec.getX();
		double y=this.y*vec.getY();
		double z=this.z*vec.getZ();
		return new Vector3(x,y,z);
	}
	public Vector3 div(double scalar) {
		double x=this.x/scalar;
		double y=this.y/scalar;
		double z=this.z/scalar;
		return new Vector3(x,y,z);
	}
	public Vector3 div(Vector3 vec) {
		double x=this.x/vec.getX();
		double y=this.y/vec.getY();
		double z=this.z/vec.getZ();
		return new Vector3(x,y,z);
	}
	private double round(double a) {
		return Math.round(a * 100.0) / 100.0;
	}
	public String toString() {
		return x+" "+y+" "+z;
	}
	public static Vector3 load(String line) {
		String split[]=line.split(" ");
		double x=Double.parseDouble(split[0]);
		double y=Double.parseDouble(split[1]);
		double z=Double.parseDouble(split[2]);
		return new Vector3(x,y,z);
	}
	public static Vector3 intersect(Vector3 plane_p, Vector3 plane_n, Vector3 lineStart, Vector3 lineEnd) {
			plane_n = plane_n.normalize();
			double plane_d = -plane_n.dotProd(plane_p);
			double ad =	lineStart.dotProd(plane_n);
			double bd = lineEnd.dotProd(plane_n);
			t = (-plane_d - ad) / (bd - ad);
			Vector3 lineStartToEnd = lineEnd.sub(lineStart);
			Vector3 lineToIntersect = lineStartToEnd.multi(t);
			return lineStart.add(lineToIntersect);
	}
	
	public Vector3 setW(double w) {
		this.w=round(w);
		return this;
	}
	public double getW() {
		return w;
	}
}