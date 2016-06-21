package br.com.xht.udp.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class UdpSession {

	private HashMap<Integer, IUdpTalk> udpTalkers = new HashMap<Integer, IUdpTalk>();
	private int sessionId;

	public UdpSession(int sessionId) {
		this.sessionId = sessionId;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void put(IUdpTalk udpTalk) {
		int personaId = (int) udpTalk.getPersonaId();
		udpTalkers.put(personaId, udpTalk);
	}

	public void broadcast(IUdpTalk udpTalk, byte[] dataPacket) {
		Iterator<Entry<Integer, IUdpTalk>> iterator = udpTalkers.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, IUdpTalk> next = iterator.next();
			Integer key = next.getKey();
			Integer personaId = udpTalk.getPersonaId();
			if (!personaId.equals(key)) {
				IUdpTalk udpTalkTmp = next.getValue();
				udpTalkTmp.sendFrom(udpTalk, dataPacket);
			}
		}
	}

}
