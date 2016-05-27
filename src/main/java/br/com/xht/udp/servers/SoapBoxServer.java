package br.com.xht.udp.servers;

import br.com.xht.udp.handler.UdpDebug;
import br.com.xht.udp.handler.UdpServer;
import br.com.xht.udp.protocol.soapbox.SoapBoxProtocol;

public class SoapBoxServer {

	public static void main(String[] args) {
		System.out.println("SoapBox Udp server started");
		UdpDebug.startDebug();
		SoapBoxProtocol soapBoxProtocol = new SoapBoxProtocol();
		UdpServer udpServer = new UdpServer(9998, soapBoxProtocol);
		udpServer.start();
	}
}
