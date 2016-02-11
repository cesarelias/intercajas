package py.edu.uca.intercajas.shared.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class SolicitudBeneficiario extends EntityBase  {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	private Solicitud solicitud;
	@ManyToOne
	private Beneficiario beneficiario;
	
	@OneToMany(mappedBy="solicitudBeneficiario")
	@JsonIgnore
	private List<Finiquito> finiquitos;

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

	public List<Finiquito> getFiniquitos() {
		return finiquitos;
	}

	public void setFiniquitos(List<Finiquito> finiquitos) {
		this.finiquitos = finiquitos;
	}

//	public Estado getEstado() {
//		return estado;
//	}
//
//	public void setEstado(Estado estado) {
//		this.estado = estado;
//	}

}
