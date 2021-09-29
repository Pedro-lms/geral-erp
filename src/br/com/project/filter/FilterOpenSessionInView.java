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
			request.setCharacterEncoding("UTF-8"); //Evita problemas de ortografia entre o inglês e a acentuação do português
			
			//Intercepta o usuário que faz a requisição
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpSession sessao = httpServletRequest.getSession();
			Entidade usuarioLogadoNaSessao =  (Entidade) sessao.getAttribute("UsuárioLogadoNaSessao");
			
			if (usuarioLogadoNaSessao != null) {
				UtilFrameworks.getThreadLocal().set(usuarioLogadoNaSessao.getEnt_codigo());
			}
			
			sf.getCurrentSession().beginTransaction(); //Inicia a transação do Hibernate
			
			//Antes de executar ação(Request)
			filterChain.doFilter(request, response); //Execução da ação
			//Após a exceução de ação(Resposta)
			transactionManager.commit(transactionStatus); //Faz a atualização do status do sistema
			
			if (sf.getCurrentSession().getTransaction().isActive()) {
				sf.getCurrentSession().flush();
				sf.getCurrentSession().getTransaction().commit();
			}
			
			//Abertura e fechamento das sessões - NUNCA DA CONEXÃO!
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
				if(sf.getCurrentSession().beginTransaction().isActive()) { //Verifica se a transação ta ativa
					sf.getCurrentSession().flush(); //Executa o que tá no banco
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
	 * Executado quando a aplicação está sendo iniciada
	 */
	@Override
	protected void initFilterBean() throws ServletException {
		setSf(HibernateUtil.getSessionFactory());
	}

	

}
