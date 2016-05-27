package br.com.xht.udp.servers;

import br.com.xht.udp.handler.UdpDebug;
import br.com.xht.udp.handler.UdpServer;
import br.com.xht.udp.protocol.string.StringProtocol;

public class StringServer {

	public static void main(String[] args) {
		UdpDebug.startDebug();
		StringProtocol stringProtocol = new StringProtocol();
		UdpServer udpServer = new UdpServer(9998, stringProtocol);
		udpServer.start();
	}
}
