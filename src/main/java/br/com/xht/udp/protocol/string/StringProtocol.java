package br.com.xht.udp.protocol.string;

import br.com.xht.udp.handler.IUdpHello;
import br.com.xht.udp.handler.IUdpProtocol;

public class StringProtocol implements IUdpProtocol {

	@Override
	public IUdpHello getNewHelloInstance() {
		return new StringHello();
	}

}
