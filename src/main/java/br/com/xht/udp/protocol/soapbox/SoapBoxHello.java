package br.com.xht.udp.protocol.soapbox;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import br.com.xht.udp.handler.IUdpTalk;
import br.com.xht.udp.handler.UdpDataPacket;
import br.com.xht.udp.handler.UdpHello;
import br.com.xht.udp.handler.UdpWriter;

public class SoapBoxHello extends UdpHello {

	private byte[] helloPacketTmp;

	@Override
	protected void parseHelloPacket() throws Exception {
		if ((helloPacket[3] != 0x06)) {
			throw new Exception("invalid hello packet!");
		}
		byte[] helloPacketTmp = { helloPacket[5], helloPacket[6], helloPacket[7], helloPacket[8] };
		this.helloPacketTmp = helloPacketTmp;
	}

	@Override
	protected void setSessionClientIdx() throws Exception {
		sessionClientIdx = helloPacket[4];
	}

	@Override
	protected void setNumberOfClients() throws Exception {
		numberOfClients = helloPacket[13];
	}

	@Override
	protected void setSessionId() throws Exception {
		byte[] sessionIdTmp = { helloPacket[9], helloPacket[10], helloPacket[11], helloPacket[12] };
		sessionId = new BigInteger(sessionIdTmp).intValue();
	}

	@Override
	protected IUdpTalk getUdpTalkInstance(UdpDataPacket dataPacket) {
		SoapBoxPacketProcessor soapBoxPacketProcessor = new SoapBoxPacketProcessor();
		UdpWriter udpWriter = new UdpWriter(dataPacket);
		SoapBoxTalk soapBoxTalk = new SoapBoxTalk(this, soapBoxPacketProcessor, udpWriter);
		return soapBoxTalk;
	}

	@Override
	protected byte[] getServerHelloMessage() {
		ByteBuffer byteBuffer = ByteBuffer.allocate(12);
		byteBuffer.put((byte) 0x00);
		byteBuffer.put((byte) 0x00);
		byteBuffer.put((byte) 0x00);
		byteBuffer.put((byte) 0x01);
		byteBuffer.put(helloPacketTmp);
		byteBuffer.put((byte) 0x01);
		byteBuffer.put((byte) 0x01);
		byteBuffer.put((byte) 0x01);
		byteBuffer.put((byte) 0x01);
		return byteBuffer.array();
	}

}
