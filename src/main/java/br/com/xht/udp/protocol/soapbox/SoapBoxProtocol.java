package br.com.xht.udp.protocol.soapbox;

import br.com.xht.udp.handler.IUdpHello;
import br.com.xht.udp.handler.IUdpProtocol;

public class SoapBoxProtocol implements IUdpProtocol {

	@Override
	public IUdpHello getNewHelloInstance() {
		return new SoapBoxHello();
	}

}
