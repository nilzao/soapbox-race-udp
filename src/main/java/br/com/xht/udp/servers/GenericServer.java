package br.com.xht.udp.servers;

import br.com.xht.udp.handler.IUdpProtocol;
import br.com.xht.udp.handler.UdpServer;

public class GenericServer {
	public static void main(String[] args) {
		int port = 9999;
		String protocol = "br.com.xht.udp.protocol.soapbox.freeroam.SoapBoxProtocol";
		if (args.length > 0 && args.length != 2) {
			return;
		} else if (args.length == 2) {
			port = new Integer(args[0]);
			protocol = args[1];
		}
		// UdpDebug.startDebug();
		Class<?> dynamicObj;
		try {
			dynamicObj = Class.forName(protocol);
			IUdpProtocol udpProtocol = (IUdpProtocol) dynamicObj.newInstance();
			UdpServer udpServer = new UdpServer(port, udpProtocol);
			udpServer.start();
		} catch (ClassNotFoundException e) {
			System.err.println("Protocol class [" + protocol + "] not found");
			System.err.println(e.getMessage());
			System.exit(1);
		} catch (InstantiationException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		} catch (IllegalAccessException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		System.out.println("");
		System.out.println("Generic udp server\n");
		System.out.println("to change port and protocol run:");
		System.out.println("java -jar soapbox-udp.jar PORT FULL_PROTOCOL_CLASS_NAME\n");
		System.out.println("Example:");
		System.out.println("  java -jar soapbox-udp.jar 1234 br.com.xht.udp.protocol.string.StringProtocol");
		System.out.println("");
		System.out.println("listening on port: [" + port + "]");
		System.out.println("with protocol: [" + protocol + "]");
		System.out.println("");
	}
}
