package br.com.services.implementacao;

import br.com.services.interfaces.ServiceLogin;

public class ServiceLoginImpl implements ServiceLogin {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean autentico(String login, String senha) throws Exception {
		return false;
	}

}
