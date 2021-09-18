package br.com.framwork.hibernate.session;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.faces.bean.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.SessionFactoryImplementor;

import br.com.framwork.implementacao.crud.VariavelConexaoUtil;

/**
 * Responsavel por estabelecer a conex�o do hibernate
 * 
 */
@ApplicationScoped /*Estabelece a conex�o */
public class HibernateUtil implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static String JAVA_COMP_ENV_JDBC_DATA_SOURCE = "java:/comp/env/jdbc/postgres";
	
	private static SessionFactory sessionFactory = buildSessionFactory();
	
	/**
	 * Respons�vel por ler o arquivo de configura��o hibernate.cfg.xml
	 * @return SessionFactory
	 */
	private static SessionFactory buildSessionFactory() {
		
		try {
			
			if (sessionFactory == null) {
				sessionFactory = new Configuration().configure().buildSessionFactory();
			}
			return sessionFactory;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError("Erro ao criar a conex�o SessionFactory");
		}
	}
	
	/*
	 * Retorna o sessionFactory corrente
	 * @return SessionFactory
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	/*
	 * Retorna  a sess�o do SessionFactory
	 * @retrun Session
	 */
	public static Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	/*
	 * Abre uma nova sess�o no SessionFactory
	 * return Session
	 */
	
	public static Session openSession() {
		if (sessionFactory == null) {
			buildSessionFactory();
		}
		
		return sessionFactory.openSession();
	}
	
	/*
	 * Obtem a conex�o do provedor de conex�es configurado
	 * @return Connection SQL
	 * @throws SQL exception
	 */
	public static Connection getConnetctionProvider() throws SQLException {
		return ((SessionFactoryImplementor) sessionFactory).getConnectionProvider().getConnection();
	}
	
	/*
	 * @return Connection no initial Context java:/comp/env/jdbc/datasource 
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception{
		InitialContext context = new InitialContext();
		DataSource ds = (DataSource) context.lookup(JAVA_COMP_ENV_JDBC_DATA_SOURCE);
		
		return ds.getConnection();
	}

	/*
	 * @return DataSource JNDI Tomcat
	 * @throws Naming Exception
	 */
	public DataSource getDataSourceJndi () throws NamingException {
		InitialContext context = new InitialContext();
		
		return (DataSource) context.lookup(VariavelConexaoUtil.JAVA_COMP_ENV_JDBC_DATA_SOURCE);
	}

}

