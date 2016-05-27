package br.com.xht.udp.handler;

import java.util.Comparator;

public class UdpTalkPingComparator implements Comparator<UdpTalk> {

	@Override
	public int compare(UdpTalk udpTalk1, UdpTalk udpTalk2) {
		return udpTalk2.compareTo(udpTalk1);
	}

}
