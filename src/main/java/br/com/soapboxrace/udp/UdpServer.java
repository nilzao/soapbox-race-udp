package br.com.soapboxrace.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;

public class UdpServer {

	private static HashMap<Integer, UdpWriter> udpWriters = new HashMap<Integer, UdpWriter>();
	private static DatagramSocket serverSocket;

	public UdpServer(int port) {
		try {
			serverSocket = new DatagramSocket(port);
			UdpSrvReceive udpSrvReceive = new UdpSrvReceive();
			udpSrvReceive.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws Exception {
		int port = 9998;
		System.out.println("");
		System.out.println("Soapbox race udp server\n");
		System.out.println("to change port run:");
		System.out.println("java -jar soapbox-udp.jar PORT\n");
		System.out.println("Example:");
		System.out.println("  java -jar soapbox-udp.jar 1234");
		if (args.length > 0 && args.length > 1) {
			return;
		} else if (args.length == 1) {
			port = new Integer(args[0]);
		}
		System.out.println("");
		System.out.println("listening on port: [" + port + "]");
		System.out.println("");
		new UdpServer(port);
	}

	private static class UdpSrvReceive extends Thread {

		public void run() {
			byte[] receiveData = new byte[512];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				while (true) {
					serverSocket.receive(receivePacket);
					UdpTalk udpTalk = UdpTalk.getUdpTalk(receivePacket);
					if (udpTalk != null) {
						udpTalk.processReceived(receivePacket);
					}
					for (int i = 0; i < receiveData.length; i++) {
						receiveData[i] = 0;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				serverSocket.close();
			}
		}
	}

	public static HashMap<Integer, UdpWriter> getUdpWriters() {
		return udpWriters;
	}

	public static DatagramSocket getServerSocket() {
		return serverSocket;
	}

}
