package Latte;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;


public class Server {
	private static DatagramSocket socket;
	private static boolean recieving=true;
	private static String inData="";
	private static String outData="";
	private static ArrayList<ClientInfo> clients = new ArrayList<ClientInfo>();
	public static void start(int port){
		try {
			socket=new DatagramSocket(port);
			recieve();
		} catch (SocketException e) {}
	}
	public static boolean serverExists(String host,int port) { 
	    try (Socket s = new Socket(host,port)) {
	        return true;
	    } catch (IOException ex) {
	    	 return false;
	    }
	}
	protected static void recieve() {
		Thread thread = new Thread() {
			public void run() {
				while (recieving) {
					byte[] rdata = new byte[1024];
					try {
						socket.receive(new DatagramPacket(rdata,rdata.length));
						String msg = new String(rdata).trim();
						String msg2=msg.substring(0,msg.indexOf("/e/"));
						if(checkClient(msg2))broadcast(msg2);
						outData+=msg;
					} catch (IOException e) {
						System.out.println("Failed to recieve message");
					}
				}
			}
		};
		thread.start();
	}
	public static void accumulate(String msg) {
		inData+=msg;
	}
	public static String getRecieved() {
		return outData.substring(0,outData.indexOf("/e/"));
	}
	public static void loadNext() {
		outData=outData.substring(outData.indexOf("/e/"));
	}
	public static void broadcast(String msg) {
		for(ClientInfo client: clients) {
				send(msg,client.host,client.port);
		}
	}
	public static boolean hasData() {
		return !outData.isEmpty();
	}
	
	public static void broadcastAccumulated(String host, int port) {
		while(inData.length()>=1021) {
			broadcast(inData.substring(0,1020));
			inData=inData.substring(1020);
		}
		broadcast(inData);
		inData="";
	}
	
	public static void sendAccumulated(String host, int port) {
		while(inData.length()>=1021) {
			send(inData.substring(0,1020),host,port);
			inData=inData.substring(1020);
		}
		send(inData,host,port);
		inData="";
	}
	
	public static void send (String msg,String host, int port) {
		try {
			msg+="/e/";
			byte[] data = msg.getBytes();
			DatagramPacket packet = new DatagramPacket(data,data.length, InetAddress.getByName(host),port);
			socket.send(packet);
		} catch (Exception e) {
			System.out.println("No client found or failed to send message");
		}
		
	}
	private static boolean checkClient(String data) {
		if(data.startsWith("\\con")) {
			System.out.println("A new player connected");
			String[] split=data.split(" ");
			clients.add(new ClientInfo(split[1],split[2]));
			return false;
		}
		else if(data.startsWith("\\disc")) {
			System.out.println("A new player disconnected");
			String[] split=data.split(" ");
			clients.remove(new ClientInfo(split[1],split[2]));
			return false;
		}
		return true;
	}
}
class ClientInfo {
	public ClientInfo(String host, String port) {
		this.host=host;
		this.port=Integer.parseInt(port);
	}
	String host ="";
	int port=0;	
}