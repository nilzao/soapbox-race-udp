package br.com.soapboxrace.udplearn;

public class UdpHandler {

	public static void handlePacket(DataPacket dataPacket) {
		int port = dataPacket.getPort();
		UdpTalk udpTalk = UdpTalkers.get(port);
		if (udpTalk == null) {
			udpTalk = UdpHello.startTalk(dataPacket);
			if (udpTalk != null) {
				System.out.println("Client Accepted! [" + port + "]");
				UdpTalkers.put(port, udpTalk);
			}
		} else {
			UdpSession udpSession = udpTalk.getUdpSession();
			udpSession.broadcast(udpTalk, dataPacket);
		}
	}

}
