package br.com.xht.udp.handler;

public class UdpDebug {

	private static boolean isDebugging = false;

	public static void startDebug() {
		isDebugging = true;
	}

	public static void stopDebug() {
		isDebugging = false;
	}

	public static void debugReceivePacket(UdpDataPacket dataPacket) {
		if (isDebugging) {
			if (isReadablePackets(dataPacket)) {
				String string = dataPacket.getDataString();
				System.out.println("from: [" + dataPacket.getPort() + "] Receiving: [" + string.trim() + "]");
				if (string.contains("01:01:01:01:")) {
					byte[] hexStringToByteArray = hexStringToByteArray(string.trim());
					dataPacket.replaceDataBytes(hexStringToByteArray);
				}
			} else {
				System.out.println("from: [" + dataPacket.getPort() + "] Receiving: ["
						+ byteArrayToHexString(dataPacket.getDataBytes()) + "]");
			}
		}
	}

	public static void debugSendPacket(UdpDataPacket dataPacket) {
		if (isDebugging) {
			byte[] dataBytes = dataPacket.getDataBytes();
			if (isReadablePackets(dataBytes)) {
				String string = new String(dataBytes);
				System.out.println("Sending: [" + string.trim() + "]");
			} else {
				String byteArrayToHexString = "from-srv: {" + byteArrayToHexString(dataBytes) + "}\n";
				System.out.println("Sending: [" + byteArrayToHexString.trim() + "]");
				// dataPacket.replaceDataBytes(byteArrayToHexString.getBytes());
			}
		}
	}

	private static boolean isReadablePackets(UdpDataPacket dataPacket) {
		byte[] dataBytes = dataPacket.getDataBytes();
		return isReadablePackets(dataBytes);
	}

	private static boolean isReadablePackets(byte[] dataBytes) {
		if (dataBytes.length > 1) {
			if (dataBytes[1] < 0x20) {
				return false;
			}
		}
		return true;
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
