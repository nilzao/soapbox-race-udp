package br.com.soapboxrace.udplearn;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpWriter {

	private int port;

	private InetAddress address;

	private DatagramSocket serverSocket;

	private DataPacket dataPacket;

	public UdpWriter(DataPacket dataPacket) {
		this.dataPacket = dataPacket;
		address = dataPacket.getAddress();
		port = dataPacket.getPort();
		serverSocket = dataPacket.getServerSocket();
	}

	public void sendPacket(String sendData) {
		this.sendPacket(sendData.getBytes());
	}

	public void sendPacket(byte[] sendData) {
		try {
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
			Debug.debugSendPacket(dataPacket);
			serverSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getPort() {
		return port;
	}

}
