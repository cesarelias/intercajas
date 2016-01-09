package py.edu.uca.intercajas.client.requestfactory;

import py.edu.uca.intercajas.server.entity.Caja;
import py.edu.uca.intercajas.server.entity.EntityLocator;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value=Caja.class,locator=EntityLocator.class)
public interface CajaProxy  extends EntityProxy {

	public Long getId();
	public String getNombre();
	public void setNombre(String nombre);
	public String getSiglas();
	public void setSiglas(String siglas);
	public Integer getT_min();
	public void setT_min(Integer t_min);

}
