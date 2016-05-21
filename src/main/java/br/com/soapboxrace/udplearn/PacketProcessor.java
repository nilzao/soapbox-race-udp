package br.com.soapboxrace.udplearn;

public class PacketProcessor {

	private int countA = 0;
	private int countB = 0;

	public byte[] getProcessed(byte[] data, int sessionClientIdx) {
		if (isTypeA(data)) {
			return processTypeA(data);
		}
		if (istTypeB(data)) {
			return processTypeB(data);
		}
		return data;
	}

	private boolean isTypeA(byte[] data) {
		String string = new String(data);
		if (string.contains("msg:")) {
			return true;
		}
		return false;
	}

	private boolean istTypeB(byte[] data) {
		String string = new String(data);
		if (string.contains("ping:")) {
			return true;
		}
		return false;
	}

	private byte[] processTypeA(byte[] data) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("pkg typeA num: [");
		stringBuilder.append(countA);
		stringBuilder.append("] ");
		stringBuilder.append(new String(data));
		countA++;
		return stringBuilder.toString().getBytes();
	}

	private byte[] processTypeB(byte[] data) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("pkg typeB num: [");
		stringBuilder.append(countB);
		stringBuilder.append("] ");
		stringBuilder.append(new String(data));
		countB++;
		return stringBuilder.toString().getBytes();
	}
}
