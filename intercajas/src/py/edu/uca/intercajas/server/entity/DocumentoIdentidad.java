package py.edu.uca.intercajas.server.entity;

import javax.persistence.Embeddable;

import py.edu.uca.intercajas.server.entity.enums.TipoDocumentoIdentidad;

@Embeddable
public class DocumentoIdentidad   {

	private String numeroDocumento;

	private TipoDocumentoIdentidad tipoDocumento;

	public TipoDocumentoIdentidad getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumentoIdentidad tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}


}
