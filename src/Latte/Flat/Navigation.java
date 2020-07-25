package Latte.Flat;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

public class Navigation {
	private Node[] nodes;
	
	private Node start;
	private Node end;
	private Vector savedStart= new Vector();
	private Vector savedEnd= new Vector();
	private Vector offset= new Vector();
	private int w=0;
	private int h=0;
	
	public void setupGrid(int w, int h, Vector offset, Vector spacer) {
		this.w=w;
		this.h=h;
		this.offset=offset;
		nodes=new Node[w*h];
		for(int x=0;x<w;x++) {
			for(int y=0;y<h;y++) {
				double xShift=(x*spacer.getX())+offset.getX();
				double yShift=(y*spacer.getY())+offset.getY();
				nodes[y*w+x] = new Node(xShift,yShift);
			}
		}
		for(int x=0;x<w;x++) {
			for(int y=0;y<h;y++) {
				if(y>0) nodes[y*w+x].neighbour.add(nodes[(y-1)*w+x]);
				if(y<h-1) nodes[y*w+x].neighbour.add(nodes[(y+1)*w+x]);
				if(x>0) nodes[y*w+x].neighbour.add(nodes[y*w+(x-1)]);
				if(x<w-1) nodes[y*w+x].neighbour.add(nodes[y*w+(x+1)]);
				
				if(y>0&&x>0) nodes[y*w+x].neighbour.add(nodes[(y-1)*w+(x-1)]);
				if(y>0&&x<w-1) nodes[y*w+x].neighbour.add(nodes[(y-1)*w+(x+1)]);
				if(x>0&&y<h-1) nodes[y*w+x].neighbour.add(nodes[(y+1)*w+(x-1)]);
				if(x<w-1&&y<h-1) nodes[y*w+x].neighbour.add(nodes[(y+1)*w+(x+1)]);
			}
		}
	}
	public void updateGrid(Vector pos,Vector spacer) {
		for(int x=0;x<w;x++) {
			for(int y=0;y<h;y++) {
				double xShift=(x*spacer.getX())+pos.getX();
				double yShift=(y*spacer.getY())+pos.getY();
				nodes[y*w+x].pos= offset.add(new Vector(xShift,yShift));
			}
		}
	}
	public Vector getParent() {
		if(end.parent==null) return null;
		return end.parent.pos;
	}
	public Vector getStart() {
		return savedStart;
	}
	public Vector getEnd() {
		return savedEnd;
	}
	public void debugPath(Graphics g) {
		Node p = end;
		while(p.parent!=null) {
			g.drawLine((int)p.pos.getX(),(int)p.pos.getY(),(int)p.parent.pos.getX(),(int)p.parent.pos.getY());
			p=p.parent;
		}
	}
	public Node find(double x, double y) {
		int min = Integer.MAX_VALUE;
		int indx=0;
		for(int i =0;i<nodes.length;i++) {
			int dist=(int)nodes[i].pos.getDistance(new Vector(x,y));
			if(dist<min) {
				min=dist;
				indx=i;
			}
		}
		return nodes[indx];
	}
	public void setStart(double x, double y) {
		start=find(x,y);
		savedStart = new Vector(x,y);
	}
	public void setEnd(double x, double y) {
		end=find(x,y);
		savedEnd = new Vector(x,y);
	}
	public void setObstacale(int x, int y, boolean obstacale) {
		nodes[y*w+x].obstacale=obstacale;
	}
	public void solve() {
		for(int x=0;x<w;x++) {
			for(int y=0;y<h;y++) {
				nodes[y*w+x].visted=false;
				nodes[y*w+x].global=Integer.MAX_VALUE;
				nodes[y*w+x].local=Integer.MAX_VALUE;
				nodes[y*w+x].parent=null;
			}
		}
		Node current = start;
		start.local=0;
		start.global = start.heuristic(end);
		
		ArrayList<Node> notTested = new ArrayList<Node>();
		notTested.add(start);
		
		while(!notTested.isEmpty()&&current!=end) {
			Collections.sort(notTested);
			
			while(!notTested.isEmpty() && notTested.get(0).visted)
				notTested.remove(0);
			
			if(notTested.isEmpty()) break;
			
			current = notTested.get(0);
			current.visted=true;
			
			for(Node neighbour: current.neighbour) {
				double goal=current.local+current.pos.getDistance(neighbour.pos);
				if(goal<neighbour.local) {
					neighbour.parent=current;
					neighbour.local=goal;
					neighbour.global=neighbour.local+neighbour.heuristic(end);
				}
				if(!neighbour.visted&&!neighbour.obstacale)
					notTested.add(neighbour);
			}
		}
	}
	public void debugBlocks(Graphics g) {
		for(int x=0;x<w;x++) {
			for(int y=0;y<h;y++) {
				int r=0;
				int gr=0;
				if(nodes[y*w+x].visted) r=255;
				if(nodes[y*w+x].obstacale) gr=255;
				g.setColor(new Color(r,gr,0,150));
				g.fillRect((int)nodes[y*w+x].pos.getX(),(int)nodes[y*w+x].pos.getY(),32,32);
			}
		}
	}
	public Vector getGridStart() {
		return nodes[0].pos;
	}
	public Vector getGridEnd() {
		return nodes[nodes.length-1].pos;
	}
}
class Node implements Comparable<Node> {
	boolean obstacale=false;
	boolean visted=false;
	double local=0;
	double global=0;
	Vector pos = new Vector();
	ArrayList<Node> neighbour = new ArrayList<Node>();
	Node parent;
	Node(){}
	Node(double x, double y) {pos.setPos(x,y);}
	double heuristic(Node dist) {
		return pos.getDistance(dist.pos);
	}

	@Override
	public int compareTo(Node n2) {
		if(global>n2.global)return 1;
		if(global<n2.global)return -1;
		return 0;
	}
}