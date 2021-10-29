package br.com.repository.interfaces;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

@Repository //Interface de conexão com o banco de dados
public interface RepositoryLogin extends Serializable {

	boolean autentico(String login, String senha) throws Exception;
}
