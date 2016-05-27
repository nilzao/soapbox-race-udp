package br.com.xht.udp.handler;

import java.util.HashMap;

public class UdpSessions {

	private static HashMap<Integer, UdpSession> udpSessions = new HashMap<Integer, UdpSession>();

	public static UdpSession addUdpTalk(IUdpTalk udpTalk) {
		int sessionId = udpTalk.getSessionId();
		UdpSession udpSession = udpSessions.get(sessionId);
		if (udpSession == null) {
			System.out.println("new session [" + udpTalk.getSessionId() + "] added to UdpSessions with UdpTalk");
			udpSession = new UdpSession(udpTalk.getSessionId(), udpTalk.getNumberOfClients(), udpTalk.getTimeStart());
			udpSession.put(udpTalk);
			put(udpSession);
			return udpSession;
		}
		udpSession.put(udpTalk);
		System.out.println("returning [" + udpTalk.getSessionId() + "] from UdpSessions with one more UdpTalk");
		udpSession.broadcastSyncPackets();
		return udpSession;
	}

	private static void put(UdpSession udpSession) {
		udpSessions.put(udpSession.getSessionId(), udpSession);
	}

	public UdpSession remove(UdpSession udpSession) {
		return udpSessions.remove(udpSession.getSessionId());
	}

	public static UdpSession get(int sessionId) {
		return udpSessions.get(sessionId);
	}

}