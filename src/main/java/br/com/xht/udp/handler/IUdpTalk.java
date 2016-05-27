package br.com.xht.udp.handler;

public interface IUdpTalk {

	public boolean isSyncStarted();

	public byte getSessionClientIdx();

	public void sendFrom(IUdpTalk udpTalk, byte[] dataPacket);

	public void setIncomeSyncPacket(byte[] dataPacket);

	public byte[] getSyncPacket();

	public long getTimeStart();

	public void broadcast(byte[] dataPacket);

	public int getSessionId();

	public byte getNumberOfClients();

	public long getPing();

}
