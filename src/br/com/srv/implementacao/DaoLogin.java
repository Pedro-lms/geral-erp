package br.com.srv.implementacao;

import br.com.framwork.implementacao.crud.ImplementacaoCrud;
import br.com.repository.interfaces.RepositoryLogin;

public class DaoLogin extends ImplementacaoCrud<Object> implements RepositoryLogin {

	private static final long serialVersionUID = 1L;
/*
 * Autenticação junto ao banco de dados
 * Neste ponto é mais vanjatoso utilizar o JDBC puro por questão de performance em comparação com o hibernate
 */
	public boolean autentico(String login, String senha) throws Exception {
		String sql = "Select count(1) as ";
		return false;
	}

}
