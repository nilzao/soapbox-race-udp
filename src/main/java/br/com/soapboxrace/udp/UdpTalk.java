package br.com.soapboxrace.udp;

import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class UdpTalk {

	private UdpWriter udpWriter;

	public UdpTalk(UdpWriter udpWriter) {
		this.udpWriter = udpWriter;
	}

	public static String byteArrayToHexString(byte[] b) {
		int len = b.length;
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < len; i++) {
			stringBuffer.append(Integer.toHexString((b[i] >> 4) & 0xf));
			stringBuffer.append(Integer.toHexString(b[i] & 0xf));
			stringBuffer.append(':');
		}
		return stringBuffer.toString();
	}

	public static UdpTalk getUdpTalk(DatagramPacket receivePacket) {
		byte[] receivedData = getBytes(receivePacket);
		String byteArrayToHexString = byteArrayToHexString(receivedData);
		System.out.println("receivn: " + byteArrayToHexString);
		UdpTalk udpTalk = null;
		int port = receivePacket.getPort();
		HashMap<Integer, UdpWriter> udpWriters = UdpServer.getUdpWriters();
		UdpWriter udpWriter = udpWriters.get(port);
		if (udpWriter == null) {
			udpWriter = startTalk(receivePacket);
		}
		if (udpWriter != null) {
			udpTalk = new UdpTalk(udpWriter);
		}
		return udpTalk;
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

	private static byte[] getBytes(DatagramPacket receivePacket) {
		int length = receivePacket.getLength();
		byte[] data = new byte[length];
		System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), data, 0, receivePacket.getLength());
		return data;
	}

	private static UdpWriter startTalk(DatagramPacket receivePacket) {
		HashMap<Integer, UdpWriter> udpWriters = UdpServer.getUdpWriters();
		int port = receivePacket.getPort();
		UdpWriter udpWriter = null;
		byte[] receivedData = getBytes(receivePacket);
		if (receivedData[0] == 0 && receivedData[3] == 6) {
			udpWriter = new UdpWriter(UdpServer.getServerSocket(), receivePacket, receivedData);
			udpWriters.put(port, udpWriter);
			System.out.println("client added: " + port);
			byte[] helloBytes = hexStringToByteArray("00:00:00:01:aa:bb:cc:dd:01:01:01:01");
			udpWriter.send(helloBytes);
		}
		return udpWriter;
	}

	public void processReceived(DatagramPacket receivePacket) {
		byte[] data = UdpProcess.prepare(getBytes(receivePacket));
		if (data != null) {
			this.broadcast(data);
		}
	}

	private void broadcast(byte[] data) {
		int port = udpWriter.getPort();
		HashMap<Integer, UdpWriter> udpWriters = UdpServer.getUdpWriters();
		Iterator<Entry<Integer, UdpWriter>> iterator = udpWriters.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, UdpWriter> next = iterator.next();
			Integer key = next.getKey();
			if (port != key) {
				UdpWriter udpWriterTmp = next.getValue();
				udpWriterTmp.send(data);
				System.out.println("msg sent to: " + key);
			}
		}
	}
}
