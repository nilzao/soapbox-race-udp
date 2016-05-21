package br.com.soapboxrace.udplearn;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class UdpSession {

	private HashMap<Integer, UdpHello> udpTalkers = new HashMap<Integer, UdpHello>();
	private int sessionId;

	public UdpSession(int sessionId, UdpHello udpTalk) {
		this.sessionId = sessionId;
		this.put(udpTalk);
	}

	public int getSessionId() {
		return sessionId;
	}

	public UdpHello put(UdpHello udpTalk) {
		int clientSessionIdx = (int) udpTalk.getSessionClientIdx();
		return udpTalkers.put(clientSessionIdx, udpTalk);
	}

	public void broadcast(UdpHello udpTalk, DataPacket dataPacket) {
		Iterator<Entry<Integer, UdpHello>> iterator = udpTalkers.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, UdpHello> next = iterator.next();
			Integer key = next.getKey();
			if (udpTalk.getSessionClientIdx() != key) {
				UdpHello udpTalkTmp = next.getValue();
				UdpWriter udpWriter = udpTalkTmp.getUdpWriter();
				DataPacket transformPacket = udpTalk.transformPacket(dataPacket);
				udpWriter.sendPacket(transformPacket.getDataBytes());
			}
		}
	}

}
