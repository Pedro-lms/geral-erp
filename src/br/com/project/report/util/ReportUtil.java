package br.com.project.report.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Component;

import com.lowagie.text.pdf.codec.Base64.InputStream;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;

@Component
public class ReportUtil implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static final String UNDERLINE = "_";
	private static final String PONTO = ".";
	private static final String FOLDER_RELATORIOS = "/relatorios";
	private static final String SUBREPORT_DIR = "SUBREPORT_DIR";
	private static final String EXTENSION_PDF = "pdf";
	private static final String EXTENSION_XLS = "xls";
	private static final String EXTENSION_ODS = "ods";
	private static final String EXTENSION_HTML = "html";
	private static final String EXTENSION_TXT = "txt";
	private static final String EXTENSION_CSV = "csv";
	private String SEPARATOR = File.separator;
	private static final int RELATORIO_PDF = 1;
	private static final int RELATORIO_EXCEL = 2;
	private static final int RELATORIO_HTML = 3;
	private static final int RELATORIO_PLANILHA_OPEN_OFICE = 4;
	private static final int RELATORIO_CSV = 5;
	private static final int RELATORIO_DOCX = 6;
	private StreamedContent arquivoRetorno = null;
	private String caminhoArquivoRelatorio = null;
	private JRExporter tipoArquivoExportado = null;
	private String extensaoArquivoExportado = "";
	private String caminhoSubreport_dir = "";
	private File arquivoGerado = null;
	
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public StreamedContent geraRelatorio(List<?> listDataBeanCollectionReport,
			HashMap parametrosRelatorio, String nomeRelatorioJasper,
			String nomeRelatorioSaida, int tipoRelatorio) throws Exception {
		
		/*
		 *Cria a lista de collectionDatasource de beans que carregam os dados para o relat�rio 
		 */
		JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listDataBeanCollectionReport);
		
		/*
		 * Fornece o caminho f�sico at� a pasta que cont�m os relat�rios compilados .jasper
		 */
		FacesContext context = FacesContext.getCurrentInstance();
		context.responseComplete();
		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
		String caminhoRelatorio = servletContext.getRealPath(FOLDER_RELATORIOS);
	
		//Ex: c://programas/c.txt
		File file = new File(caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "jasper");
		
		if(caminhoRelatorio == null || (caminhoRelatorio != null && caminhoRelatorio.isEmpty() || !file.exists())) {
			caminhoRelatorio = this.getClass().getResource(FOLDER_RELATORIOS).getPath();
			SEPARATOR = "";
		}
		
		/*
		 * Caminho para imagens
		 */
		parametrosRelatorio.put("REPORT_PARAMETERS_IMG", caminhoRelatorio);
		
		/*
		 * Caminho completo at� o relat�rio compilado indicado
		 */
		String caminhoArquivoJasper = caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "jasper";
		
		/*
		 * Faz o carregamento do relat�rio indicado
		 */
		JasperReport relatorioJasper = (JasperReport) JRLoader.loadObjectFromFile(caminhoArquivoJasper);
		
		/*
		 * Configura par�metros como caminho f�sico para sub-reports
		 */
		caminhoSubreport_dir = caminhoRelatorio + SEPARATOR;
		parametrosRelatorio.put(SUBREPORT_DIR, caminhoSubreport_dir);
		
		/*
		 * Carrega o arquivo compilado para a mem�ria
		 * Chave do que quero:> define a extens�o desejada
		 */
		JasperPrint impressoraJasper = JasperFillManager.fillReport(relatorioJasper, parametrosRelatorio, jrbcds);
		
		switch (tipoRelatorio) {
		case RELATORIO_PDF:
			tipoArquivoExportado = new JRPdfExporter();
			extensaoArquivoExportado = EXTENSION_PDF;
			break;

		case RELATORIO_HTML:
			tipoArquivoExportado = new JRHtmlExporter();
			extensaoArquivoExportado = EXTENSION_HTML;
			break;
			
		case RELATORIO_EXCEL:
			tipoArquivoExportado = new JRXlsExporter();
			extensaoArquivoExportado = EXTENSION_XLS;
			break;
		
		case RELATORIO_CSV:
			tipoArquivoExportado = new JRCsvExporter();
			extensaoArquivoExportado = EXTENSION_CSV;
			break;
				
		case RELATORIO_DOCX:
			tipoArquivoExportado = new JRDocxExporter();
			extensaoArquivoExportado = EXTENSION_TXT;
			break;
			
		case RELATORIO_PLANILHA_OPEN_OFICE:
			tipoArquivoExportado = new JROdtExporter();
			extensaoArquivoExportado = EXTENSION_ODS;
			break;
			
		default:
			tipoArquivoExportado = new JRPdfExporter();
			extensaoArquivoExportado = EXTENSION_PDF;
			break;
		}
		
		nomeRelatorioSaida += UNDERLINE + DateUtils.getDateAnualReportName();
		
		/*
		 * caminho relatorio exportado
		 */
		caminhoArquivoRelatorio = caminhoRelatorio + SEPARATOR + nomeRelatorioSaida + PONTO;
		
		/*
		 * Cria novo file exportado
		 */
		arquivoGerado =new File(caminhoArquivoRelatorio);
		
		/*
		 * Preparar a impress�o
		 */
		tipoArquivoExportado.setParameter(JRExporterParameter.JASPER_PRINT, impressoraJasper);
		
		/*
		 * Nome do arquivo f�sico a ser exportado
		 */
		tipoArquivoExportado.setParameter(JRExporterParameter.JASPER_PRINT, arquivoGerado);
		
		/*
		 * Execu��o da exporta��o
		 */
		tipoArquivoExportado.exportReport();
		
		/*
		 * Remove o arquivo do servidor ap�s ser feito o download pelo usu�rio
		 */
		arquivoGerado.deleteOnExit();
		
		/*
		 * Cria o imput stwarm para ser utilizado pelo PrimeFaces
		 */
		FileInputStream conteudoRelatorio =  new FileInputStream(arquivoGerado);
		
		/*
		 * Faz o retorno para a aplica��o
		 */
		arquivoRetorno = new DefaultStreamedContent(conteudoRelatorio, "application/"+extensaoArquivoExportado 
				+ nomeRelatorioSaida + PONTO + extensaoArquivoExportado);
		return arquivoRetorno;
	}
}
