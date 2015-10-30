package py.edu.uca.intercajas.client.requestfactory;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

import py.edu.uca.intercajas.server.entity.DocumentoIdentidad;

@ProxyFor(value=DocumentoIdentidad.class)
public interface DocumentoIdentidadProxy extends ValueProxy  {

//	public enum TipoDocumentoIdentidad {CEDULA,PASAPORTE,OTROS}
	
	public String getNumeroDocuemnto();
	public void setNumeroDocuemnto(String numeroDocuemnto);
//	public TipoDocumentoIdentidad getTipoDocumento();
//	public void setTipoDocumento(TipoDocumentoIdentidad tipoDocumento);

}
