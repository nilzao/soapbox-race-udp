package br.com.xht.udp.handler;

public interface IUdpHello {

	// public IUdpHello getNewInstance();

	public IUdpTalk startTalk(UdpDataPacket dataPacket);

	public byte getSessionClientIdx();

	public byte getNumberOfClients();

	public int getSessionId();

}
