package br.com.soapboxrace.udplearn;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class UdpSession {

	private HashMap<Integer, UdpTalk> udpTalkers = new HashMap<Integer, UdpTalk>();
	private int sessionId;

	private long timeStart;

	public UdpSession(int sessionId, long timeStart) {
		this.sessionId = sessionId;
		this.timeStart = timeStart;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void put(UdpTalk udpTalk) {
		int clientSessionIdx = (int) udpTalk.getSessionClientIdx();
		udpTalkers.put(clientSessionIdx, udpTalk);
	}

	public void broadcast(UdpTalk udpTalk, byte[] dataPacket) {
		Iterator<Entry<Integer, UdpTalk>> iterator = udpTalkers.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, UdpTalk> next = iterator.next();
			Integer key = next.getKey();
			if (udpTalk.getSessionClientIdx() != key && udpTalk.isSyncStarted()) {
				UdpTalk udpTalkTmp = next.getValue();
				udpTalkTmp.sendFrom(udpTalk, dataPacket);
			}
		}
	}

	public void broadcastSyncPackets() {
		Iterator<Entry<Integer, UdpTalk>> iterator = udpTalkers.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, UdpTalk> next = iterator.next();
			UdpTalk udpTalkTmp = next.getValue();
			try {
				byte[] syncPacket = udpTalkTmp.getSyncPacket();
				broadcast(udpTalkTmp, syncPacket);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}

	public long getDiffTime() {
		long now = new Date().getTime();
		return now - timeStart;
	}

}
