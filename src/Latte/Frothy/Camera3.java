package Latte.Frothy;

import java.util.ArrayList;

import Latte.Window;
import Latte.Flat.Vector;

public class Camera3 {
	private static Vector3 pos=new Vector3();
	private static Vector3 up=new Vector3(0,1,0);
	private static Vector3 lookDir=new Vector3();
	private static double yaw=0;
	private static double pitch=0;
	static double[] depthBuffer;
	static double near=0.1;
	static double far =1000.0;
	static double fov=90.0;
	static double aspectRatio=0;
	static double color=0;
	private static Matrix matProj = new Matrix();
	public static void setYaw(double yaw) {
		Camera3.yaw = yaw;
	}
	public static void addYaw(double yaw) {
		Camera3.yaw += yaw;
	}
	public static double getYaw() {
		return yaw;
	}
	public static void setPitch(double pitch) {
		Camera3.pitch = pitch;
		double degree=Math.toDegrees((Camera3.pitch));
		if(degree>=90)Camera3.pitch=Math.toRadians(89);
		if(degree<=-90)Camera3.pitch=Math.toRadians(-89);
	}
	public static void addPitch(double pitch) {
		Camera3.pitch += pitch;
		double degree=Math.toDegrees((Camera3.pitch));
		if(degree>=90)Camera3.pitch=Math.toRadians(89);
		if(degree<=-90)Camera3.pitch=Math.toRadians(-89);
	}
	public static double getPitch() {
		return pitch;
	}
	public static void setPos(double x, double y, double z) {
		pos.setPos(x,y,z);
	}

	public static Vector3 getPos() {
		return pos;
	}
	
	public static void addPos(double x, double y, double z) {
		pos=pos.add(new Vector3(x,y,z));
	}

	
	public static void setupProjection() {
		depthBuffer=new double[Window.getWidth()*Window.getHeight()];
		aspectRatio=(Window.getHeight()+0.0)/Window.getWidth();
		matProj=Matrix.projection(aspectRatio,fov,near,far);
	}
	public static void clear() {
		for (int i = 0; i < Window.getWidth()*Window.getHeight(); i++)
			depthBuffer[i] = 0.0;
	}
	protected static ArrayList<Tri> project(ArrayList<Tri> tris) {
		Vector3 target = new Vector3(0,0,1);
		Matrix yawMat = Matrix.rotateY(yaw);
		Matrix pitchMat = Matrix.rotateX(pitch);
		Matrix rot = pitchMat.multi(yawMat);
		lookDir=rot.multiVec(target);
		target = pos.add(lookDir);
		ArrayList<Tri> newTris = new ArrayList<Tri>();
		Matrix matView = Matrix.pointAt(pos,target,up).invert();
		for(Tri tri: tris) {
			Tri viewed = new Tri(), proj=new Tri();
			Vector3 normal = tri.getNormal();
			//TODO: SEPERATE LIGHT INTO OWN CLASS.
			Vector3 camRay = tri.p[0].sub(pos);
			if(normal.dotProd(camRay)<0.0)
			{
				Vector3 light = new Vector3(0,1,-1).normalize();
				
				tri.color = Math.max(0.1,light.dotProd(normal));
				if(tri.color>1)tri.color=1;
				for(int i=0;i<3;i++) {
					viewed.p[i] = matView.multiVec(tri.p[i]);
					viewed.t[i]=tri.t[i];
				}
				ArrayList<Tri> clippedTris =viewed.clipping(new Vector3(0,0,0.1),new Vector3(0,0,1));
				for(Tri clipped: clippedTris) {
					for(int i=0;i<3;i++) {
						proj.p[i]=  matProj.multiVec(clipped.p[i]);
						proj.t[i]=clipped.t[i];
						proj.t[i]=new Vector(proj.t[i].getX()/proj.p[i].getW(),
								proj.t[i].getY()/proj.p[i].getW(),1.0/proj.p[i].getW());
						proj.p[i]=	proj.p[i].div(proj.p[i].getW());
						proj.p[i]=	proj.p[i].multi(new Vector3(-1,-1,1));
						proj.p[i]=	proj.p[i].add(new Vector3(1,1,0));
						double w=Window.getWidth()/2.0,h=Window.getHeight()/2.0;
						proj.p[i]=proj.p[i].multi(new Vector3(w,h,1));
						proj.color=tri.color;
					}
					newTris.add(proj);
				}
			}
		}
		return newTris;
	}
	public static Vector3 getForward(double delta) {
		return lookDir.multi(delta);
	}
}
