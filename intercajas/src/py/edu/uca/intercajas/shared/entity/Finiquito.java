package py.edu.uca.intercajas.shared.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "@class")
@JsonSubTypes({ @JsonSubTypes.Type(value = Denegado.class, name = "denegado"),
		@JsonSubTypes.Type(value = Concedido.class, name = "concedido") })
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Finiquito extends EntityBase {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ManyToOne @JoinColumn(name="caja_declarada_id")
	private CajaDeclarada cajaDeclarada;
	@NotNull
	@ManyToOne @JoinColumn(name="solicitud_beneficiario_id")
	private SolicitudBeneficiario solicitudBeneficiario;
	@NotNull
	@ManyToOne
	private Mensaje mensaje;
	@NotNull @Size(min=1, max=15) @Column(name="numero_resolucion")
	private String numeroResolucion;
	@NotNull
	private boolean autorizado;

	public SolicitudBeneficiario getSolicitudBeneficiario() {
		return solicitudBeneficiario;
	}

	public void setSolicitudBeneficiario(
			SolicitudBeneficiario solicitudBeneficiario) {
		this.solicitudBeneficiario = solicitudBeneficiario;
	}

	public String getNumeroResolucion() {
		return numeroResolucion;
	}

	public void setNumeroResolucion(String numeroResolucion) {
		this.numeroResolucion = numeroResolucion;
	}

	public CajaDeclarada getCajaDeclarada() {
		return cajaDeclarada;
	}

	public void setCajaDeclarada(CajaDeclarada cajaDeclarada) {
		this.cajaDeclarada = cajaDeclarada;
	}

	public boolean getAutorizado() {
		return autorizado;
	}

	public boolean isAutorizado() {
		return autorizado;
	}

	public void setAutorizado(boolean autorizado) {
		this.autorizado = autorizado;
	}

	public Mensaje getMensaje() {
		return mensaje;
	}

	public void setMensaje(Mensaje mensaje) {
		this.mensaje = mensaje;
	}
}