package br.com.xht.udp.protocol.soapbox.freeroam;

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
	}

	@Override
	public byte[] getSyncPacket() {
		byte[] pingSyncPacket = { 0x00 };
		return pingSyncPacket;
	}

}
