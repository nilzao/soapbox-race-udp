package br.com.xht.udp.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class UdpSession {

	private HashMap<Integer, IUdpTalk> udpTalkers = new HashMap<Integer, IUdpTalk>();
	private int sessionId;
	private byte numberOfClients;
	private long timeStart;
	private boolean isSyncDone = false;

	public UdpSession(int sessionId, byte numberOfClients, long timeStart) {
		this.sessionId = sessionId;
		this.timeStart = timeStart;
		this.numberOfClients = numberOfClients;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void put(IUdpTalk udpTalk) {
		int clientSessionIdx = (int) udpTalk.getSessionClientIdx();
		udpTalkers.put(clientSessionIdx, udpTalk);
	}

	public void broadcast(IUdpTalk udpTalk, byte[] dataPacket) {
		if (isSyncDone) {
			Iterator<Entry<Integer, IUdpTalk>> iterator = udpTalkers.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, IUdpTalk> next = iterator.next();
				Integer key = next.getKey();
				Integer sessionClientIdx = (int) udpTalk.getSessionClientIdx();
				if (!sessionClientIdx.equals(key)) {
					IUdpTalk udpTalkTmp = next.getValue();
					udpTalkTmp.sendFrom(udpTalk, dataPacket);
				}
			}
		}
	}

	public void broadcastSyncPackets() {
		if (isFull()) {
			Iterator<Entry<Integer, IUdpTalk>> iterator = udpTalkers.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, IUdpTalk> next = iterator.next();
				IUdpTalk udpTalkTmp = next.getValue();
				broadcastSyncPackets(udpTalkTmp, udpTalkTmp.getSyncPacket());
			}
			isSyncDone = true;
		}
	}

	private void broadcastSyncPackets(IUdpTalk udpTalk, byte[] dataPacket) {
		Iterator<Entry<Integer, IUdpTalk>> iterator = udpTalkers.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, IUdpTalk> next = iterator.next();
			Integer key = next.getKey();
			Integer sessionClientIdx = (int) udpTalk.getSessionClientIdx();
			if (!sessionClientIdx.equals(key)) {
				IUdpTalk udpTalkTmp = next.getValue();
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

	private boolean isFull() {
		return udpTalkers.size() == numberOfClients;
	}

}
