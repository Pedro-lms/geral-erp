package br.com.project.exception;

import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.hibernate.SessionFactory;
import org.primefaces.context.RequestContext;

import br.com.framwork.hibernate.session.HibernateUtil;

public class CustomExceptionHandler extends ExceptionHandlerWrapper{

	private ExceptionHandler wrapperd;
	
	final FacesContext facesContext = FacesContext.getCurrentInstance();
	
	final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
	
	final NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();
	
	public CustomExceptionHandler(ExceptionHandler exceptionHandler) {
		this.wrapperd = exceptionHandler;
	}
	
	/*
	 * Sobrescreve o m�todo ExceptionHandler que retorna a "pilha" de exce��es
	 */
	@Override
	public ExceptionHandler getWrapped() {
		return wrapperd;
	}
	
	/*
	 *Sobreescreve o metodo handle que � respons�vel por manipular as exce��es do JSF
	 */
	@Override
	public void handle() throws FacesException {
		final Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();
		
		while(iterator.hasNext()) {
			ExceptionQueuedEvent event = iterator.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
			
			//Recuperar a exce��o do contexo
			Throwable exception = context.getException();
			
			//Tratamento da exce��o
			try {
	
				requestMap.put("exceptionMessage", exception.getMessage());
				
				if(exception != null 
						&& exception.getMessage() != null
						&& exception.getMessage().indexOf("ConstraintViolantionException") != -1) {//Diferente de menos 1

					FacesContext.getCurrentInstance()
					.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_WARN,"Registro n�o pode ser removido por ",
							"estar associado"));
				
				}else if (exception != null 
						&& exception.getMessage() != null
						&& exception.getMessage().indexOf("org.hibernate.Sta�eObjectStateException") != -1) {//Diferente de menos 1

					FacesContext.getCurrentInstance()
					.addMessage("message", new FacesMessage
							(FacesMessage.SEVERITY_ERROR,"Registro foi atualizado ou pode ter sido removido por outro usu�rio",
							"Consulte novamente."));
				
				}else {
					//Avisa ao usu�rio do erro
					FacesContext.getCurrentInstance()
					.addMessage("message", new FacesMessage
							(FacesMessage.SEVERITY_FATAL,"O sistema apresentou um erro inesperado",
							""));
					
					//Tranquiliza o usu�rio
					FacesContext.getCurrentInstance()
					.addMessage("message", new FacesMessage
							(FacesMessage.SEVERITY_INFO,"Fique tranquilo! Voc� pode continuar usando o sistema normalmente",
							"Consulte novamente."));
					
					//Informa o porqu� do erro
					FacesContext.getCurrentInstance()
					.addMessage("message", new FacesMessage
							(FacesMessage.SEVERITY_FATAL,"Erro causado por: \n",
							exception.getMessage()));
					
					//Caso a p�gina n�o redirecional o primefaces faz um alert javascriot
					RequestContext.getCurrentInstance().execute("alert('O sistema se recuperou de um erro inesperado");
					
					RequestContext.getCurrentInstance()
					.showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "O sistema se recuperou de um erro inexperado"));
					
					//Redirecionamento para p�gina de erro
					navigationHandler.handleNavigation(facesContext, null, "/error/error.jsf?faces-redirect=true&expired=true");
				}
				
				//Renderiza��o de resposta
				facesContext.renderResponse();
				
				
			}finally {
				SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
				if (sessionFactory.getCurrentSession().getTransaction().isActive()) {
					sessionFactory.getCurrentSession().getTransaction().rollback();
				}
			}
			//Imprime o erro no console
			exception.printStackTrace();
			
			iterator.remove();
		}
		
		getWrapped().handle();
	}
	
}
