package br.com.xht.udp.protocol.string;

import br.com.xht.udp.handler.IPacketProcessor;

public class StringPacketProcessor implements IPacketProcessor {

	@Override
	public byte[] getProcessed(byte[] data, byte sessionFromClientIdx) {
		return data;
	}

}
