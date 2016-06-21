package br.com.xht.udp.handler;

public abstract class UdpHello implements IUdpHello {

	protected byte[] helloPacket;
	protected int personaId;
	protected byte numberOfClients;
	protected int sessionId;

	@Override
	public IUdpTalk startTalk(UdpDataPacket dataPacket) {
		IUdpTalk udpTalk = null;
		try {
			setHelloPacket(dataPacket.getDataBytes());
			udpTalk = getUdpTalkInstance(dataPacket);
			sendHelloPacket(udpTalk);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return udpTalk;
	}

	protected void setHelloPacket(byte[] helloPacket) throws Exception {
		this.helloPacket = helloPacket;
		parseHelloPacket();
		setPersonaId();
		setSessionId();
	}

	protected abstract void parseHelloPacket() throws Exception;

	protected abstract void setPersonaId() throws Exception;

	protected abstract void setSessionId() throws Exception;

	protected abstract byte[] getServerHelloMessage();

	protected abstract IUdpTalk getUdpTalkInstance(UdpDataPacket dataPacket);

	protected void sendHelloPacket(IUdpTalk udpTalk) {
		udpTalk.sendFrom(udpTalk, getServerHelloMessage());
	}

	public byte getNumberOfClients() {
		return numberOfClients;
	}

	public int getSessionId() {
		return sessionId;
	}

	public byte[] getHelloPacket() {
		return helloPacket;
	}

}
