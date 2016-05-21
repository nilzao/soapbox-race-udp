package br.com.soapboxrace.udplearn;

public class UdpSync {

	public static void startSession(UdpTalk udpTalk, DataPacket dataPacket) {
		String dataString = dataPacket.getDataString();
		if (dataString.contains("gogogo!")) {
			udpTalk.setPingTime(dataPacket.getDataBytes());
		}
	}

}
