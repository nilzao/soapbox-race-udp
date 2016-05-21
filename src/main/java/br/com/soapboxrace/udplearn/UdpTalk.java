package br.com.soapboxrace.udplearn;

import java.util.Date;

public class UdpTalk {

	private UdpSession udpSession = null;
	private byte sessionClientIdx;
	private long timeStart = 0;
	private long pingTime = 0;
	private UdpWriter udpWriter;
	private int sessionId;
	private byte[] incomeSyncPacket;
	private boolean isSyncCompleted = false;
	private PacketProcessor packetProcessor = new PacketProcessor();

	public UdpTalk(byte sessionClientIdx, int sessionId, UdpWriter udpWriter) {
		this.sessionClientIdx = sessionClientIdx;
		this.sessionId = sessionId;
		this.udpWriter = udpWriter;
		timeStart = new Date().getTime();
		udpSession = UdpSessions.addUdpTalk(this);
	}

	private boolean isByteSync() {

		return false;
	}

	public void sendSync() throws Exception {
		if (incomeSyncPacket == null) {
			throw new Exception("Sync timeout");
		}
		if (isByteSync()) {
			syncByte();
		} else {
			syncString();
		}
		isSyncCompleted = true;
	}

	private void syncString() {
		String incomePacket = new String(incomeSyncPacket);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("sync packet: [");
		stringBuilder.append(incomePacket.trim());
		stringBuilder.append("]\n");
		stringBuilder.append("Session started time: [");
		stringBuilder.append(getUdpSession().getDiffTime());
		stringBuilder.append("]\n");
		stringBuilder.append("pingTime: [");
		stringBuilder.append(pingTime);
		stringBuilder.append("]\n\n");
		getUdpWriter().sendPacket(stringBuilder.toString());
	}

	private byte pingCalc() {
		byte pingCalc = 0x00;
		return pingCalc;
	}

	private void syncByte() {
		byte[] pingSyncPacket = incomeSyncPacket;
		pingSyncPacket[6] = pingCalc();
		pingSyncPacket[8] = (byte) 0x01;
		pingSyncPacket[9] = (byte) 0x7f;
		pingSyncPacket[10] = (byte) 0xff;
		pingSyncPacket[14] = (byte) 0x00; // from session client Index
		getUdpWriter().sendPacket(pingSyncPacket);
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

	public void setPingTime(byte[] incomeSyncPacket) {
		this.incomeSyncPacket = incomeSyncPacket;
		pingTime = getDiffTime();
		try {
			sendSync();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public long getPingTime() {
		return pingTime;
	}

	public long getDiffTime() {
		long now = new Date().getTime();
		return now - timeStart;
	}

	public void sendFrom(UdpTalk udpTalk, byte[] sendData) {
		byte[] processed = packetProcessor.getProcessed(sendData, udpTalk.getSessionClientIdx());
		getUdpWriter().sendPacket(processed);
	}

	public int getSessionId() {
		return sessionId;
	}

	public boolean isSyncCompleted() {
		return isSyncCompleted;
	}

	public long getTimeStart() {
		return timeStart;
	}

}
