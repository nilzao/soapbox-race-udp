package br.com.soapboxrace.udplearn;

import java.nio.ByteBuffer;

public class PacketProcessor {

	private int countA = 0;
	private int countB = 0;

	public byte[] getProcessed(byte[] data, int sessionClientIdx) {
		byte[] dataTmp = isTypeA(data);
		if (dataTmp != null) {
			countA++;
			return dataTmp;
		}
		dataTmp = isTypeB(data);
		if (dataTmp != null) {
			countB++;
			return dataTmp;
		}
		return data;
	}

	private byte[] isTypeA(byte[] data) {
		if (data[0] == 0 && data[3] == 7) {
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
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("pkg typeA num: [");
		stringBuilder.append(countA);
		stringBuilder.append("] ");
		stringBuilder.append(new String(data));
		return stringBuilder.toString().getBytes();
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
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("pkg typeB num: [");
		stringBuilder.append(countB);
		stringBuilder.append("] ");
		stringBuilder.append(new String(data));
		return stringBuilder.toString().getBytes();
	}

	private byte[] transformByteTypeB(byte[] data) {
		byte[] seqArray = ByteBuffer.allocate(2).putShort((short) countB).array();
		int size = data.length - 3;
		byte[] dataTmp = new byte[size];
		dataTmp[0] = 1;
		dataTmp[2] = seqArray[0];
		dataTmp[3] = seqArray[1];
		int iDataTmp = 4;
		for (int i = 6; i < (data.length - 1); i++) {
			dataTmp[iDataTmp++] = data[i];
		}
		return dataTmp;
	}

}
