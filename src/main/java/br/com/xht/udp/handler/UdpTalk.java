package br.com.xht.udp.handler;

import java.util.Date;

public abstract class UdpTalk implements IUdpTalk, Comparable<UdpTalk> {

	private UdpWriter udpWriter;

	private IPacketProcessor packetProcessor;

	private IUdpHello udpHello;

	private long timeStart;

	private long ping;

	protected byte[] syncPacket;

	public UdpTalk(IUdpHello udpHello, IPacketProcessor packetProcessor, UdpWriter udpWriter) {
		this.timeStart = new Date().getTime();
		this.udpHello = udpHello;
		this.packetProcessor = packetProcessor;
		this.udpWriter = udpWriter;
	}

	public void sendFrom(IUdpTalk udpTalk, byte[] dataPacket) {
		byte[] processed = packetProcessor.getProcessed(dataPacket);
		if (processed != null) {
			udpWriter.sendPacket(processed);
		}
	}

	protected abstract void parseSyncPacket() throws Exception;

	protected IPacketProcessor getPacketProcessor() {
		return packetProcessor;
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

	public long getPing() {
		return ping;
	}

	protected byte[] getHelloPacket() {
		return udpHello.getHelloPacket();
	}

	public int getPersonaId() {
		return udpHello.getPersonaId();
	}

	@Override
	public int compareTo(UdpTalk udpTalk) {
		if (ping < udpTalk.getPing()) {
			return -1;
		}
		if (ping > udpTalk.getPing()) {
			return 1;
		}
		return 0;
	}
}
