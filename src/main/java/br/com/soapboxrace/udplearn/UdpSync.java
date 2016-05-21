package br.com.soapboxrace.udplearn;

import java.nio.ByteBuffer;

public class UdpSync {

	public static void completeSync(UdpTalk udpTalk, DataPacket dataPacket) {
		byte[] dataBytes = dataPacket.getDataBytes();
		ByteBuffer dbuf = ByteBuffer.allocate(4);
		dbuf.putInt(udpTalk.getSessionId());
		byte[] sessionId = dbuf.array();
		if (dataBytes[0] == 0x00 && //
				dataBytes[4] == 0x02 && //
				dataBytes[13] == 0x00 && //
				dataBytes[14] == 0x06 && //
				dataBytes[15] == 0x00 && //
				dataBytes[16] == sessionId[0] && //
				dataBytes[17] == sessionId[1] && //
				dataBytes[18] == sessionId[2] && //
				dataBytes[19] == sessionId[3]) {
			udpTalk.setPingTime(dataBytes);
		} else {
			String dataString = dataPacket.getDataString();
			if (dataString.contains("gogogo!")) {
				udpTalk.setPingTime(dataPacket.getDataBytes());
			}
		}
	}

}
