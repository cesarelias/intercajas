package py.edu.uca.intercajas.client.requestfactory;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

import py.edu.uca.intercajas.server.entity.DocumentoIdentidad;
import py.edu.uca.intercajas.server.entity.enums.TipoDocumentoIdentidad;

@ProxyFor(value=DocumentoIdentidad.class)
public interface DocumentoIdentidadProxy extends ValueProxy  {

	
	public String getNumeroDocumento();
	public void setNumeroDocumento(String numeroDocuemnto);
	public TipoDocumentoIdentidad getTipoDocumento();
	public void setTipoDocumento(TipoDocumentoIdentidad tipoDocumento);

}
