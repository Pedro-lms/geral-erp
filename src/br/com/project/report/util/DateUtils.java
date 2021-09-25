package br.com.project.report.util;

import java.io.Serializable;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static String getDateAnualReportName() {
		DateFormat df = new SimpleDateFormat("ddMMyyyy");
		return df.format(Calendar.getInstance().getTime());
	}
	
	public static String formateDateSQL (Date data) {
		StringBuffer retorno = new StringBuffer();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		retorno.append("'");
		retorno.append(df.format(data));
		retorno.append("'");
		return retorno.toString();
	}

	public static String formateDateSQLSimple (java.util.Date date) {
		StringBuffer retorno = new StringBuffer();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		retorno.append(df.format(date));
		return retorno.toString();
	}

	public static Object formateDateSQL(java.util.Date time) {
		StringBuffer retorno = new StringBuffer();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		retorno.append("'");
		retorno.append(df.format(time));
		retorno.append("'");
		return retorno.toString();
	}

}
