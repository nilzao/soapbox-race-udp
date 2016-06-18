package br.com.xht.udp.protocol.soapbox;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

import br.com.xht.udp.handler.IPacketProcessor;
import br.com.xht.udp.handler.IUdpHello;
import br.com.xht.udp.handler.UdpTalk;
import br.com.xht.udp.handler.UdpWriter;

public class SoapBoxTalk extends UdpTalk {

	public SoapBoxTalk(IUdpHello udpHello, IPacketProcessor packetProcessor, UdpWriter udpWriter) {
		super(udpHello, packetProcessor, udpWriter);
	}

	@Override
	protected void parseSyncPacket() throws Exception {
		ByteBuffer dbuf = ByteBuffer.allocate(4);
		dbuf.putInt(getSessionId());
		byte[] sessionId = dbuf.array();
		if (!(syncPacket[0] == 0x00 && //
				syncPacket[4] == 0x02 && //
				syncPacket[13] == 0x00 && //
				syncPacket[14] == 0x06 && //
				syncPacket[15] == 0x00 && //
				syncPacket[16] == sessionId[0] && //
				syncPacket[17] == sessionId[1] && //
				syncPacket[18] == sessionId[2] && //
				syncPacket[19] == sessionId[3])) {
			throw new Exception("invalid sync packet");
		}
	}

	private byte pingCalc() {
		long diffTime = getDiffTime();
		if (diffTime > 1000) {
			return (byte) 0xff;
		}
		float pingCalcFloat = 0.254F * diffTime;
		byte pingCalc = new BigDecimal(pingCalcFloat).byteValue();
		return pingCalc;
	}

	@Override
	public byte[] getSyncPacket() {
		byte[] helloPacket = getHelloPacket();
		byte[] pingSyncPacket = syncPacket;
		// pingSyncPacket[5] = seqSync; // seq from hello part?
		pingSyncPacket[6] = pingCalc(); // ping calc?
		pingSyncPacket[7] = helloPacket[2]; // hello part, fixed byte?
		pingSyncPacket[8] = helloPacket[3]; // hello part, packet loss?
		pingSyncPacket[(pingSyncPacket.length - 6)] = (byte) 0x03;
		return pingSyncPacket;
	}

}
