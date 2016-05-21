package br.com.soapboxrace.udplearn;

import java.util.HashMap;

public class UdpTalkers {

	private static HashMap<Integer, UdpHello> udpTalkers = new HashMap<Integer, UdpHello>();

	public static UdpHello put(Integer key, UdpHello value) {
		return udpTalkers.put(key, value);
	}

	public static UdpHello remove(Integer key) {
		return udpTalkers.remove(key);
	}

	public static UdpHello get(Integer key) {
		return udpTalkers.get(key);
	}

}
