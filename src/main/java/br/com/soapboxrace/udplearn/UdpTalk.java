package br.com.soapboxrace.udplearn;

import java.util.Date;

public class UdpTalk {

	private UdpSession udpSession = null;

	private byte sessionClientIdx;

	private long timeStart = 0;

	private UdpWriter udpWriter;

	public UdpTalk(UdpSession udpSession, byte sessionClientIdx, UdpWriter udpWriter) {
		this.udpSession = udpSession;
		this.sessionClientIdx = sessionClientIdx;
		this.udpWriter = udpWriter;
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

	public int getSessionId() {
		if (udpSession != null) {
			return udpSession.getSessionId();
		}
		return 0;
	}
}
