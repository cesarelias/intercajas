package py.edu.uca.intercajas.client.requestfactory;

import java.util.Date;
import java.util.List;

import py.edu.uca.intercajas.server.entity.EntityLocator;
import py.edu.uca.intercajas.server.entity.PeriodoAporteDeclarado;
import py.edu.uca.intercajas.server.entity.SolicitudTitular;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;


@ProxyFor(value=SolicitudTitular.class,locator=EntityLocator.class)
public interface SolicitudTitularProxy extends EntityProxy {

	public Long getId();
	public String getNumero();
	public void setNumero(String numero);
	public Date getFecha();
	public void setFecha(Date fecha);
	public BeneficiarioProxy getBeneficiario();
	public void setBeneficiario(BeneficiarioProxy beneficiario);
	
	public List<PeriodoAporteDeclaradoProxy> getListaPeriodoAporteDeclarados();
	public void setListaPeriodoAporteDeclarados(List<PeriodoAporteDeclaradoProxy> listaPeriodoAporteDeclarados);

}
