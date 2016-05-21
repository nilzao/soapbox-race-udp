package br.com.soapboxrace.udplearn;

public class Debug {

	private static boolean isDebugging = true;

	public static void debugReceivePacket(DataPacket dataPacket) {
		if (isDebugging) {
			if (isReadablePackets(dataPacket)) {
				String string = dataPacket.getDataString();
				System.out.println("Receiving: [" + string.trim() + "]");
				if (string.contains("01:01:01:01:")) {
					byte[] hexStringToByteArray = hexStringToByteArray(string.trim());
					dataPacket.replaceDataBytes(hexStringToByteArray);
				}
			} else {
				System.out.println("Receiving: [" + byteArrayToHexString(dataPacket.getDataBytes()) + "]");
			}
		}
	}

	private static boolean isReadablePackets(DataPacket dataPacket) {
		byte[] dataBytes = dataPacket.getDataBytes();
		if (dataBytes[0] < 0x20) {
			return false;
		}
		return true;
	}

	public static void debugSendPacket(DataPacket dataPacket) {
		if (isDebugging) {
			if (isReadablePackets(dataPacket)) {
				String string = dataPacket.getDataString();
				System.out.println("Sending: [" + string.trim() + "]");
			} else {
				String byteArrayToHexString = byteArrayToHexString(dataPacket.getDataBytes());
				dataPacket.replaceDataBytes(byteArrayToHexString.getBytes());
				System.out.println("Sending: [" + byteArrayToHexString + "]");
			}
		}
	}

	private static byte[] hexStringToByteArray(String s) {
		s = s.replace(":", "");
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
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
