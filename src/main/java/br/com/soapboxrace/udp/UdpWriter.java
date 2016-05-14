package br.com.soapboxrace.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class UdpWriter {
	private DatagramSocket serverSocket;
	private InetAddress ipAddress;
	private int port;
	private short seqTypeA = 1;
	private short seqTypeB = 0;
	private byte[] sendData;
	private byte[] clientHello;
	private byte clientIdx;

	public UdpWriter(DatagramSocket serverSocket, DatagramPacket receivePacket, byte[] clientHello) {
		this.serverSocket = serverSocket;
		this.clientHello = clientHello;
		setClientIdx();
		if (receivePacket != null) {
			this.ipAddress = receivePacket.getAddress();
			this.port = receivePacket.getPort();
		}
	}

	public void send(byte[] sendData) {
		this.sendData = sendData;
		try {
			this.setCountData();
			DatagramPacket sendPacket = new DatagramPacket(this.sendData, this.sendData.length, ipAddress, port);
			System.out.print("sending: ");
			String byteArrayToHexString = UdpTalk.byteArrayToHexString(this.sendData);
			System.out.println(byteArrayToHexString);
			serverSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setCountData() {
		if (this.sendData[1] == 10 && this.sendData[2] == 10) {
			setCountDataTypeA();
		} else if (this.sendData[2] == 11 && this.sendData[3] == 11) {
			setCountDataTypeB();
		}
	}

	private void setCountDataTypeA() {
		byte[] seqArray = ByteBuffer.allocate(2).putShort(seqTypeA).array();
		sendData[1] = seqArray[0];
		sendData[2] = seqArray[1];
		seqTypeA++;
	}

	private void setCountDataTypeB() {
		byte[] seqArray = ByteBuffer.allocate(2).putShort(seqTypeB).array();
		sendData[2] = seqArray[0];
		sendData[3] = seqArray[1];
		seqTypeB++;
	}

	public int getPort() {
		return port;
	}

	private void setClientIdx() {
		this.clientIdx = clientHello[6];
	}

	public byte getClientIdx() {
		return clientIdx;
	}

}
