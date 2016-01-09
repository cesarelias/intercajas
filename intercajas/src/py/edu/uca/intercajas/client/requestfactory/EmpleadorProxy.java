package py.edu.uca.intercajas.client.requestfactory;

import py.edu.uca.intercajas.server.entity.Caja;
import py.edu.uca.intercajas.server.entity.Empleador;
import py.edu.uca.intercajas.server.entity.EntityLocator;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value=Empleador.class,locator=EntityLocator.class)
public interface EmpleadorProxy  extends EntityProxy {

	public String getNombre();
	public void setNombre(String nombre);
	public String getDescripcion();
	public void setDescripcion(String descripcion);
	public CajaProxy getCaja();
	public void setCaja(CajaProxy caja);
	
}
