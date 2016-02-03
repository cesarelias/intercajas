package py.edu.uca.intercajas.shared.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class SolicitudBeneficiario extends EntityBase  {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	Solicitud solicitud;
	@ManyToOne
	Beneficiario beneficiario;
//	Estado estado;
//	
//	public enum Estado {
//		Nuevo,
//		Concedido,
//		Denegado
//	}
	
	public SolicitudBeneficiario() {
		super();
	}

	public Beneficiario getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(Beneficiario beneficiario) {
		this.beneficiario = beneficiario;
	}


	public Solicitud getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(Solicitud solicitud) {
		this.solicitud = solicitud;
	}

//	public Estado getEstado() {
//		return estado;
//	}
//
//	public void setEstado(Estado estado) {
//		this.estado = estado;
//	}

}
