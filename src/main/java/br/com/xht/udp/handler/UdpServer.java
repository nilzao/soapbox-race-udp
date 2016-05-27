package br.com.xht.udp.handler;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServer {

	private DatagramSocket serverSocket;
	private int port;
	private UdpHandler udpHandler;

	public UdpServer(int port, IUdpProtocol udpProtocol) {
		this.port = port;
		udpHandler = new UdpHandler(udpProtocol);
	}

	public void start() {
		try {
			serverSocket = new DatagramSocket(port);
			UdpSrvReceive udpSrvReceive = new UdpSrvReceive(serverSocket, udpHandler);
			udpSrvReceive.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class UdpSrvReceive extends Thread {

		private DatagramSocket serverSocket;
		private UdpHandler udpHandler;

		public UdpSrvReceive(DatagramSocket serverSocket, UdpHandler udpHandler) {
			this.serverSocket = serverSocket;
			this.udpHandler = udpHandler;
		}

		public void run() {
			byte[] receiveData = new byte[2048];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				while (true) {
					serverSocket.receive(receivePacket);
					UdpDataPacket dataPacket = new UdpDataPacket(serverSocket, receivePacket);
					UdpDebug.debugReceivePacket(dataPacket);
					udpHandler.handlePacket(dataPacket);
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
