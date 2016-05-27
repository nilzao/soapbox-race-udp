package br.com.xht.udp.handler;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpWriter {

	private int port;

	private InetAddress address;

	private DatagramSocket serverSocket;

	private UdpDataPacket dataPacket;

	public UdpWriter(UdpDataPacket dataPacket) {
		this.dataPacket = dataPacket;
		address = dataPacket.getAddress();
		port = dataPacket.getPort();
		serverSocket = dataPacket.getServerSocket();
	}

	public void sendPacket(byte[] dataPacket) {
		this.dataPacket.replaceDataBytes(dataPacket);
		this.sendPacket(this.dataPacket);
	}

	public void sendPacket(UdpDataPacket dataPacket) {
		try {
			byte[] sendData = dataPacket.getDataBytes();
			UdpDebug.debugSendPacket(dataPacket);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
			serverSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getPort() {
		return port;
	}

	public InetAddress getAddress() {
		return address;
	}

}
