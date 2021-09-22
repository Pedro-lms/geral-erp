package br.com.framwork.utils;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class UtilFrameworks implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();
	
	public synchronized static ThreadLocal<Long> geThreadLocal(){
		return threadLocal;
	}
}
