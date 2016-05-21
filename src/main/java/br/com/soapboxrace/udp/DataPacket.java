package br.com.soapboxrace.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DataPacket {

	private DatagramPacket receivePacket;
	private DatagramSocket serverSocket;
	private byte[] dataBytes;

	public DataPacket(DatagramSocket serverSocket, DatagramPacket receivePacket) {
		this.serverSocket = serverSocket;
		this.receivePacket = receivePacket;
		int length = receivePacket.getLength();
		dataBytes = new byte[length];
		System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), dataBytes, 0, receivePacket.getLength());
	}

	public void replaceDataBytes(byte[] newDataBytes) {
		dataBytes = newDataBytes;
	}

	public byte[] getDataBytes() {
		return dataBytes;
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
