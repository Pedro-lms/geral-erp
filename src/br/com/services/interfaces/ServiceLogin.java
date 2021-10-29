package br.com.services.interfaces;

import java.io.Serializable;

import org.springframework.stereotype.Service;

@Service
public interface ServiceLogin extends Serializable {
	boolean autentico(String login, String senha) throws Exception;

}
