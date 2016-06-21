package br.com.xht.udp.handler;

public interface IUdpHello {

	public IUdpTalk startTalk(UdpDataPacket dataPacket);

	public int getPersonaId();

	public byte getNumberOfClients();

	public int getSessionId();

	public byte[] getHelloPacket();

}
