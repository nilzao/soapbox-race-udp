package br.com.xht.udp.protocol.string;

import br.com.xht.udp.handler.IPacketProcessor;
import br.com.xht.udp.handler.IUdpHello;
import br.com.xht.udp.handler.UdpTalk;
import br.com.xht.udp.handler.UdpWriter;

public class StringTalk extends UdpTalk {

	private String syncPacketStr;

	public StringTalk(IUdpHello udpHello, IPacketProcessor packetProcessor, UdpWriter udpWriter) {
		super(udpHello, packetProcessor, udpWriter);
	}

	@Override
	protected void parseSyncPacket() throws Exception {
		syncPacketStr = new String(syncPacket).trim();
		if (!syncPacketStr.contains("gogogo!")) {
			throw new Exception("invalid sync packet");
		}
	}

	@Override
	public byte[] getSyncPacket() {
		String incomePacket = new String(syncPacketStr);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("sync packet: [");
		stringBuilder.append(incomePacket.trim());
		stringBuilder.append("]\n");
		stringBuilder.append("Session [");
		stringBuilder.append(getSessionId());
		stringBuilder.append("] talker started time: [");
		stringBuilder.append(getDiffTime());
		stringBuilder.append("]\n");
		return stringBuilder.toString().getBytes();
	}

}
