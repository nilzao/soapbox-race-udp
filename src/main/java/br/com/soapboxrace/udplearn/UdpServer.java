package br.com.soapboxrace.udplearn;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServer {

	private DatagramSocket serverSocket;

	public UdpServer(int port) {
		try {
			serverSocket = new DatagramSocket(port);
			UdpSrvReceive udpSrvReceive = new UdpSrvReceive(serverSocket);
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

		private DatagramSocket serverSocket;

		public UdpSrvReceive(DatagramSocket serverSocket) {
			this.serverSocket = serverSocket;
		}

		public void run() {
			byte[] receiveData = new byte[512];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				while (true) {
					serverSocket.receive(receivePacket);
					DataPacket dataPacket = new DataPacket(serverSocket, receivePacket);
					Debug.debugReceivePacket(dataPacket);
					UdpHandler.handlePacket(dataPacket);
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
}
