package br.com.xht.udp.handler;

import java.util.HashMap;

public class UdpTalkers {

	private static HashMap<Integer, IUdpTalk> udpTalkers = new HashMap<Integer, IUdpTalk>();

	public static IUdpTalk put(Integer key, IUdpTalk value) {
		return udpTalkers.put(key, value);
	}

	public static IUdpTalk remove(Integer key) {
		return udpTalkers.remove(key);
	}

	public static IUdpTalk get(Integer key) {
		return udpTalkers.get(key);
	}

}
