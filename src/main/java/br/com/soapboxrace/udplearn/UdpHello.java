package br.com.soapboxrace.udplearn;

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
			System.out.println("hello msg: [" + new String(helloPacket) + "]");
			sendWelcomeMsg(udpTalk);
		} catch (Exception e) {
			System.err.println("NOT A VALID HELLO PACKET!");
			System.err.println(e.getMessage());
			udpTalk = null;
		}
		return udpTalk;
	}

	private static void sendWelcomeMsg(UdpTalk udpTalk) {
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
		stringBuilder.append("you have 10 seconds to send some packets asap to detect your ping, have fun!");
		stringBuilder.append("\n");
		stringBuilder.append("\n");
		udpTalk.getUdpWriter().sendPacket(stringBuilder.toString());
	}

	private static byte[] parseHelloPacket(DataPacket dataPacket) throws Exception {
		String dataString = dataPacket.getDataString();
		if (!dataString.contains("hello!")) {
			throw new Exception("invalid hello");
		}
		return dataString.trim().getBytes();
	}

	private static byte parseSessionClientIdx(DataPacket dataPacket) throws Exception {
		String dataString = dataPacket.getDataString();
		if (!dataString.contains("sidx:")) {
			throw new Exception("invalid session client index");
		}
		int indexOf = dataString.indexOf("sidx:");
		String sidxStr = dataString.substring(indexOf + 5, indexOf + 6);
		return Byte.valueOf(sidxStr);
	}

	private static int parseSessionId(DataPacket dataPacket) throws Exception {
		String dataString = dataPacket.getDataString();
		dataString = dataString.trim();
		if (!dataString.contains("sid:")) {
			throw new Exception("invalid session id");
		}
		int indexOf = dataString.indexOf("sid:");
		String sessionIdStr = dataString.substring(indexOf + 4, dataString.length());
		return Integer.valueOf(sessionIdStr);
	}

	private static class UdpSyncThread extends Thread {

		private UdpTalk udpTalk;

		public UdpSyncThread(UdpTalk udpTalk) {
			this.udpTalk = udpTalk;
		}

		public void run() {
			try {
				Thread.sleep(10000);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			udpTalk.syncCompleted();
		}
	}
}
