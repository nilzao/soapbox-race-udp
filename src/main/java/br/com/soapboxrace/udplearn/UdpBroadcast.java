package br.com.soapboxrace.udplearn;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import br.com.soapboxrace.udp.UdpWriter;

public class UdpBroadcast {

	private HashMap<Integer, UdpWriter> udpWriters = new HashMap<Integer, UdpWriter>();

	public void broadcast(UdpWriter udpWriterFrom, byte[] data) {
		int port = udpWriterFrom.getPort();
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

	public UdpWriter put(UdpWriter udpWriter) {
		return udpWriters.put(udpWriter.getPort(), udpWriter);
	}

	public UdpWriter remove(UdpWriter udpWriter) {
		return udpWriters.remove(udpWriter.getPort());
	}

}
