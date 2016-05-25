package br.com.soapboxrace.udp;

import java.nio.ByteBuffer;

public class PacketProcessor {

	private int countA = 0;
	private int countB = 0;
	private byte sessionFromClientIdx;

	public byte[] getProcessed(byte[] data, byte sessionFromClientIdx) {
		this.sessionFromClientIdx = sessionFromClientIdx;
		byte[] dataTmp = isTypeB(data);
		if (dataTmp != null) {
			countB++;
			return dataTmp;
		}
		dataTmp = isTypeA(data);
		if (dataTmp != null) {
			countA++;
			return dataTmp;
		}
		dataTmp = isTypeAStart(data);
		if (dataTmp != null) {
			countA++;
			return dataTmp;
		}
		return data;
	}

	private byte[] isTypeAStart(byte[] data) {
		if (data.length == 26) {
			data = transformByteTypeA(data);
			data[(data.length - 11)] = sessionFromClientIdx;
			data[(data.length - 6)] = 0x03;
			return data;
		}
		return null;
	}

	private byte[] isTypeA(byte[] data) {
		if (data[0] == 0 && data[3] == 7 && data.length != 26) {
			return transformByteTypeA(data);
		}
		String string = new String(data);
		if (string.contains("msg:")) {
			return transformStrTypeA(data);
		}
		return null;
	}

	private byte[] isTypeB(byte[] data) {
		if (data[0] == 1 && data[1] == 0 && data[2] == 0) {
			return transformByteTypeB(data);
		}
		String string = new String(data);
		if (string.contains("ping:")) {
			return transformStrTypeB(data);
		}
		return null;
	}

	private byte[] transformStrTypeA(byte[] data) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("from [");
		stringBuffer.append(sessionFromClientIdx);
		stringBuffer.append("] ");
		stringBuffer.append("pkg typeA num: [");
		stringBuffer.append(countA);
		stringBuffer.append("] ");
		stringBuffer.append(new String(data));
		return stringBuffer.toString().getBytes();
	}

	private byte[] transformByteTypeA(byte[] data) {
		byte[] seqArray = ByteBuffer.allocate(2).putShort((short) countA).array();
		int size = data.length - 1;
		byte[] dataTmp = new byte[size];
		dataTmp[1] = seqArray[0];
		dataTmp[2] = seqArray[1];
		int iDataTmp = 3;
		for (int i = 4; i < data.length; i++) {
			dataTmp[iDataTmp++] = data[i];
		}
		return dataTmp;
	}

	private byte[] transformStrTypeB(byte[] data) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("from [");
		stringBuffer.append(sessionFromClientIdx);
		stringBuffer.append("] ");
		stringBuffer.append("pkg typeB num: [");
		stringBuffer.append(countB);
		stringBuffer.append("] ");
		stringBuffer.append(new String(data));
		return stringBuffer.toString().getBytes();
	}

	private byte[] transformByteTypeB(byte[] data) {
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
		return dataTmp;
	}

}
