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

	public UdpTalk(byte sessionClientIdx, byte numberOfClients, int sessionId, UdpWriter udpWriter) {
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

	public void setFirstSessionPacket(byte[] firstSessionPacket) {
		this.firstSessionPacket = firstSessionPacket;
		isSyncStarted = true;
		udpSession = UdpSessions.addUdpTalk(this);
		udpSession.broadcastSyncPackets();
	}

}
