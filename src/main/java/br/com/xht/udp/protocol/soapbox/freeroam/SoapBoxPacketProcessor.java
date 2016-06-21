package br.com.xht.udp.protocol.soapbox.freeroam;

import java.nio.ByteBuffer;

import br.com.xht.udp.handler.IPacketProcessor;

public class SoapBoxPacketProcessor implements IPacketProcessor {

	private int count = 1;

	private byte[] header;

	public SoapBoxPacketProcessor() {
		byte[] headerTmp = { //
				(byte) 0x00, (byte) 0x00, // seq
				(byte) 0x02, // fixo
				(byte) 0x6a, // sequencial, vem do hello-ok-from-srv
				(byte) 0x58, // ???
				(byte) 0x56, // fixo, vem do hello-ok-from-srv
				(byte) 0x82, // varia 0x7a a 0x84 ??? vem do hello-ok-from-srv
				(byte) 0x00, (byte) 0x00, // seq
				(byte) 0x7f, // fixo 0xff em quase todos pacotes
				(byte) 0xff, // fixo
				(byte) 0x00 // varia 0x00 a 0x0d ???
		};
		header = headerTmp;
	}

	@Override
	public byte[] getProcessed(byte[] data) {
		byte[] seqArray = ByteBuffer.allocate(2).putShort((short) count).array();
		header[0] = seqArray[0];
		header[1] = seqArray[1];
		header[7] = seqArray[0];
		header[8] = seqArray[1];
		data = convertPacket(data);
		int size = header.length + data.length;
		byte[] dataTmp = new byte[size];
		for (int i = 0; i < header.length; i++) {
			dataTmp[i] = header[i];
		}
		for (int i = 0; i < data.length; i++) {
			int pos = i + header.length;
			dataTmp[pos] = data[i];
		}
		count++;
		return dataTmp;
	}

	// rip this:
	// 00:01:07:02:06:d1:63:ba:ff:ff:ff:ff:59:ec:46:e1:
	private byte[] convertPacket(byte[] data) {
		if (data.length > 16) {
			int size = data.length - 15;
			byte[] dataTmp = new byte[size];
			dataTmp[0] = 0x00;
			for (int i = 1; i < dataTmp.length; i++) {
				int pos = i + 15;
				dataTmp[i] = data[pos];
			}
			return dataTmp;
		}
		return data;
	}

	@Override
	public void syncStopped() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isSyncStopped() {
		// TODO Auto-generated method stub
		return false;
	}

}
