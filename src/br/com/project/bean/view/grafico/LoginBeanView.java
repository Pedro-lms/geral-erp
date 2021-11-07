package br.com.project.bean.view.grafico;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.framework.interfac.crud.InterfaceCrud;
import br.com.project.bean.geral.BeanManagedViewAbstract;
import br.com.project.geral.controller.SessionController;
import br.com.services.interfaces.ServiceLogin;

@Controller
@Scope(value="request")
@ManagedBean(name = "loginBeanView") //Mapeameto ao controlador do JSF dado o modelo arquitetural MVC
public class LoginBeanView extends BeanManagedViewAbstract{

	private static final long serialVersionUID = 1L;

	private String usename = null;
	private String password = null;
	
	@Resource
	private SessionController sessionController;
	
	@Resource
	private ServiceLogin serviceLogin;
	
	public void invalidar(ActionEvent actionEvent) throws Exception{
		RequestContext requestContext = RequestContext.getCurrentInstance();
		FacesMessage facesMessage = null;
		boolean loggedIn = false;
		
		if (serviceLogin.autentico(getUsename(), getPassword())) {
			sessionController.invalidateSession(getUsename());
			loggedIn = true;
		}else {
			loggedIn = false;
			facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "Acesso negado", "Login ou senha incorreto");
		}
		
		if(facesMessage != null) {
			FacesContext.getCurrentInstance().addMessage("msg", facesMessage);
		}
		requestContext.addCallbackParam("mensagemRetorno", "Java");
	}
	
	public String getUsename() {
		return usename;
	}

	public void setUsename(String usename) {
		this.usename = usename;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public SessionController getSessionController() {
		return sessionController;
	}

	public void setSessionController(SessionController sessionController) {
		this.sessionController = sessionController;
	}

	public ServiceLogin getServiceLogin() {
		return serviceLogin;
	}

	public void setServiceLogin(ServiceLogin serviceLogin) {
		this.serviceLogin = serviceLogin;
	}

	@Override
	public void addMsg() throws Exception {		
	}

	@Override
	protected Class<?> getClassImplement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected InterfaceCrud<?> getController() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
