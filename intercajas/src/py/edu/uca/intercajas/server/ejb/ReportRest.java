package py.edu.uca.intercajas.server.ejb;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import py.edu.uca.intercajas.server.pdfSign.Signatures;
import py.edu.uca.intercajas.shared.entity.Beneficiario;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Path("/report")
@Stateless
public class ReportRest {

	@Resource(mappedName = "java:jboss/datasources/IntercajasDS")
	DataSource dataSource;

	@PersistenceContext
	EntityManager em;
	
	@Path("/auditoriaPorUsuario")
	@GET
	@Produces("text/plain")
	public String auditoriaPorUsuario(@QueryParam(value = "desde") String desde,
			           @QueryParam(value = "hasta") String hasta,
			           @QueryParam(value = "usuario") String usuario) {
		System.out.println("rest working");
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			parameters.put("desde", new java.sql.Date(df.parse(desde).getTime()));
			parameters.put("hasta", new java.sql.Date(df.parse(hasta).getTime()));
			parameters.put("usuario", usuario);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return print("/home/cesar/docs/git/intercajas/reports/auditoria.jrxml", parameters);
	}

	
	@Path("/totalizacion")
	@GET
	@Produces("text/plain")
	public String totalizacion(@QueryParam(value = "param") Long solicitud_id) {
		System.out.println("rest working");
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("solicitud_id", solicitud_id);
		return print("/home/cesar/docs/git/intercajas/reports/totalizacion.jrxml", parameters);
	}	
	
	public String print(String reportName, Map<String, Object> parameters) {

		try {
			String filePath = "/home/cesar/principal/reports/";
			String fileName = "rep-" + new Date().getTime() + ".pdf";

			// Compile jrxml file.
			JasperReport jasperReport = JasperCompileManager
					.compileReport(reportName);

			// Parameters for report

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, dataSource.getConnection());

			// Make sure the output directory exists.
			File outDir = new File(filePath);
			outDir.mkdirs();

			// Export to PDF.
			JasperExportManager.exportReportToPdfFile(jasperPrint, filePath	+ fileName);


			//Firmamos
			String fileNameSigned = "rep-" + new Date().getTime() + ".pdf";
			Signatures s = new Signatures();
			s.signPdf(filePath + fileName, filePath + fileNameSigned);
			
			return fileNameSigned;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}