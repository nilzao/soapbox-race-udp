package br.com.soapboxrace.udplearn;

import java.util.Date;

public class UdpHello {

	private byte sessionClientIdx;

	private DataPacket helloPacket = null;

	private long timeStart = 0;

	private UdpSession udpSession = null;

	private int sessionId = 0;

	private UdpWriter udpWriter;

	public static UdpHello startTalk(DataPacket dataPacket) {
		UdpHello udpTalk = new UdpHello();
		udpTalk.helloPacket = dataPacket;
		try {
			udpTalk.timeStart = new Date().getTime();
			udpTalk.setHelloPacket();
			udpTalk.setSessionClientIdx();
			udpTalk.setUdpSession();
			udpTalk.setUdpWriter();
			udpTalk.sendWelcome();
		} catch (Exception e) {
			System.err.println("NOT A VALID HELLO PACKET!");
			System.err.println(e.getMessage());
			udpTalk = null;
		}
		return udpTalk;
	}

	private void sendWelcome() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\n");
		stringBuilder.append("Welcome to udp server learning");
		stringBuilder.append("\n");
		stringBuilder.append("Your SessionID: ");
		stringBuilder.append(sessionId);
		stringBuilder.append("\n");
		stringBuilder.append("Your Session Client Index: ");
		stringBuilder.append(sessionClientIdx);
		stringBuilder.append("\n");
		stringBuilder.append("Your Client Port: ");
		stringBuilder.append(helloPacket.getPort());
		stringBuilder.append("\n");
		stringBuilder.append("you have 10 seconds to send some packets asap to detect your ping, have fun!");
		stringBuilder.append("\n");
		stringBuilder.append("\n");
		udpWriter.sendPacket(stringBuilder.toString());
	}

	public DataPacket transformPacket(DataPacket dataPacket) {
		return dataPacket;
	}

	public byte getSessionClientIdx() {
		return sessionClientIdx;
	}

	private void setHelloPacket() throws Exception {
		String dataString = helloPacket.getDataString();
		if (!dataString.contains("hello!")) {
			throw new Exception("invalid hello");
		}
	}

	private void setSessionClientIdx() throws Exception {
		String dataString = helloPacket.getDataString();
		if (!dataString.contains("sidx:")) {
			throw new Exception("invalid session client index");
		}
		int indexOf = dataString.indexOf("sidx:");
		String sidxStr = dataString.substring(indexOf + 5, indexOf + 6);
		sessionClientIdx = Byte.valueOf(sidxStr);
	}

	private void setUdpSession() throws Exception {
		String dataString = helloPacket.getDataString();
		dataString = dataString.trim();
		if (!dataString.contains("sid:")) {
			throw new Exception("invalid session id");
		}
		int indexOf = dataString.indexOf("sid:");
		String sessionIdStr = dataString.substring(indexOf + 4, dataString.length());
		sessionId = Integer.valueOf(sessionIdStr);
		udpSession = UdpSessions.addUdpTalk(this);
	}

	public UdpWriter getUdpWriter() {
		return udpWriter;
	}

	private void setUdpWriter() {
		this.udpWriter = new UdpWriter(helloPacket);
	}

	public UdpSession getUdpSession() {
		return udpSession;
	}

	public int getSessionId() {
		return sessionId;
	}

	public long getDiffTime() {
		long now = new Date().getTime();
		return now - timeStart;
	}

}
