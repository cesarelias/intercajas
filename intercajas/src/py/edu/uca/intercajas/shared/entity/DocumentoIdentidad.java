package py.edu.uca.intercajas.shared.entity;

import javax.persistence.Embeddable;

@Embeddable
public class DocumentoIdentidad   {

	private String numeroDocumento;

	private TipoDocumentoIdentidad tipoDocumento;

	public enum TipoDocumentoIdentidad {
		CEDULA,
		PASAPORTE
	}
	
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
