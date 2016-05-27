package br.com.xht.udp.servers;

import br.com.xht.udp.handler.UdpServer;
import br.com.xht.udp.protocol.soapbox.SoapBoxProtocol;

public class SoapBoxServer {

	public static void main(String[] args) {
		int port = 9998;
		System.out.println("");
		System.out.println("SoapBox race udp server\n");
		System.out.println("to change port run:");
		System.out.println("java -jar soapbox-udp.jar PORT\n");
		System.out.println("Example:");
		System.out.println("  java -jar soapbox-udp.jar 1234");
		if (args.length > 0 && args.length > 1) {
			return;
		} else if (args.length == 1) {
			port = new Integer(args[0]);
		}
		System.out.println("");
		System.out.println("listening on port: [" + port + "]");
		System.out.println("");
		// UdpDebug.startDebug();
		SoapBoxProtocol soapBoxProtocol = new SoapBoxProtocol();
		UdpServer udpServer = new UdpServer(port, soapBoxProtocol);
		udpServer.start();
	}
}
