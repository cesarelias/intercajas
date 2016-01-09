package py.edu.uca.intercajas.client.requestfactory;

import java.util.Date;

import py.edu.uca.intercajas.server.entity.EntityLocator;
import py.edu.uca.intercajas.server.entity.PeriodoAporteDeclarado;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;


@ProxyFor(value=PeriodoAporteDeclarado.class,locator=EntityLocator.class)
public interface PeriodoAporteDeclaradoProxy extends EntityProxy {

	public Date getInicio();
	public void setInicio(Date inicio);
	public Date getFin();
	public void setFin(Date fin);
	public CajaProxy getCaja();
	public void setCaja(CajaProxy caja);
	public String getLugar();
	public void setLugar(String lugar);
	
	public SolicitudProxy getSolicitud();
	public void setSolicitud(SolicitudProxy solicitud);
	
}
