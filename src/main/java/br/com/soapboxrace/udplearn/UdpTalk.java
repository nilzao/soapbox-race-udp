package br.com.soapboxrace.udplearn;

import java.net.DatagramPacket;

public class UdpTalk {

	private byte clientSessionIdx;

	public static UdpTalk startTalk(DatagramPacket receivePacket) {
		return null;
	}

	public void handlePacket(DatagramPacket receivePacket) {
		// TODO Auto-generated method stub
	}

	public byte getClientSessionIdx() {
		return clientSessionIdx;
	}

	public void setClientSessionIdx(byte clientSessionIdx) {
		this.clientSessionIdx = clientSessionIdx;
	}

}
