package br.com.xht.udp.protocol.soapbox;

import java.nio.ByteBuffer;

import br.com.xht.udp.handler.IPacketProcessor;

public class SoapBoxPacketProcessor implements IPacketProcessor {

	private int countA = 1;
	private int countB = 0;
	private byte sessionFromClientIdx;
	private boolean syncStopped = false;

	@Override
	public byte[] getProcessed(byte[] data, byte sessionFromClientIdx) {
		this.sessionFromClientIdx = sessionFromClientIdx;
		if (isTypeB(data)) {
			return transformByteTypeB(data);
		}
		if (isTypeA(data)) {
			return transformByteTypeA(data);
		}
		if (isTypeASync(data)) {
			return transformByteTypeASync(data);
		}
		return data;
	}

	private boolean isTypeA(byte[] data) {
		if (data[0] == 0x00 && data[3] == 0x07 && data.length != 26) {
			return true;
		}
		return false;
	}

	private boolean isTypeASync(byte[] data) {
		if (data[0] == 0x00 && data[3] == 0x07 && data.length == 26) {
			return true;
		}
		return false;
	}

	private boolean isTypeB(byte[] data) {
		if (data[0] == 0x01) {
			return true;
		}
		return false;
	}

	private byte[] transformByteTypeA(byte[] data) {
		if (data.length == 22) {
			syncStopped = true;
			// return null;
		}
		byte[] seqArray = ByteBuffer.allocate(2).putShort((short) countA).array();
		int size = data.length - 1;
		byte[] dataTmp = new byte[size];
		dataTmp[1] = seqArray[0];
		dataTmp[2] = seqArray[1];
		int iDataTmp = 3;
		for (int i = 4; i < data.length; i++) {
			dataTmp[iDataTmp++] = data[i];
		}
		countA++;
		return dataTmp;
	}

	private byte[] transformByteTypeASync(byte[] data) {
		data = transformByteTypeA(data);
		data[(data.length - 11)] = sessionFromClientIdx;
		countA++;
		return data;
	}

	private byte[] transformByteTypeB(byte[] data) {
		if (data.length < 4) {
			return null;
		}
		byte[] seqArray = ByteBuffer.allocate(2).putShort((short) countB).array();
		int size = data.length - 3;
		byte[] dataTmp = new byte[size];
		dataTmp[0] = 1;
		dataTmp[1] = sessionFromClientIdx;
		dataTmp[2] = seqArray[0];
		dataTmp[3] = seqArray[1];
		int iDataTmp = 4;
		for (int i = 6; i < (data.length - 1); i++) {
			dataTmp[iDataTmp++] = data[i];
		}
		if (!syncStopped) {
			dataTmp[4] = (byte) 0xff;
			dataTmp[5] = (byte) 0xff;
		}
		countB++;
		return dataTmp;
	}

	@Override
	public void syncStopped() {
		syncStopped = true;
	}

	@Override
	public boolean isSyncStopped() {
		return syncStopped;
	}

}
