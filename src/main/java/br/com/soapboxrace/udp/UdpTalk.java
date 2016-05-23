package br.com.soapboxrace.udp;

import java.util.Date;

public class UdpTalk {

	private UdpSession udpSession = null;
	private byte sessionClientIdx;
	private byte numberOfClients;
	private long timeStart = 0;
	private UdpWriter udpWriter;
	private int sessionId;
	private byte[] firstSessionPacket;
	private boolean isSyncStarted = false;
	private PacketProcessor packetProcessor = new PacketProcessor();
	private byte[] helloPacket;

	public UdpTalk(byte[] helloPacket, byte sessionClientIdx, byte numberOfClients, int sessionId,
			UdpWriter udpWriter) {
		this.helloPacket = helloPacket;
		this.sessionClientIdx = sessionClientIdx;
		this.sessionId = sessionId;
		this.udpWriter = udpWriter;
		this.numberOfClients = numberOfClients;
		timeStart = new Date().getTime();
	}

	public UdpSession getUdpSession() {
		return udpSession;
	}

	public byte getSessionClientIdx() {
		return sessionClientIdx;
	}

	public UdpWriter getUdpWriter() {
		return udpWriter;
	}

	public void sendFrom(UdpTalk udpTalk, byte[] sendData) {
		byte[] processed = packetProcessor.getProcessed(sendData, udpTalk.getSessionClientIdx());
		getUdpWriter().sendPacket(processed);
	}

	public int getSessionId() {
		return sessionId;
	}

	public long getTimeStart() {
		return timeStart;
	}

	public boolean isSyncStarted() {
		return isSyncStarted;
	}

	public byte getNumberOfClients() {
		return numberOfClients;
	}

	public byte[] getFirstSessionPacket() {
		return firstSessionPacket;
	}

	// somehow, sometime at start, this package is broadcasted
	// again changing pingCalc and packageLoss
	public void setFirstSessionPacket(byte[] firstSessionPacket) {
		firstSessionPacket = packetProcessor.getProcessed(firstSessionPacket, sessionClientIdx);
		firstSessionPacket[4] = (byte) 0xa7;
		// firstSessionPacket[5] = (byte) 0x00; // pingCalc
		firstSessionPacket[6] = helloPacket[2];
		// firstSessionPacket[7] = (byte) 0x00; // packageLoss??
		firstSessionPacket[8] = (byte) 0x00;
		firstSessionPacket[9] = (byte) 0x01;
		firstSessionPacket[10] = (byte) 0x7f;
		firstSessionPacket[11] = (byte) 0xff;
		this.firstSessionPacket = firstSessionPacket;
		isSyncStarted = true;
		udpSession = UdpSessions.addUdpTalk(this);
		udpSession.broadcastSyncPackets();
	}

}
