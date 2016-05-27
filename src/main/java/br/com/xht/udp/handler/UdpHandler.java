package br.com.xht.udp.handler;

public class UdpHandler {

	private IUdpProtocol udpProtocol;

	public UdpHandler(IUdpProtocol udpProtocol) {
		this.udpProtocol = udpProtocol;
	}

	public void handlePacket(UdpDataPacket dataPacket) {
		IUdpTalk udpTalk = UdpTalkers.get(dataPacket.getPort());
		if (udpTalk == null) {
			udpTalk = this.startTalk(dataPacket);
		} else if (!udpTalk.isSyncStarted()) {
			UdpSync.startSync(udpTalk, dataPacket);
		} else {
			udpTalk.broadcast(dataPacket.getDataBytes());
		}
	}

	private IUdpTalk startTalk(UdpDataPacket dataPacket) {
		int port = dataPacket.getPort();
		IUdpTalk udpTalk = UdpTalkers.get(port);
		if (udpTalk == null) {
			udpTalk = UdpHelloHandler.startTalk(udpProtocol, dataPacket);
			if (udpTalk != null) {
				UdpTalkers.put(port, udpTalk);
			}
		}
		return udpTalk;
	}

}