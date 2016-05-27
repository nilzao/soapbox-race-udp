package br.com.xht.udp.protocol.string;

import br.com.xht.udp.handler.IUdpTalk;
import br.com.xht.udp.handler.UdpDataPacket;
import br.com.xht.udp.handler.UdpHello;
import br.com.xht.udp.handler.UdpWriter;

public class StringHello extends UdpHello {

	private String helloPacketStr = null;

	@Override
	protected void parseHelloPacket() throws Exception {
		if (helloPacketStr == null && helloPacket != null) {
			helloPacketStr = new String(helloPacket).trim();
			if (!helloPacketStr.contains("hello!")) {
				throw new Exception("invalid hello packet!");
			}
		}
	}

	@Override
	protected void setSessionClientIdx() throws Exception {
		if (!helloPacketStr.contains("sidx:")) {
			throw new Exception("invalid session client index");
		}
		int indexOf = helloPacketStr.indexOf("sidx:");
		String sidxStr = helloPacketStr.substring(indexOf + 5, indexOf + 6);
		sessionClientIdx = Byte.valueOf(sidxStr);
	}

	@Override
	protected void setNumberOfClients() throws Exception {
		if (!helloPacketStr.contains("ncli:")) {
			throw new Exception("invalid number of clients");
		}
		int indexOf = helloPacketStr.indexOf("ncli:");
		String ncliStr = helloPacketStr.substring(indexOf + 5, indexOf + 6);
		numberOfClients = Byte.valueOf(ncliStr);
	}

	@Override
	protected void setSessionId() throws Exception {
		if (!helloPacketStr.contains("sid:")) {
			throw new Exception("invalid session id");
		}
		int indexOf = helloPacketStr.indexOf("sid:");
		String sessionIdStr = helloPacketStr.substring(indexOf + 4, helloPacketStr.length());
		sessionId = Integer.valueOf(sessionIdStr);
	}

	@Override
	protected IUdpTalk getUdpTalkInstance(UdpDataPacket dataPacket) {
		StringPacketProcessor stringPacketProcessor = new StringPacketProcessor();
		UdpWriter udpWriter = new UdpWriter(dataPacket);
		StringTalk stringTalk = new StringTalk(this, stringPacketProcessor, udpWriter);
		return stringTalk;
	}

	@Override
	protected byte[] getServerHelloMessage() {
		System.out.println("hello msg: [" + helloPacketStr + "]");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\n");
		stringBuilder.append("Welcome to udp server learning");
		stringBuilder.append("\n");
		stringBuilder.append("SessionID: [");
		stringBuilder.append(getSessionId());
		stringBuilder.append("]\n");
		stringBuilder.append("Your Session Client Index: ");
		stringBuilder.append(getSessionClientIdx());
		stringBuilder.append("\n");
		stringBuilder.append("Please send your sessionStart packet");
		stringBuilder.append("\n");
		stringBuilder.append("Waiting all the [");
		stringBuilder.append(getNumberOfClients());
		stringBuilder.append("] clients join the session to start talking.");
		stringBuilder.append("\n");
		stringBuilder.append("\n");
		return stringBuilder.toString().getBytes();
	}

}
