package py.edu.uca.intercajas.client.requestfactory;

import py.edu.uca.intercajas.server.entity.Costo;
import py.edu.uca.intercajas.server.entity.EntityLocator;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value=Costo.class,locator=EntityLocator.class)
public interface CostoProxy extends EntityProxy{
	
	public Long getId();
	
	public String getNombre();
	
	public void setNombre(String nombre);
	
	public String getCorreo();

	public void setCorreo(String correo);
	
	public Long getVersion();
	
	public String getOperacion();
	
	public void setOperacion(String operacion);
	
}
