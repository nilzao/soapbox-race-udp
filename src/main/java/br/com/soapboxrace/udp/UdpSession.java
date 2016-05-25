package br.com.soapboxrace.udp;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class UdpSession {

	private HashMap<Integer, UdpTalk> udpTalkers = new HashMap<Integer, UdpTalk>();
	private int sessionId;
	private byte numberOfClients;
	private long timeStart;
	private boolean isSyncDone = false;

	public UdpSession(int sessionId, long timeStart, byte numberOfClients) {
		this.sessionId = sessionId;
		this.timeStart = timeStart;
		this.numberOfClients = numberOfClients;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void put(UdpTalk udpTalk) {
		int clientSessionIdx = (int) udpTalk.getSessionClientIdx();
		udpTalkers.put(clientSessionIdx, udpTalk);
	}

	public void broadcast(UdpTalk udpTalk, byte[] dataPacket) {
		if (isSyncDone) {
			Iterator<Entry<Integer, UdpTalk>> iterator = udpTalkers.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, UdpTalk> next = iterator.next();
				Integer key = next.getKey();
				Integer sessionClientIdx = (int) udpTalk.getSessionClientIdx();
				if (!sessionClientIdx.equals(key)) {
					UdpTalk udpTalkTmp = next.getValue();
					udpTalkTmp.sendFrom(udpTalk, dataPacket);
				}
			}
		}
	}

	public void broadcastSyncPackets() {
		if (isFull()) {
			for (int i = 0; i < udpTalkers.size(); i++) {
				UdpTalk udpTalk = udpTalkers.get(i);
				try {
					broadcastSyncPackets(udpTalk, udpTalk.getSyncPacket());
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println(e.getMessage());
				}
			}
			isSyncDone = true;
		}
	}

	private void broadcastSyncPackets(UdpTalk udpTalk, byte[] dataPacket) {
		Iterator<Entry<Integer, UdpTalk>> iterator = udpTalkers.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, UdpTalk> next = iterator.next();
			Integer key = next.getKey();
			Integer sessionClientIdx = (int) udpTalk.getSessionClientIdx();
			if (!sessionClientIdx.equals(key)) {
				UdpTalk udpTalkTmp = next.getValue();
				udpTalkTmp.sendFrom(udpTalk, dataPacket);
			}
		}
	}

	public long getDiffTime() {
		long now = new Date().getTime();
		return now - timeStart;
	}

	public byte getNumberOfClients() {
		return numberOfClients;
	}

	public boolean isFull() {
		return udpTalkers.size() == numberOfClients;
	}

}
