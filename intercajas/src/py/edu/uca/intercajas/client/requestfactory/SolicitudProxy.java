package py.edu.uca.intercajas.client.requestfactory;

import java.util.Date;

import py.edu.uca.intercajas.server.entity.EntityLocator;
import py.edu.uca.intercajas.server.entity.Solicitud;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value=Solicitud.class,locator=EntityLocator.class)
public interface SolicitudProxy  extends EntityProxy {

	public Date getFecha();
	public void setFecha(Date fecha);
	public String getNumero();
	public void setNumero(String numero);
	
}
