package br.com.xht.udp.handler;

public class UdpSync {

	public static void startSync(IUdpTalk udpTalk, UdpDataPacket dataPacket) {
		udpTalk.setIncomeSyncPacket(dataPacket.getDataBytes());
	}
}
