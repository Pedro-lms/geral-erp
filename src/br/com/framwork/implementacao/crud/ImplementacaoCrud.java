package br.com.framwork.implementacao.crud;

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persist(T obj) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveOrUpdate(T obj) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(T obj) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(T obj) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T merge(T obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findList(Class<T> objs) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object findById(Class<T> entidade, Long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T findGenericById(Class<T> entidade, Long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findListQueryDynamic(String s) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void executeUpdateQueryDynamic(String s) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeUpdateSQLDynamic(String s) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearSession() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evict(Object obj) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Session getSession() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<?> getListSQLDynamic(String sql) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JdbcTemplate getJdbcTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimpleJdbcInsert getSimpleJdbcInsert() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long totalRegistro(String table) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query obterQuery(String query) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findListByQueryDynamic(String query, int iniciaNoRegistro, int maximoResultado) throws Exception {
		// TODO Auto-generated method stub
		return null;
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
	private void commitProcessoAjax() {
		sessionFactory.getCurrentSession().beginTransaction().commit();
	}
	
	private void rollBackProcessoAjax() {
		sessionFactory.getCurrentSession().beginTransaction().commit();
	}
}
