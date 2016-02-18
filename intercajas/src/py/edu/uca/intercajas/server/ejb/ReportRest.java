package py.edu.uca.intercajas.server.ejb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Path("/report")
@Stateless
public class ReportRest {

	@Path("/test")
	@GET
	@Produces("text/plain")
	public String test(@QueryParam("param") String param) {
		System.out.println("rest working");

		try {
			return print(param);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public Connection getConnection() throws SQLException {

		Connection con = null;
		DataSource ds = null;

		try {
			Context init = new InitialContext();
			Context ctx = (Context) init.lookup("java:jboss/datasources");
			ds = (DataSource) ctx.lookup("IntercajasDS");
			ctx.close();

			con = ds.getConnection();
			return con;
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}

	}

	public String print(String param) throws SQLException {

		String fileName = "rep-" + new Date().getTime() + ".pdf";

		HashMap<String, Object> hm = new HashMap<>();

		hm.put("nombre", param);

		try {
			JasperReport jasperReport = JasperCompileManager
					.compileReport("/tmp/jasper/auditoria.jrxml");
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, hm, getConnection());
			JasperExportManager.exportReportToPdfFile(jasperPrint,
					"/home/cesar/principal/reports/" + fileName);

			return fileName;

		} catch (JRException e) {
			e.printStackTrace();
		}

		return null;
	}
}