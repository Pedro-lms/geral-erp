package br.com.project.util.all;

public enum EstatusPersistencia {

	ERRO("Erro"), 
	SUCESSO("Sucesso"),
	OBJETO_REFERENCIADO("Esse objeto n�o pode ser apagado por possuir refer�ncia a si pr�prio");
	
	private String name;
	
	private EstatusPersistencia(String s) {
		this.name = s;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
