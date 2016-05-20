package br.com.soapboxrace.udplearn;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpWriter {

	private DatagramSocket serverSocket;
	private InetAddress ipAddress;
	private int port;

	public UdpWriter(DatagramSocket serverSocket, DatagramPacket receivePacket) {
		if (receivePacket != null) {
			this.ipAddress = receivePacket.getAddress();
			this.port = receivePacket.getPort();
		}
	}

	public void sendPacket(String sendData) {
		this.sendPacket(sendData.getBytes());
	}

	public void sendPacket(byte[] sendData) {
		try {
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
			serverSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
