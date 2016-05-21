package br.com.soapboxrace.udplearn;

import java.util.Date;

public class UdpTalk {

	private UdpSession udpSession = null;

	private byte sessionClientIdx;

	private long timeStart = 0;

	private UdpWriter udpWriter;

	private int sessionId;

	public UdpTalk(byte sessionClientIdx, int sessionId, UdpWriter udpWriter) {
		this.sessionClientIdx = sessionClientIdx;
		this.sessionId = sessionId;
		this.udpWriter = udpWriter;
		this.udpSession = UdpSessions.addUdpTalk(this);
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

	public long getDiffTime() {
		long now = new Date().getTime();
		return now - timeStart;
	}

	public void sendFrom(UdpTalk udpTalk, byte[] sendData) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("from [");
		stringBuffer.append(udpTalk.getSessionClientIdx());
		stringBuffer.append("] ");
		stringBuffer.append(new String(sendData));
		getUdpWriter().sendPacket(stringBuffer.toString().getBytes());
	}

	public int getSessionId() {
		return sessionId;
	}
}
