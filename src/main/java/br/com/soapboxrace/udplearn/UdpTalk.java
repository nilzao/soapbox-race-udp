package br.com.soapboxrace.udplearn;

import java.math.BigDecimal;
import java.util.Date;

public class UdpTalk {

	private UdpSession udpSession = null;
	private byte sessionClientIdx;
	private long timeStart = 0;
	private long pingTime = 0;
	private UdpWriter udpWriter;
	private int sessionId;
	private byte[] incomeSyncPacket;
	private boolean isSyncStarted = false;
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
		if (incomeSyncPacket[3] == 0x07) {
			return true;
		}
		return false;
	}

	private byte[] syncString() {
		String incomePacket = new String(incomeSyncPacket);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("sync packet: [");
		stringBuilder.append(incomePacket.trim());
		stringBuilder.append("]\n");
		stringBuilder.append("Session started time: [");
		stringBuilder.append(getUdpSession().getDiffTime());
		stringBuilder.append("]\n");
		stringBuilder.append("ping: [");
		stringBuilder.append(pingTime);
		stringBuilder.append("]\n\n");
		return stringBuilder.toString().getBytes();
	}

	private byte pingCalc() {
		long diffTime = getDiffTime();
		if (diffTime > 1000) {
			return (byte) 0xff;
		}
		float pingCalcFloat = 0.254F * diffTime;
		byte pingCalc = new BigDecimal(pingCalcFloat).byteValue();
		return pingCalc;
	}

	private byte[] syncByte() {
		byte[] pingSyncPacket = incomeSyncPacket;
		pingSyncPacket[6] = pingCalc();
		pingSyncPacket[8] = (byte) 0x01;
		pingSyncPacket[9] = (byte) 0x7f;
		pingSyncPacket[10] = (byte) 0xff;
		pingSyncPacket[14] = (byte) 0x00; // from session client Index
		return pingSyncPacket;
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

	public void setIncomeSyncPacket(byte[] incomeSyncPacket) {
		this.incomeSyncPacket = incomeSyncPacket;
		isSyncStarted = true;
		pingTime = getDiffTime();
		try {
			sendFrom(this, getSyncPacket());
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

	public byte[] getSyncPacket() throws Exception {
		if (incomeSyncPacket == null) {
			throw new Exception("Sync timeout");
		}
		isSyncCompleted = true;
		if (isByteSync()) {
			return syncByte();
		}
		return syncString();
	}

	public boolean isSyncStarted() {
		return isSyncStarted;
	}

}
