package py.edu.uca.intercajas.shared.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import com.sun.istack.internal.NotNull;

@Embeddable
public class DocumentoIdentidad   {

	@NotNull @Column(name="numero_documento", length=70) @Size(max=70)
	private String numeroDocumento;
	
	@NotNull @Column(name="tipo_documento")
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
