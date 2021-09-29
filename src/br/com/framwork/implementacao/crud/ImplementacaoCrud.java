package br.com.framwork.implementacao.crud;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.framwork.hibernate.session.HibernateUtil;
import br.com.framwork.interfaces.crud.InterfaceCrud;

@Component
@Transactional
public class ImplementacaoCrud <T> implements InterfaceCrud<T> {

	private static final long serialVersionUID = 1L;

	private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	
	@Autowired
	private JdbcTemplateImplementa jdbcTemplate;
	
	@Autowired
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	@Autowired
	private SimpleJdbcInsertImplementa simpleJdbcInsertImplementa;
	
	@Autowired
	private SimpleJdbcClassImplementa simpleJdbcClassImplementa;
	
	public JdbcTemplateImplementa getJdbcTemplateImplementa() {
		return jdbcTemplate;
	}
	
	public SimpleJdbcInsertImplementa getSimpleJdbcInsertImplementa() {
		return simpleJdbcInsertImplementa;
	}
	
	public SimpleJdbcClassImplementa getJdbcClassImplementa() {
		return simpleJdbcClassImplementa;
	}
	
	public SimpleJdbcTemplate getSimplesJdbcTemplate() {
		return simpleJdbcTemplate;
	}
	
	@Override
	public void save(T obj) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().save(obj);
	}

	@Override
	public void persist(T obj) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().persist(obj);
		executeFlushSession();
	}

	/*
	 * Se já tiver uma primary key atualiza
	 * se não, salva
	 * Merge faz o mesmo, mas retornando o objeto, 
	 * enquanto o save não faz 
	 */
	@Override
	public void saveOrUpdate(T obj) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().saveOrUpdate(obj);
		executeFlushSession();
	}

	@Override
	public void update(T obj) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().saveOrUpdate(obj);
		executeFlushSession();
	}

	@Override
	public void delete(T obj) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().delete(obj);
		executeFlushSession();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T merge(T obj) throws Exception {
		validaSessionFactory();
		obj = (T) sessionFactory.getCurrentSession().merge(obj);
		executeFlushSession();
		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findList(Class<T> entidade) throws Exception {
		validaSessionFactory();
		
		StringBuilder query = new StringBuilder();
		query.append("select distinct(entity) from").append(entidade.getSimpleName()).append("entity");
		
		List<T> lista = sessionFactory.getCurrentSession().createQuery(query.toString()).list();
		
		return lista;
	}

	@Override
	public Object findById(Class<T> entidade, Long id) throws Exception {
		validaSessionFactory();
		Object obj = sessionFactory.getCurrentSession().load(getClass(), id);
		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findGenericById(Class<T> entidade, Long id) throws Exception {
		validaSessionFactory();
		T obj = (T) sessionFactory.getCurrentSession().load(getClass(), id);
		return obj;
	}

	/*
	 * Encontra query por estrutura do tipo lista
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findListQueryDynamic(String s) throws Exception {
		validaSessionFactory();
		List<T> lista = new ArrayList<T>();
		lista = sessionFactory.getCurrentSession().createQuery(s).list();
		return lista;
	}

	/*
	 * HQL para executar uma query dinâmica
	 */
	@Override
	public void executeUpdateQueryDynamic(String s) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().createQuery(s).executeUpdate();
	}

	/*
	 * SQL para executar uma query dinâmica
	 */
	@Override
	public void executeUpdateSQLDynamic(String s) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().createSQLQuery(s).executeUpdate();
		executeFlushSession();
	}

	/*
	 * Limpa toda a sessão para evitar cash
	 */
	@Override
	public void clearSession() throws Exception {
		sessionFactory.getCurrentSession().clear();
	}

	/*
	 * Limpa um objeto em específico
	 */
	@Override
	public void evict(Object obj) throws Exception {
		sessionFactory.getCurrentSession().evict(obj);
	}

	/*
	 * Retorna a sessão
	 */
	@Override
	public Session getSession() throws Exception {
		validaSessionFactory();
		return sessionFactory.getCurrentSession();
	}

	/*
	 * Retorna o SQL na forma de lista dinâmica
	 */
	@Override
	public List<?> getListSQLDynamic(String sql) throws Exception {
		validaSessionFactory();
		List<?> lista = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		return lista;
	}

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	@Override
	public SimpleJdbcInsert getSimpleJdbcInsert() {
		return simpleJdbcInsertImplementa;
	}

	@Override
	public Long totalRegistro(String table) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(1) from ").append(table);
		return jdbcTemplate.queryForLong(sql.toString());
	}

	/*
	 * Tabela generica de consulta
	 */
	@Override
	public Query obterQuery(String query) throws Exception {
		validaSessionFactory();
		Query queryReturn = (Query) sessionFactory.getCurrentSession().createQuery(query.toString());
		return queryReturn;
	}

	/*
	 *  Realiza consulta no banco de dados, iniciando o carregamento
	 * a partir do registro passado no parametro -> iniciaNoRegistro e obtendo o
	 * máximo de resultados passados em -> maximoResultado
	 * 
	 * @param query
	 * @param iniciaNoRegistro
	 * @param maximoResultado
	 * @return List<T
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findListByQueryDynamic(String query, int iniciaNoRegistro, int maximoResultado) throws Exception {
		validaSessionFactory();
		List<T> lista = new ArrayList<T>();
		lista = sessionFactory.getCurrentSession().createQuery(query).setFirstResult(iniciaNoRegistro).setMaxResults(maximoResultado).list();
		return lista;
	}
	
	private void validaSessionFactory() {
		if(sessionFactory == null) {
			sessionFactory = HibernateUtil.getSessionFactory();
		}
		validaTransaction();
	}
	
	/*
	 * Se por um acaso não tiver iniciado uma conexão do ORM entre Java e SQL
	 * Hibernate inicia a sessão
	 */
	private void validaTransaction() {
		if (!sessionFactory.getCurrentSession().getTransaction().isActive()) {
			sessionFactory.getCurrentSession().beginTransaction(); 
		}
	}
	
	/*
	 * Inicia o processo de commit por requisição Ajax
	 */
	@SuppressWarnings("unused")
	private void commitProcessoAjax() {
		sessionFactory.getCurrentSession().beginTransaction().commit();
	}
	
	@SuppressWarnings("unused")
	private void rollBackProcessoAjax() {
		sessionFactory.getCurrentSession().beginTransaction().rollback();
	}
	
	/*
	 * Roda instantaneamente o SQL no banco de dados
	 * Evita o Cash no sistema
	 */
	private void executeFlushSession() {
		sessionFactory.getCurrentSession().flush();
	}
	
	@SuppressWarnings("unchecked")
	public 	List<Object[]> getListSQLDynamicArray(String sql) throws Exception{
		validaSessionFactory();
		
		List<Object[]> lista = (List<Object[]>) sessionFactory.getCurrentSession().createQuery(sql).list();
		return lista;
	}
}
