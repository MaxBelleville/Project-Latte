package Latte.Frothy;

public class Matrix {
	double[][] mat = new double[4][4];
	public Matrix() {
		for(int x=0;x<mat.length;x++) {
			for(int y=0;y<mat.length;y++) {
				mat[x][y]=0.0;
			}
		}
	}
	public Matrix multi(Matrix other) {
		Matrix matrix =new Matrix();
		for(int c=0;c<4;c++) {
			for(int r=0;r<4;r++) {
				matrix.mat[r][c]=mat[r][0]*other.mat[0][c]+mat[r][1]*other.mat[1][c]+
						mat[r][2]*other.mat[2][c]+mat[r][3]*other.mat[3][c];
			}
		}
		return matrix;
	}
	
	 public static Matrix pointAt(Vector3 pos, Vector3 target, Vector3 up) {
		Vector3 forward = target.sub(pos);
		forward=forward.normalize();
		
		Vector3 a = forward.multi(up.dotProd(forward));
		Vector3 newUp = up.sub(a);
		newUp = newUp.normalize();
		Vector3 right = newUp.crossProd(forward);
		Matrix matrix =new Matrix();
		matrix.mat[0][0] = right.getX();	matrix.mat[0][1] = right.getY();	matrix.mat[0][2] = right.getZ();   matrix.mat[0][3] = 0.0;
		matrix.mat[1][0] = newUp.getX();	matrix.mat[1][1] = newUp.getY();	matrix.mat[1][2] = newUp.getZ();   matrix.mat[1][3] = 0.0;
		matrix.mat[2][0] = forward.getX();	matrix.mat[2][1] = forward.getY();	matrix.mat[2][2] = forward.getZ(); matrix.mat[2][3] = 0.0;
		matrix.mat[3][0] = pos.getX();		matrix.mat[3][1] = pos.getY();		matrix.mat[3][2] = pos.getZ();	   matrix.mat[3][3] = 1.0;
		return matrix;
	}
	 
	public Matrix invert() {
		Matrix matrix =new Matrix();
		matrix.mat[0][0] = mat[0][0]; matrix.mat[0][1] = mat[1][0]; matrix.mat[0][2] = mat[2][0]; matrix.mat[0][3] = 0.0;
		matrix.mat[1][0] = mat[0][1]; matrix.mat[1][1] = mat[1][1]; matrix.mat[1][2] = mat[2][1]; matrix.mat[1][3] = 0.0;
		matrix.mat[2][0] = mat[0][2]; matrix.mat[2][1] = mat[1][2]; matrix.mat[2][2] = mat[2][2]; matrix.mat[2][3] = 0.0;
		matrix.mat[3][0] = -(mat[3][0] * matrix.mat[0][0] + mat[3][1] * matrix.mat[1][0] + mat[3][2] * matrix.mat[2][0]);
		matrix.mat[3][1] = -(mat[3][0] * matrix.mat[0][1] + mat[3][1] * matrix.mat[1][1] + mat[3][2] * matrix.mat[2][1]);
		matrix.mat[3][2] = -(mat[3][0] * matrix.mat[0][2] + mat[3][1] * matrix.mat[1][2] + mat[3][2] * matrix.mat[2][2]);
		matrix.mat[3][3] = 1.0;
		return matrix;
	}
	
	public Vector3 multiVec(Vector3 vec) {
		double x = vec.getX()*mat[0][0]+vec.getY()*mat[1][0]+vec.getZ()*mat[2][0]+vec.getW()*mat[3][0];
		double y = vec.getX()*mat[0][1]+vec.getY()*mat[1][1]+vec.getZ()*mat[2][1]+vec.getW()*mat[3][1];
		double z = vec.getX()*mat[0][2]+vec.getY()*mat[1][2]+vec.getZ()*mat[2][2]+vec.getW()*mat[3][2];
		double w = vec.getX()*mat[0][3]+vec.getY()*mat[1][3]+vec.getZ()*mat[2][2]+vec.getW()*mat[3][3];
		return new Vector3(x,y,z,w);
	}
	public static Matrix identity() {
		Matrix matrix =new Matrix();
		matrix.mat[0][0]=1.0;
		matrix.mat[1][1]=1.0;
		matrix.mat[2][2]=1.0;
		matrix.mat[3][3]=1.0;
		return matrix;
	}
	public static Matrix translation(double x,double y, double z) {
		Matrix matrix =new Matrix();
		matrix.mat[0][0]=1.0;
		matrix.mat[1][1]=1.0;
		matrix.mat[2][2]=1.0;
		matrix.mat[3][3]=1.0;
		matrix.mat[3][0]=x;
		matrix.mat[3][1]=y;
		matrix.mat[3][2]=z;
		return matrix;
	}
	public static Matrix rotateX(double angle) {
		Matrix matrix =new Matrix();
		matrix.mat[0][0]=1.0;
		matrix.mat[1][1]=Math.cos(angle);
		matrix.mat[1][2]=Math.sin(angle);
		matrix.mat[2][1]=-Math.sin(angle);
		matrix.mat[2][2]=Math.cos(angle);
		matrix.mat[3][3]=1.0;
		return matrix;
	}
	public static Matrix rotateY(double angle) {
		Matrix matrix =new Matrix();
		matrix.mat[0][0]=Math.cos(angle);
		matrix.mat[0][2]=Math.sin(angle);
		matrix.mat[2][0]=-Math.sin(angle);
		matrix.mat[1][1]=1.0;
		matrix.mat[2][2]=Math.cos(angle);
		matrix.mat[3][3]=1.0;
		return matrix;
	}
	public static Matrix rotateZ(double angle) {
		Matrix matrix =new Matrix();
		matrix.mat[0][0]=Math.cos(angle);
		matrix.mat[0][1]=Math.sin(angle);
		matrix.mat[1][0]=-Math.sin(angle);
		matrix.mat[1][1]=Math.cos(angle);
		matrix.mat[2][2]=1.0;
		matrix.mat[3][3]=1.0;
		return matrix;
	}
	public static Matrix projection(double aspectRatio, double fov, double near, double far) {
		Matrix matrix =new Matrix();
		double fovRad=1.0/Math.tan(Math.toRadians(fov/2.0));
		matrix.mat[0][0]=aspectRatio*fovRad;
		matrix.mat[1][1]=fovRad;
		matrix.mat[2][2]=far/(far-near);
		matrix.mat[3][2]=(-far*near)/(far-near);
		matrix.mat[2][3]=1.0;
		matrix.mat[3][3]=0.0;
		return matrix;
	}
}
