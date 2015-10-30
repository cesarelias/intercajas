package py.edu.uca.intercajas.server.entity;

import javax.persistence.Embeddable;

@Embeddable
public class DocumentoIdentidad   {

	private String numeroDocuemnto;

	private TipoDocumentoIdentidad tipoDocumento;

	public String getNumeroDocuemnto() {
		return numeroDocuemnto;
	}

	public void setNumeroDocuemnto(String numeroDocuemnto) {
		this.numeroDocuemnto = numeroDocuemnto;
	}

	public TipoDocumentoIdentidad getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumentoIdentidad tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}


}
