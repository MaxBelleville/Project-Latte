package Latte;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Client {
	private static boolean recieving=true;
	private static DatagramSocket socket;
	private static String clientIp="localhost";
	private static int clientPort =8000;
	private static String host="localhost";
	private static int port =8000;
	private static String inData="";
	private static String outData="";
	
	
	public static void start(){
		try {
			socket=new DatagramSocket();
			clientIp=socket.getLocalAddress().toString();
			if(clientIp.startsWith("0.0"))clientIp="localhost";
			clientPort=socket.getLocalPort();
			recieve();
		} catch (SocketException e) {}
	}
	
	public static void connect(String host, int port) {
		Client.host=host;
		Client.port=port;
		send("\\con "+clientIp+" "+clientPort);
	}
	public static void disconnect(String host, int port) {
		send("\\disc "+host+" "+port);
	}
	public static void accumulate(String msg) {
		inData+=msg;
		while(inData.length()>=1021) {
			send(inData.substring(0,1020));
			inData=inData.substring(1020);
		}
	}
	
	public static void sendAccumulated() {
		send(inData);
		inData="";
	}
	
	public static String getRecieved() {
		return outData.substring(0,outData.indexOf("/e/"));
	}
	public static void loadNext() {
		outData=outData.substring(outData.indexOf("/e/")+3);
	}
	
	public static void send (String msg) {
		try {
			msg+="/e/";
			byte[] data = msg.getBytes();
			DatagramPacket packet = new DatagramPacket(data,data.length, InetAddress.getByName(host),port);
			socket.send(packet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected static void recieve() {
		Thread thread = new Thread() {
			public void run() {
				while (recieving) {
			byte[] rdata = new byte[1024];
			try {
				socket.receive(new DatagramPacket(rdata,rdata.length));
				outData+= new String(rdata).trim();
			} catch (IOException e) {
				System.out.println("Failed to recieve message");
			}
				}
			}
		};
		thread.start();
	}

	public static boolean hasData() {
		return !outData.isEmpty();
	}

}