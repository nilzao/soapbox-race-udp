package br.com.soapboxrace.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class UdpWriter {
	private DatagramSocket serverSocket;
	private InetAddress ipAddress;
	private int port;
	private short seqTypeA = 1;
	private short seqTypeB = 0;
	private short seqHandshake = 0;
	private byte[] sendData;
	private byte[] clientHello;
	private byte clientIdx;
	private byte[] helloPacket;
	private byte[] sessionId;
	private byte[] handShakePacket;

	public UdpWriter(DatagramSocket serverSocket, DatagramPacket receivePacket, byte[] clientHello) {
		this.serverSocket = serverSocket;
		this.clientHello = clientHello;
		this.helloPacket = Arrays.copyOfRange(clientHello, 5, 9);
		this.sessionId = Arrays.copyOfRange(clientHello, 9, 13);
		setClientIdx();
		if (receivePacket != null) {
			this.ipAddress = receivePacket.getAddress();
			this.port = receivePacket.getPort();
		}
	}

	private boolean isSessionPacket() {
		int pos = sendData.length - 10;
		if (sendData[pos++] == sessionId[0] && sendData[pos++] == sessionId[1] && sendData[pos++] == sessionId[2]
				&& sendData[pos++] == sessionId[3]) {
			return true;
		}
		return false;
	}

	private void handShake() {
		if (this.isSessionPacket() && seqHandshake == 0) {
			this.sendData[(this.sendData.length - 6)] = 3;
			handShakePacket = this.sendData;
			handShakePacket[4] = (byte) 0xa9;
			this.sendPacket();
			seqHandshake++;
		}
		if (seqHandshake > 0 && seqHandshake < 50) {
			sendPacket();
			seqHandshake++;
		}
		if (seqHandshake == 50) {
			this.sendData = handShakePacket;
			sendPacket();
			seqHandshake++;
		}
	}

	private void sendPacket() {
		try {
			DatagramPacket sendPacket = new DatagramPacket(this.sendData, this.sendData.length, ipAddress, port);
			// System.out.print("sending: ");
			// String byteArrayToHexString =
			// UdpTalk.byteArrayToHexString(this.sendData);
			// System.out.println(byteArrayToHexString);
			serverSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void send(byte[] sendData) {
		this.sendData = sendData;
		this.handShake();
		this.setCountData();
		this.sendPacket();
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
		this.clientIdx = clientHello[4];
	}

	public byte getClientIdx() {
		return clientIdx;
	}

	public byte[] getHelloPacket() {
		return helloPacket;
	}

	public byte[] getSessionId() {
		return sessionId;
	}

}
