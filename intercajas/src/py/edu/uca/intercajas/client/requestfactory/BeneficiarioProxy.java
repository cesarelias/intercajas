package py.edu.uca.intercajas.client.requestfactory;

import java.util.Date;

import py.edu.uca.intercajas.server.entity.Beneficiario;
import py.edu.uca.intercajas.server.entity.EntityLocator;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value=Beneficiario.class,locator=EntityLocator.class)
public interface BeneficiarioProxy extends EntityProxy {

	public enum Sexo {MASCULINO, FEMENINO}
	
	public String getNombres();
	public void setNombres(String nombres);
	public String getApellidos();
	public void setApellidos(String apellidos);
	public Date getFechaNacimiento();
	public void setFechaNacimiento(Date fechaNacimiento);
//	public Sexo getSexo();
//	public void setSexo(Sexo sexo);
	public DireccionProxy getDireccion();
	public void setDireccion(DireccionProxy direccion);
	public DocumentoIdentidadProxy getDocumento();
	public void setDocumento(DocumentoIdentidadProxy documento);
	
}
