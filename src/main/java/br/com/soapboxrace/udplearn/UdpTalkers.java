package br.com.soapboxrace.udplearn;

import java.util.HashMap;

public class UdpTalkers {

	private static HashMap<Integer, UdpTalk> udpTalkers = new HashMap<Integer, UdpTalk>();

	public static UdpTalk put(Integer key, UdpTalk value) {
		return udpTalkers.put(key, value);
	}

	public static UdpTalk remove(Integer key) {
		return udpTalkers.remove(key);
	}

	public static UdpTalk get(Integer key) {
		return udpTalkers.get(key);
	}

}
