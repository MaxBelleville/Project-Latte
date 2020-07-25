package Latte.Frothy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Latte.Window;
import Latte.Flat.Vector;

public class Tri {
	Vector3[] p = new Vector3[3];
	Vector[] t = new Vector[3];
	double color=0;
	Tri(Vector3 p1, Vector3 p2, Vector3 p3,Vector t1, Vector t2, Vector t3) {
		p[0]=p1;
		p[1]=p2;
		p[2]=p3;
		t[0]=t1;
		t[1]=t2;
		t[2]=t3;
	}
	Tri(Vector3 p1, Vector3 p2, Vector3 p3) {
		p[0]=p1;
		p[1]=p2;
		p[2]=p3;
		t[0]=new Vector();
		t[1]=new Vector();
		t[2]=new Vector();
	}
	Tri() {
		p[0]=new Vector3();
		p[1]=new Vector3();
		p[2]=new Vector3();
		t[0]=new Vector();
		t[1]=new Vector();
		t[2]=new Vector();
	}
	public Tri clone() {
		Tri temp = new Tri();
		for(int i=0;i<3;i++) {
			temp.p[i]=p[i];
			temp.t[i]=t[i];
		}
		temp.color=color;
		return temp;
	}
	public Vector3 getNormal() {
		Vector3 line1=new Vector3(),line2=new Vector3();
		line1 = p[1].sub(p[0]);
		line2 = p[2].sub(p[0]);
		return (line1.crossProd(line2)).normalize();
	}
	public double getClippingDist(Vector3 point,Vector3 plane_n,Vector3 plane_p) {
		return (plane_n.dotProd(point)-(plane_n.dotProd(plane_p)));
	}
	public ArrayList<Tri> clipping(Vector3 plane_p, Vector3 plane_n)
	{
		plane_n = plane_n.normalize();
		ArrayList<Tri> tris = new ArrayList<Tri>();
		Vector3 inside_points[] = new Vector3[3];  int nInsidePointCount = 0;
		Vector3 outside_points[] = new Vector3[3]; int nOutsidePointCount = 0;
		Vector inside_tex[] = new Vector[3]; int nInsideTexCount = 0;
		Vector outside_tex[]= new Vector[3]; ; int nOutsideTexCount = 0;
		double d0 = getClippingDist(p[0],plane_n,plane_p);
		double d1 = getClippingDist(p[1],plane_n,plane_p);
		double d2 = getClippingDist(p[2],plane_n,plane_p);

		if (d0 >= 0) { inside_points[nInsidePointCount++] = p[0]; inside_tex[nInsideTexCount++] = t[0]; }
		else {
			outside_points[nOutsidePointCount++] =p[0]; outside_tex[nOutsideTexCount++] =t[0];
		}
		if (d1 >= 0) {
			inside_points[nInsidePointCount++] =p[1]; inside_tex[nInsideTexCount++] =t[1];
		}
		else {
			outside_points[nOutsidePointCount++] =p[1];  outside_tex[nOutsideTexCount++] =t[1];
		}
		if (d2 >= 0) {
			inside_points[nInsidePointCount++] =p[2]; inside_tex[nInsideTexCount++] =t[2];
		}
		else {
			outside_points[nOutsidePointCount++] =p[2];  outside_tex[nOutsideTexCount++] =t[2];
		}

		if (nInsidePointCount == 3) tris.add(this);

		if (nInsidePointCount == 1 && nOutsidePointCount == 2)
		{
			Tri temp =clone();
			temp.p[0] = inside_points[0];
			temp.t[0] = inside_tex[0];
			temp.p[1] = Vector3.intersect(plane_p, plane_n, inside_points[0], outside_points[0]);
			temp.t[1].setUV(Vector3.t,outside_tex[0],inside_tex[0]);
			temp.p[2] = Vector3.intersect(plane_p, plane_n, inside_points[0], outside_points[1]);
			temp.t[2].setUV(Vector3.t,outside_tex[1],inside_tex[0]);
			temp.color=color;
			tris.add(temp);
		}

		if (nInsidePointCount == 2 && nOutsidePointCount == 1)
		{
			Tri temp =clone();
			Tri temp2 =clone();
			temp.p[0] = inside_points[0];
			temp.p[1] = inside_points[1];
			temp.t[0] = inside_tex[0];
			temp.t[1] = inside_tex[1];
			temp.p[2] = Vector3.intersect(plane_p, plane_n, inside_points[0], outside_points[0]);
			temp.t[2].setUV(Vector3.t,outside_tex[0],inside_tex[0]);
			temp2.p[0] = inside_points[1];
			temp2.t[0] = inside_tex[1];
			temp2.p[1] = temp.p[2];
			temp2.t[1] = temp.t[2];
			temp2.p[2] = Vector3.intersect(plane_p, plane_n, inside_points[1], outside_points[0]);
			temp2.t[2].setUV(Vector3.t,outside_tex[0],inside_tex[1]);
			temp.color=color;
			temp2.color=color;
			tris.add(temp);
			tris.add(temp2);
		}
		return tris;
	}

