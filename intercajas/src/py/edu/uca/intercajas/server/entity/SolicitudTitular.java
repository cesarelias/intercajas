package py.edu.uca.intercajas.server.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class SolicitudTitular extends Solicitud {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	Beneficiario beneficiario;

	public Beneficiario getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(Beneficiario beneficiario) {
		this.beneficiario = beneficiario;
	}

}
