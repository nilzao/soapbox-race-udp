package br.com.xht.udp.handler;

public interface IPacketProcessor {

	public byte[] getProcessed(byte[] data);

	public void syncStopped();

	public boolean isSyncStopped();

}
