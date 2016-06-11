package py.edu.uca.intercajas.server.jasper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.util.JRVisitorSupport;

public class SubReportVisitor extends JRVisitorSupport {

	private final String jasperPath;

	public SubReportVisitor(String jasperPath) {
		this.jasperPath = jasperPath;
	}
	
	@Override
	public void visitSubreport(JRSubreport subreport) {
	    SubReportInfo subReportInfo = new SubReportInfo(subreport, jasperPath);
	    compile(subReportInfo);
	}

	private void compile(SubReportInfo subReportInfo) {
	    try {
	    	
	    	if (subReportInfo.shouldCompile())
	    		JasperCompileManager.compileReportToFile(subReportInfo.getJRXMLFilepath());
	    	
	    } catch (JRException e) {
	    	throw new IllegalStateException("No es posible compiar el subreporte'" + subReportInfo.getJRXMLFilename() + "'.", e);
	    }
	}
	
}