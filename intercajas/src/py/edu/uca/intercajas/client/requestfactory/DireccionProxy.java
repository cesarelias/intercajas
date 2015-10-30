package py.edu.uca.intercajas.client.requestfactory;

import py.edu.uca.intercajas.server.entity.Direccion;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value=Direccion.class)
public interface DireccionProxy extends ValueProxy {
	
	public String getCallePrincipal();
	public void setCallePrincipal(String callePrincipal);
	public String getNumeroCasa();
	public void setNumeroCasa(String numeroCasa);
	public String getBarrio();
	public void setBarrio(String barrio);
	public String getCiudad();
	public void setCiudad(String ciudad);
	public String getDepartamento();
	public void setDepartamento(String departamento);

}
