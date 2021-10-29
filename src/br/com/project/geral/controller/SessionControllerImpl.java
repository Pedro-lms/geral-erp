package br.com.project.geral.controller;

import java.util.HashMap;

import javax.faces.bean.ApplicationScoped;
import javax.servlet.http.HttpSession;

@ApplicationScoped
public class SessionControllerImpl implements SessionController{

	private static final long serialVersionUID = 1L;

	private HashMap<String, HttpSession> hashMap = new HashMap<String, HttpSession>();
	
	@Override
	public void addSession(String keyLoginUser, HttpSession httpSession) {
		hashMap.put(keyLoginUser, httpSession); //Identificador da chave do usuário 
	}

	@Override
	public void invalidateSession(String keyLoginUser) {
		
		HttpSession httpSession = hashMap.get(keyLoginUser);
		
		if (httpSession != null) {
			try {
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("Não há usuário");
		}
		
		hashMap.remove(keyLoginUser);
	}

}
