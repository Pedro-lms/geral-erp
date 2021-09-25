package br.com.project.util.all;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public abstract class Messages extends FacesContext implements Serializable{

	private static final long serialVersionUID = 1L;

	public Messages() {
	}
	
	public static void responseOperation(EstatusPersistencia estatusPersistencia) {
		if (estatusPersistencia != null && estatusPersistencia.equals(EstatusPersistencia.SUCESSO)) {
			sucesso();
		}else if (estatusPersistencia != null && estatusPersistencia.equals(EstatusPersistencia.OBJETO_REFERENCIADO)) {
			messageSeverityFatal(EstatusPersistencia.OBJETO_REFERENCIADO.toString()); //Aqui eu chamo o toString porque usei polimorfismo para passar o nome
		}
	}
	
	public static void message(String message) {
		if (facesContextValido()) {
			getFacesContext().addMessage("message", new FacesMessage(message));
		}
	}
	
	public static void sucesso() {
		messageSeverityInfo(Constante.OPERACAO_REALIZADA_COM_SUCESSO);
	}
	
	public static void erroNaOperacao() {
		if (facesContextValido()) {
			messageSeverityFatal(Constante.ERRO_NA_OPERACAO);
		}
	}
	
	public static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}
	
	public static void messageSeverityFatal(String message) {
		if(facesContextValido()) {
			getFacesContext().addMessage(message, new FacesMessage(FacesMessage.SEVERITY_FATAL, message, message));
		}
	}
	
	public static void messageSeverityInfo(String message) {
		if(facesContextValido()) {
			getFacesContext().addMessage(message, new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));
		}
	}
	
	public static void messageSeverityError(String message) {
		if(facesContextValido()) {
			getFacesContext().addMessage(message, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
		}
	}
	
	private static boolean facesContextValido() {
		return getFacesContext() != null;
	}
	
	public static void messageSeverityWarn(String message) {
		if(facesContextValido()) {
			getFacesContext().addMessage(message, new FacesMessage(FacesMessage.SEVERITY_WARN, message, message));
		}
	}
	
}
