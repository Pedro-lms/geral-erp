package br.com.framwork.interfaces.crud;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public interface InterfaceCrud <T> extends Serializable{
	
	//Salva os dados
	void save(T obj) throws Exception;
	
	//Realiza a persistência dos dados
	void persist (T obj) throws Exception;
	
	//Salva ou atualiza
	void saveOrUpdate (T obj) throws Exception;
	
	//Realiza o update e atualização dos dados
	void update (T obj) throws Exception;
	
	//Realiza a remoção dos dados
	void delete (T obj) throws Exception;
	
	//Salva ou atualiza e retorna o objeto em estado persistente
	T merge (T obj) throws Exception;
	
	//Carrega a lista de dados de uma determinada classe
	List<T> findList(Class<T> objs) throws Exception;

	//Encontra pelo id
	Object findById(Class<T> entidade, Long id) throws Exception;
	
	//Encontra pelo id a partir de um genérico
	T findGenericById(Class<T> entidade, Long id) throws Exception;
	
	//Encontra por uma query estruturada em lista com HQL 
	List<T> findListQueryDynamic(String s) throws Exception;
	
	//Executa um update com HQL
	void executeUpdateQueryDynamic(String s) throws Exception;
	
	//Executa um update com SQL
	void executeUpdateSQLDynamic(String s) throws Exception;
	
	//Limpa a sessão do hibernate
	void clearSession() throws Exception;
	
	//Retira um objeto da sessão do hibernate que pode causar cash
	void evict (Object obj) throws Exception;
	
	Session getSession() throws Exception;
	
	List<?> getListSQLDynamic (String sql) throws Exception;
	
	JdbcTemplate getJdbcTemplate();
	
	SimpleJdbcTemplate getSimpleJdbcTemplate();
	
	SimpleJdbcInsert getSimpleJdbcInsert();
	
	Long totalRegistro(String table) throws Exception;
	
	Query obterQuery(String query) throws Exception;
	
	//Carregamento dinamico com JSF e Primefaces
	List<T> findListByQueryDynamic(String query, int iniciaNoRegistro, int maximoResultado) throws Exception;;
	
}
