package br.com.xht.udp.protocol.soapbox.freeroam;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import br.com.xht.udp.handler.IUdpTalk;
import br.com.xht.udp.handler.UdpDataPacket;
import br.com.xht.udp.handler.UdpHello;
import br.com.xht.udp.handler.UdpSessions;
import br.com.xht.udp.handler.UdpWriter;

public class SoapBoxHello extends UdpHello {

	private byte[] helloPacketTmp;

	@Override
	protected void parseHelloPacket() throws Exception {
		if ((helloPacket[2] != 0x06)) {
			throw new Exception("invalid hello packet!");
		}
		byte[] helloPacketParse = { //
				helloPacket[3], //
				helloPacket[4], //
				helloPacket[5], //
				helloPacket[6]//
		};
		helloPacketTmp = helloPacketParse;
	}

	@Override
	protected void setPersonaId() throws Exception {
		byte[] personaIdTmp = { helloPacket[7], helloPacket[8], helloPacket[9], helloPacket[10] };
		personaId = new BigInteger(personaIdTmp).intValue();
	}

	@Override
	public int getPersonaId() {
		return personaId;
	}

	@Override
	protected void setSessionId() throws Exception {
		sessionId = 666;
	}

	@Override
	protected IUdpTalk getUdpTalkInstance(UdpDataPacket dataPacket) {
		SoapBoxPacketProcessor soapBoxPacketProcessor = new SoapBoxPacketProcessor();
		UdpWriter udpWriter = new UdpWriter(dataPacket);
		SoapBoxTalk soapBoxTalk = new SoapBoxTalk(this, soapBoxPacketProcessor, udpWriter);
		UdpSessions.addUdpTalk(soapBoxTalk);
		return soapBoxTalk;
	}

	@Override
	protected byte[] getServerHelloMessage() {
		ByteBuffer byteBuffer = ByteBuffer.allocate(11);
		byteBuffer.put((byte) 0x00);
		byteBuffer.put((byte) 0x00);
		byteBuffer.put((byte) 0x01);
		byteBuffer.put(helloPacketTmp[0]);
		byteBuffer.put(helloPacketTmp[1]);
		byteBuffer.put(helloPacketTmp[2]);
		byteBuffer.put(helloPacketTmp[3]);
		byteBuffer.put((byte) 0x01);
		byteBuffer.put((byte) 0x01);
		byteBuffer.put((byte) 0x01);
		byteBuffer.put((byte) 0x01);
		return byteBuffer.array();
	}

	@Override
	public byte[] getHelloPacket() {
		return helloPacketTmp;
	}

}