	public void TexturedTriangle(Graphics g,BufferedImage img)
	{
		double[] arr1 = {p[0].getX(),p[0].getY(),t[0].getX(),t[0].getY(),t[0].getW()};
		double[] arr2 = {p[1].getX(),p[1].getY(),t[1].getX(),t[1].getY(),t[1].getW()};
		double[] arr3 = {p[2].getX(),p[2].getY(),t[2].getX(),t[2].getY(),t[2].getW()};
		if (arr2[1] < arr1[1])
		{
			for(int i=0;i<arr1.length;i++) {
				double tmp=arr1[i];
				arr1[i]=arr2[i];
				arr2[i]=tmp;
			}
		}

		if (arr3[1] < arr1[1])
		{
			for(int i=0;i<arr1.length;i++) {
				double tmp=arr3[i];
				arr3[i]=arr1[i];
				arr1[i]=tmp;
			}
		}

		if (arr3[1] < arr2[1])
		{
			for(int i=0;i<arr2.length;i++) {
				double tmp=arr3[i];
				arr3[i]=arr2[i];
				arr2[i]=tmp;
			}
		}

		int dy1 = (int)arr2[1] - (int)arr1[1];
		int dx1 = (int)arr2[0] - (int)arr1[0];
		double dv1 = arr2[3] - arr1[3];
		double du1 = arr2[2] - arr1[2];
		double dw1 = arr2[4] - arr1[4];

		int dy2 = (int)arr3[1] - (int)arr1[1];
		int dx2 = (int)arr3[0] - (int)arr1[0];
		double dv2 = arr3[3] - arr1[3];
		double du2 = arr3[2] - arr1[2];
		double dw2 = arr3[4] - arr1[4];


		double tex_u, tex_v, tex_w;

		double dax_step = 0, dbx_step = 0,
			du1_step = 0, dv1_step = 0,
			du2_step = 0, dv2_step = 0,
			dw1_step=0, dw2_step=0;

		if (dy1!=0) dax_step = dx1 / Math.abs(dy1);
		if (dy2!=0) dbx_step = dx2 / Math.abs(dy2);

		if (dy1!=0) du1_step = du1 / Math.abs(dy1);
		if (dy1!=0) dv1_step = dv1 / Math.abs(dy1);
		if (dy1!=0) dw1_step = dw1 / Math.abs(dy1);

		if (dy2!=0) du2_step = du2 / Math.abs(dy2);
		if (dy2!=0) dv2_step = dv2 / Math.abs(dy2);
		if (dy2!=0) dw2_step = dw2 / Math.abs(dy2);


		if (dy1!=0)
		{
			for (int i = (int)arr1[1]; i <= (int)arr2[1]; i++)
			{
				int ax = (int) (arr1[0] + (i - arr1[1]) * dax_step);
				int bx = (int) (arr1[0] + (i - arr1[1]) * dbx_step);

				double tex_su = arr1[2] + (i - arr1[1]) * du1_step;
				double tex_sv = arr1[3] + (i - arr1[1]) * dv1_step;
				double tex_sw = arr1[4] + (i - arr1[1]) * dw1_step;

				double tex_eu = arr1[2] + (i - arr1[1]) * du2_step;
				double tex_ev = arr1[3] + (i - arr1[1]) * dv2_step;
				double tex_ew = arr1[4] + (i - arr1[1]) * dw2_step;

				double[] tex_arr1 = {ax,tex_su,tex_sv,tex_sw};
				double[] tex_arr2 = {bx,tex_eu,tex_ev,tex_ew};
				if (ax > bx) {
					for(int t=0;t<tex_arr1.length;t++) {
						double tmp=tex_arr1[t];
						tex_arr1[t]=tex_arr2[t];
						tex_arr2[t]=tmp;
					}
				}

				tex_u = tex_arr1[1];
				tex_v = tex_arr1[2];
				tex_w = tex_arr1[3];

				double tstep = 1.0f / (tex_arr2[0] - tex_arr1[0]);
				double t = 0.0f;

				for (int j = (int)tex_arr1[0]; j < (int)tex_arr2[0]; j++)
				{
					tex_u = (1.0f - t) * tex_arr1[1] + t * tex_arr2[1];
					tex_v = (1.0f - t) * tex_arr1[2] + t * tex_arr2[2];
					tex_w = (1.0f - t) * tex_arr1[3] + t * tex_arr2[3];
					int sx=0,sy=0;
					if(img!=null) {
						sx= (int)((tex_u/tex_w) * img.getWidth());
						sy = (int)((tex_v/tex_w)* img.getHeight()-1.0f);
					}
					if (tex_w > Camera3.depthBuffer[i*Window.getWidth() + j])
					{
						if(img!=null) {
							if((sx>=img.getWidth()||sy>=img.getHeight()||sx<=0||sy<=0))g.setColor(Color.black);
							else g.setColor(new Color(img.getRGB(sx,sy)));
						}
						g.fillRect(j,i,1,1);
						Camera3.depthBuffer[i*Window.getWidth() + j] = tex_w;
					}
					t += tstep;
				}	

			}
		}

		dy1 = (int)arr3[1] - (int)arr2[1];
		dx1 = (int)arr3[0] - (int)arr2[0];
		dv1 = arr3[3] - arr2[3];
		du1 = arr3[2] - arr2[2];
		dw1 = arr3[4] - arr2[4];
		
		if (dy1!=0) dax_step = dx1 / Math.abs(dy1);
		if (dy2!=0) dbx_step = dx2 / Math.abs(dy2);

		du1_step = 0; dv1_step = 0;
		if (dy1!=0) du1_step = du1 / Math.abs(dy1);
		if (dy1!=0) dv1_step = dv1 / Math.abs(dy1);
		if (dy1!=0) dw1_step = dw1 / Math.abs(dy1);
		
		if (dy1!=0)
		{
			for (int i = (int)arr2[1]; i <= (int)arr3[1]; i++)
			{
				int ax = (int) (arr2[0] + (i - arr2[1]) * dax_step);
				int bx = (int) (arr1[0] + (i - arr1[1]) * dbx_step);

				double tex_su = arr2[2] + (i - arr2[1]) * du1_step;
				double tex_sv = arr2[3] + (i - arr2[1]) * dv1_step;
				double tex_sw = arr2[4] + (i - arr2[1]) * dw1_step;

				double tex_eu = arr1[2] + (i - arr1[1]) * du2_step;
				double tex_ev = arr1[3] + (i - arr1[1]) * dv2_step;
				double tex_ew = arr1[4] + (i - arr1[1]) * dw2_step;
				double[] tex_arr1 = {ax,tex_su,tex_sv,tex_sw};
				double[] tex_arr2 = {bx,tex_eu,tex_ev,tex_ew};
				if (ax > bx) {
					for(int t=0;t<tex_arr1.length;t++) {
						double tmp=tex_arr1[t];
						tex_arr1[t]=tex_arr2[t];
						tex_arr2[t]=tmp;
					}
				}

				tex_u = tex_arr1[1];
				tex_v = tex_arr1[2];
				tex_w = tex_arr1[3];

				double tstep = 1.0f / (tex_arr2[0] - tex_arr1[0]);
				double t = 0.0f;

				for (int j = (int)tex_arr1[0]; j < (int)tex_arr2[0]; j++)
				{
					tex_u = (1.0f - t) * tex_arr1[1] + t * tex_arr2[1];
					tex_v = (1.0f - t) * tex_arr1[2] + t * tex_arr2[2];
					tex_w = (1.0f - t) * tex_arr1[3] + t * tex_arr2[3];
					int sx=0,sy=0;
					if(img!=null) {
						sx= (int)((tex_u/tex_w) * img.getWidth());
						sy = (int)((tex_v/tex_w)* img.getHeight()-1.0f);
					}
					if (tex_w > Camera3.depthBuffer[i*Window.getWidth() + j])
					{
						if(img!=null) {
							if((sx>=img.getWidth()||sy>=img.getHeight()||sx<=0||sy<=0))g.setColor(Color.black);
							else g.setColor(new Color(img.getRGB(sx,sy)));
						}
						g.fillRect(j,i,1,1);
						Camera3.depthBuffer[i*Window.getWidth() + j] = tex_w;
					}
					t += tstep;
				}
			}	
		}		

	}
}