package br.com.xht.udp.handler;

import java.util.Date;

public abstract class UdpTalk implements IUdpTalk {

	private UdpWriter udpWriter;

	private IPacketProcessor packetProcessor;

	private IUdpHello udpHello;

	private long timeStart;

	protected byte[] syncPacket;

	protected boolean isSyncStarted;

	public UdpTalk(IUdpHello udpHello, IPacketProcessor packetProcessor, UdpWriter udpWriter) {
		this.timeStart = new Date().getTime();
		this.udpHello = udpHello;
		this.packetProcessor = packetProcessor;
		this.udpWriter = udpWriter;
	}

	public void sendFrom(IUdpTalk udpTalk, byte[] dataPacket) {
		byte[] processed = packetProcessor.getProcessed(dataPacket, udpTalk.getSessionClientIdx());
		udpWriter.sendPacket(processed);
	}

	protected abstract void parseSyncPacket() throws Exception;

	protected IPacketProcessor getPacketProcessor() {
		return packetProcessor;
	}

	public byte getSessionClientIdx() {
		return udpHello.getSessionClientIdx();
	}

	public boolean isSyncStarted() {
		return isSyncStarted;
	}

	public int getSessionId() {
		return udpHello.getSessionId();
	}

	public byte getNumberOfClients() {
		return udpHello.getNumberOfClients();
	}

	public long getTimeStart() {
		return this.timeStart;
	}

	public long getDiffTime() {
		long now = new Date().getTime();
		return now - timeStart;
	}

	public void broadcast(byte[] dataPacket) {
		UdpSession udpSession = UdpSessions.get(getSessionId());
		udpSession.broadcast(this, dataPacket);
	}

	public void setIncomeSyncPacket(byte[] syncPacket) {
		this.syncPacket = syncPacket;
		try {
			parseSyncPacket();
			UdpSessions.addUdpTalk(this);
			isSyncStarted = true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
