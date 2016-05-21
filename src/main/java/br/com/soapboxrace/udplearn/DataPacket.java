package br.com.soapboxrace.udplearn;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DataPacket {

	private DatagramPacket receivePacket;
	private DatagramSocket serverSocket;

	public DataPacket(DatagramSocket serverSocket, DatagramPacket receivePacket) {
		this.serverSocket = serverSocket;
		this.receivePacket = receivePacket;
	}

	public byte[] getDataBytes() {
		int length = receivePacket.getLength();
		byte[] data = new byte[length];
		System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), data, 0, receivePacket.getLength());
		return data;
	}

	public String getDataString() {
		return new String(getDataBytes());
	}

	public int getPort() {
		return receivePacket.getPort();
	}

	public InetAddress getAddress() {
		return receivePacket.getAddress();
	}

	public DatagramSocket getServerSocket() {
		return serverSocket;
	}

}
