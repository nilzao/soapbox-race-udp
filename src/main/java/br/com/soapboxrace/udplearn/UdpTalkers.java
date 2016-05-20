package br.com.soapboxrace.udplearn;

import java.net.DatagramPacket;
import java.util.HashMap;

public class UdpTalkers {

	private static HashMap<Integer, UdpTalk> udpTalkers = new HashMap<Integer, UdpTalk>();

	public static void handlePacket(DatagramPacket receivePacket) {
		int port = receivePacket.getPort();
		UdpTalk udpTalk = udpTalkers.get(port);
		if (udpTalk == null) {
			udpTalk = UdpTalk.startTalk(receivePacket);
			if (udpTalk != null) {
				udpTalkers.put(port, udpTalk);
			}
		} else {
			udpTalk.handlePacket(receivePacket);
		}
	}
}
