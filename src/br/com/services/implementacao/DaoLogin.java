package br.com.services.implementacao;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import br.com.framwork.implementacao.crud.ImplementacaoCrud;
import br.com.repository.interfaces.RepositoryLogin;

public class DaoLogin extends ImplementacaoCrud<Object> implements RepositoryLogin {

	private static final long serialVersionUID = 1L;
	/*
	 * Autentica��o junto ao banco de dados
	 * Neste ponto � mais vanjatoso utilizar o JDBC puro por quest�o de performance em compara��o com o hibernate
	 */
	public boolean autentico(String login, String senha) throws Exception {
		String sql = "Select count(1) as autentica from entidade where ent_login = ? and ent_senha = ? ";
		SqlRowSet sqlRowSet = super.getJdbcTemplate().queryForRowSet(sql, new Object[]{login, senha});
		return sqlRowSet.next() ? sqlRowSet.getInt("autentica") > 0 : false;
	}

}
