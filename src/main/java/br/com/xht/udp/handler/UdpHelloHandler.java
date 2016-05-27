package br.com.xht.udp.handler;

public class UdpHelloHandler {

	public static IUdpTalk startTalk(IUdpProtocol udpProtocol, UdpDataPacket dataPacket) {
		return udpProtocol.getNewHelloInstance().startTalk(dataPacket);
	}

}
