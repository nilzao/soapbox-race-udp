package br.com.soapboxrace.udplearn;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class UdpHello {

	public static UdpTalk startTalk(DataPacket dataPacket) {
		UdpTalk udpTalk = null;
		try {
			byte[] helloPacket = parseHelloPacket(dataPacket);
			byte sessionClientIdx = parseSessionClientIdx(dataPacket);
			int sessionId = parseSessionId(dataPacket);
			UdpWriter udpWriter = new UdpWriter(dataPacket);
			udpTalk = new UdpTalk(sessionClientIdx, sessionId, udpWriter);
			UdpSyncThread udpSyncThread = new UdpSyncThread(udpTalk);
			udpSyncThread.start();
			if (isByteHello(dataPacket.getDataBytes())) {
				sendWelcomeByteMsg(udpTalk, helloPacket);
			} else {
				sendWelcomeStrMsg(udpTalk, helloPacket);
			}
		} catch (Exception e) {
			System.err.println("NOT A VALID HELLO PACKET!");
			System.err.println(e.getMessage());
			udpTalk = null;
		}
		return udpTalk;
	}

	private static void sendWelcomeByteMsg(UdpTalk udpTalk, byte[] helloPacket) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(12);
		byteBuffer.put((byte) 0x00);
		byteBuffer.put((byte) 0x00);
		byteBuffer.put((byte) 0x00);
		byteBuffer.put((byte) 0x01);
		byteBuffer.put(helloPacket);
		byteBuffer.put((byte) 0x01);
		byteBuffer.put((byte) 0x01);
		byteBuffer.put((byte) 0x01);
		byteBuffer.put((byte) 0x01);
		udpTalk.getUdpWriter().sendPacket(byteBuffer.array());
	}

	private static void sendWelcomeStrMsg(UdpTalk udpTalk, byte[] helloPacket) {
		System.out.println("hello msg: [" + new String(helloPacket) + "]");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\n");
		stringBuilder.append("Welcome to udp server learning");
		stringBuilder.append("\n");
		stringBuilder.append("SessionID: ");
		stringBuilder.append(udpTalk.getUdpSession().getSessionId());
		stringBuilder.append("\n");
		stringBuilder.append("SessionID started time: [");
		stringBuilder.append(udpTalk.getUdpSession().getDiffTime());
		stringBuilder.append("]\n");
		stringBuilder.append("Your Session Client Index: ");
		stringBuilder.append(udpTalk.getSessionClientIdx());
		stringBuilder.append("\n");
		stringBuilder.append("Your Client Port: ");
		stringBuilder.append(udpTalk.getUdpWriter().getPort());
		stringBuilder.append("\n");
		stringBuilder.append("Please send your sessionStart packet");
		stringBuilder.append("\n");
		stringBuilder.append("you have 10 seconds to send some packets asap to detect your package loss, have fun!");
		stringBuilder.append("\n");
		stringBuilder.append("\n");
		udpTalk.getUdpWriter().sendPacket(stringBuilder.toString());
	}

	private static boolean isByteHello(byte[] dataBytes) {
		if (dataBytes[3] == 0x06) {
			return true;
		}
		return false;
	}

	private static byte[] parseHelloPacket(DataPacket dataPacket) throws Exception {
		byte[] dataBytes = dataPacket.getDataBytes();
		if (isByteHello(dataBytes)) {
			byte[] helloPacket = { dataBytes[5], dataBytes[6], dataBytes[7], dataBytes[8] };
			return helloPacket;
		}
		String dataString = dataPacket.getDataString();
		if (dataString.contains("hello!")) {
			return dataString.trim().getBytes();
		}
		throw new Exception("invalid hello");
	}

	private static byte parseSessionClientIdx(DataPacket dataPacket) throws Exception {
		byte[] dataBytes = dataPacket.getDataBytes();
		if (isByteHello(dataBytes)) {
			return dataBytes[4];
		}

		String dataString = dataPacket.getDataString();
		if (dataString.contains("sidx:")) {
			int indexOf = dataString.indexOf("sidx:");
			String sidxStr = dataString.substring(indexOf + 5, indexOf + 6);
			return Byte.valueOf(sidxStr);
		}
		throw new Exception("invalid session client index");
	}

	private static int parseSessionId(DataPacket dataPacket) throws Exception {
		byte[] dataBytes = dataPacket.getDataBytes();
		if (isByteHello(dataBytes)) {
			byte[] sessionId = { dataBytes[9], dataBytes[10], dataBytes[11], dataBytes[12] };
			return new BigInteger(sessionId).intValue();
		}

		String dataString = dataPacket.getDataString();
		dataString = dataString.trim();
		if (dataString.contains("sid:")) {
			int indexOf = dataString.indexOf("sid:");
			String sessionIdStr = dataString.substring(indexOf + 4, dataString.length());
			return Integer.valueOf(sessionIdStr);
		}
		throw new Exception("invalid session id");
	}

	private static class UdpSyncThread extends Thread {

		private UdpTalk udpTalk;

		public UdpSyncThread(UdpTalk udpTalk) {
			this.udpTalk = udpTalk;
		}

		public void run() {
			try {
				Thread.sleep(500);
				udpTalk.getUdpSession().broadcastSyncPackets();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
