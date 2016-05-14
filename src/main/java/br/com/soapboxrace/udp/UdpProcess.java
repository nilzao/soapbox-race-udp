package br.com.soapboxrace.udp;

public class UdpProcess {

	public static byte[] prepare(byte[] data) {
		if (data[0] == 0 && data[3] == 7) {
			return transformTypeA(data);
		}
		if (data[0] == 1 && data[1] == 0 && data[2] == 0) {
			return transformTypeB(data);
		}
		return null;
	}

	private static byte[] transformTypeA(byte[] data) {
		int size = data.length - 1;
		byte[] dataTmp = new byte[size];
		dataTmp[1] = 10;
		dataTmp[2] = 10;
		int iDataTmp = 3;
		for (int i = 4; i < data.length; i++) {
			dataTmp[iDataTmp++] = data[i];
		}
		dataTmp[(dataTmp.length - 6)] = 3;
		return dataTmp;
	}

	private static byte[] transformTypeB(byte[] data) {
		int size = data.length - 3;
		byte[] dataTmp = new byte[size];
		dataTmp[0] = 1;
		dataTmp[2] = 11;
		dataTmp[3] = 11;
		int iDataTmp = 4;
		for (int i = 6; i < (data.length - 1); i++) {
			dataTmp[iDataTmp++] = data[i];
		}
		return dataTmp;
	}

}
