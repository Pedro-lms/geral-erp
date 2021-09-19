package br.com.framwork.implementacao.crud;

import java.io.Serializable;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class JdbcTemplateImplementa extends JdbcTemplate implements Serializable {

	private static final long serialVersionUID = 1L;


	public JdbcTemplateImplementa(DataSource dataSource) {
		super(dataSource);
	}
}
