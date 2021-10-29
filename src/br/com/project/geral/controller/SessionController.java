package br.com.project.geral.controller;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.servlet.http.HttpSession;

@ApplicationScoped //Escopo de aplica��o �nico j� que apenas uma inst�ncia � armazenada na mem�ria
public interface SessionController extends Serializable {

	void addSession(String keyLoginUser, HttpSession httpSession);
	
	void invalidateSession(String keyLoginUser);
}
