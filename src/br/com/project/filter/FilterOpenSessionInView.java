package br.com.project.filter;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.filter.DelegatingFilterProxy;

import br.com.framework.utils.UtilFrameworks;
import br.com.framwork.hibernate.session.HibernateUtil;
import br.com.project.listener.ContextLoaderListenerCaixaUtils;
import br.com.project.model.classes.Entidade;

/**
 * Servlet Filter implementation class FilterOpenSessionInView
 */
@WebFilter(filterName="conexaoFilter")
public class FilterOpenSessionInView extends DelegatingFilterProxy implements Filter, Serializable {

    private static final long serialVersionUID = 1L;

	/**
     * Default constructor. 
     */
    public FilterOpenSessionInView() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		
		super.doFilter(request, response, filterChain);
		
		//JDBC do Ecossistam Spring
		BasicDataSource springBasicDataSource = (BasicDataSource) ContextLoaderListenerCaixaUtils.getBean("SpringDataSource");
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(springBasicDataSource);
		TransactionStatus transactionStatus = transactionManager.getTransaction(definition);
		
		try {
			request.setCharacterEncoding("UTF-8"); //Evita problemas de ortografia entre o ingl�s e a acentua��o do portugu�s
			
			//Intercepta o usu�rio que faz a requisi��o
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpSession sessao = httpServletRequest.getSession();
			Entidade usuarioLogadoNaSessao =  (Entidade) sessao.getAttribute("Usu�rioLogadoNaSessao");
			
			if (usuarioLogadoNaSessao != null) {
				UtilFrameworks.getThreadLocal().set(usuarioLogadoNaSessao.getEnt_codigo());
			}
			
			sf.getCurrentSession().beginTransaction(); //Inicia a transa��o do Hibernate
			
			//Antes de executar a��o(Request)
			filterChain.doFilter(request, response); //Execu��o da a��o
			//Ap�s a exceu��o de a��o(Resposta)
			transactionManager.commit(transactionStatus); //Faz a atualiza��o do status do sistema
			
			if (sf.getCurrentSession().getTransaction().isActive()) {
				sf.getCurrentSession().flush();
				sf.getCurrentSession().getTransaction().commit();
			}
			
			//Abertura e fechamento das sess�es - NUNCA DA CONEX�O!
			if (sf.getCurrentSession().isOpen()) {
				sf.getCurrentSession().close();
			}
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
						
		}catch (Exception e) {
			
			transactionManager.rollback(transactionStatus);
			
			e.printStackTrace();
			
			if (sf.getCurrentSession().getTransaction().isActive()) {
				sf.getCurrentSession().getTransaction().rollback();
			}
			
			if (sf.getCurrentSession().isOpen()) {
				sf.getCurrentSession().close();
			}
		}finally {
			if (sf.getCurrentSession().isOpen()) {
				if(sf.getCurrentSession().beginTransaction().isActive()) { //Verifica se a transa��o ta ativa
					sf.getCurrentSession().flush(); //Executa o que t� no banco
					sf.getCurrentSession().clear(); //Limpa
				}
				
				if (sf.getCurrentSession().isOpen()) {
					sf.getCurrentSession().close();
				}
			}
		}
	}
	
	private static SessionFactory sf;
	
	public static SessionFactory getSf() {
			return sf;
		}
	
		public static void setSf(SessionFactory sf) {
			FilterOpenSessionInView.sf = sf;
		}
	/**
	 * @see Filter#init(FilterConfig)
	 * Executa apenas uma vez
	 * Executado quando a aplica��o est� sendo iniciada
	 */
	@Override
	protected void initFilterBean() throws ServletException {
		setSf(HibernateUtil.getSessionFactory());
	}

	

}
