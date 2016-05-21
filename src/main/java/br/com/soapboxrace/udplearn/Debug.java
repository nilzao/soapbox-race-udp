package br.com.soapboxrace.udplearn;

public class Debug {

	private static boolean isDebugging = true;

	private static boolean readablePackets = true;

	public static void debugReceivePacket(DataPacket dataPacket) {
		if (isDebugging) {
			byte[] dataBytes = dataPacket.getDataBytes();
			if (readablePackets) {
				String string = new String(dataBytes);
				System.out.println("Receiving: [" + string.trim() + "]");
			}
			if (!readablePackets) {
				System.out.println("Receiving: [" + byteArrayToHexString(dataBytes) + "]");
			}
		}
	}

	private static String byteArrayToHexString(byte[] b) {
		int len = b.length;
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < len; i++) {
			stringBuffer.append(Integer.toHexString((b[i] >> 4) & 0xf));
			stringBuffer.append(Integer.toHexString(b[i] & 0xf));
			stringBuffer.append(':');
		}
		return stringBuffer.toString();
	}

}
