package br.com.soapboxrace.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpWriter {

	private int port;

	private InetAddress address;

	private DatagramSocket serverSocket;

	public UdpWriter(DataPacket dataPacket) {
		address = dataPacket.getAddress();
		port = dataPacket.getPort();
		serverSocket = dataPacket.getServerSocket();
	}

	public void sendPacket(String sendData) {
		this.sendPacket(sendData.getBytes());
	}

	public void sendPacket(byte[] sendData) {
		try {
			sendData = Debug.debugSendPacket(sendData);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
			serverSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getPort() {
		return port;
	}

}
