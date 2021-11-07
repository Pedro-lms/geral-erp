package teste.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Calendar;

import org.junit.Test;

import br.com.project.report.util.DateUtils;

public class TesteData {

	@Test
	public void testData() {
		System.out.println(DateUtils.getDateAtualReportName());
		
		try {
			
		//	assertEquals("24/09/2021", DateUtils.getDateAnualReportName());
			
			assertEquals("'2021-09-24'", DateUtils.formatDateSql(Calendar.getInstance().getTime()));

			assertEquals("2021-09-24", DateUtils.formatDateSqlSimple( Calendar.getInstance().getTime()));		
		}catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
