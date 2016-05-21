package br.com.soapboxrace.udplearn;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class UdpSession {

	private HashMap<Integer, UdpTalk> udpTalkers = new HashMap<Integer, UdpTalk>();
	private int sessionId;

	public UdpSession(int sessionId) {
		this.sessionId = sessionId;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void put(UdpTalk udpTalk) {
		int clientSessionIdx = (int) udpTalk.getSessionClientIdx();
		udpTalkers.put(clientSessionIdx, udpTalk);
	}

	public void broadcast(UdpTalk udpTalk, DataPacket dataPacket) {
		Iterator<Entry<Integer, UdpTalk>> iterator = udpTalkers.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, UdpTalk> next = iterator.next();
			Integer key = next.getKey();
			if (udpTalk.getSessionClientIdx() != key) {
				UdpTalk udpTalkTmp = next.getValue();
				UdpWriter udpWriter = udpTalkTmp.getUdpWriter();
				// DataPacket transformPacket =
				// udpTalk.transformPacket(dataPacket);
				DataPacket transformPacket = dataPacket;
				udpWriter.sendPacket(transformPacket.getDataBytes());
			}
		}
	}

}
