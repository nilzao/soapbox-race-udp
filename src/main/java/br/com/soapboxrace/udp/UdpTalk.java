package br.com.soapboxrace.udp;

import java.math.BigDecimal;
import java.util.Date;

public class UdpTalk {

	private UdpSession udpSession = null;
	private byte sessionClientIdx;
	private byte numberOfClients;
	private long timeStart = 0;
	private UdpWriter udpWriter;
	private int sessionId;
	private byte[] incomeSyncPacket;
	private boolean isSyncStarted = false;
	private PacketProcessor packetProcessor = new PacketProcessor();

	public UdpTalk(byte sessionClientIdx, byte numberOfClients, int sessionId, UdpWriter udpWriter) {
		this.sessionClientIdx = sessionClientIdx;
		this.sessionId = sessionId;
		this.udpWriter = udpWriter;
		this.numberOfClients = numberOfClients;
		timeStart = new Date().getTime();
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
		stringBuilder.append("Session [");
		stringBuilder.append(udpSession.getSessionId());
		stringBuilder.append("] started time: [");
		stringBuilder.append(udpSession.getDiffTime());
		stringBuilder.append("]\n");
		return stringBuilder.toString().getBytes();
	}

	private byte pingCalc() {
		long diffTime = udpSession.getDiffTime();
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
		// pingSyncPacket[8] = (byte) 0x01;
		// pingSyncPacket[9] = (byte) 0x7f;
		// pingSyncPacket[10] = (byte) 0xff;
		// pingSyncPacket[14] = (byte) 0x00; // from session client Index
		pingSyncPacket[(pingSyncPacket.length - 6)] = 0x03;
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
		udpSession = UdpSessions.addUdpTalk(this);
		try {
			sendFrom(this, getSyncPacket());
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		udpSession.broadcastSyncPackets();
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

	public byte[] getSyncPacket() throws Exception {
		if (incomeSyncPacket == null) {
			throw new Exception("Sync timeout");
		}
		if (isByteSync()) {
			return syncByte();
		}
		return syncString();
	}

	public boolean isSyncStarted() {
		return isSyncStarted;
	}

	public byte getNumberOfClients() {
		return numberOfClients;
	}

}
