package py.edu.uca.intercajas.shared.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(name="solicitud_beneficiario")
@Entity
public class SolicitudBeneficiario extends EntityBase {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ManyToOne
	private Solicitud solicitud;
	@NotNull
	@ManyToOne
	private Beneficiario beneficiario;
	@NotNull		
	private Tipo tipo;
	private Parentesco parentesco;
	@NotNull
	private Estado estado;
	
	@OneToMany(mappedBy = "solicitudBeneficiario")
	@JsonIgnore
	private List<Finiquito> finiquitos;
	
	public enum Estado {
		Pendiente, Finalizado
	}

	public enum Tipo {
		Titular, Derechohabiente
	}

	public enum Parentesco {
		Conyuge, Descendiente, Ascendiente
	}
	
	public Tipo getTipo() {
		return tipo;
	}
	
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
	
	public Parentesco getParentesco() {
		return parentesco;
	}
	
	public void setParentesco(Parentesco parentesco) {
		this.parentesco = parentesco;
	}

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

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	

}
