package br.com.xht.udp.handler;

public interface IPacketProcessor {

	public byte[] getProcessed(byte[] data, byte sessionFromClientIdx);

	public void syncStopped();

	public boolean isSyncStopped();

}
