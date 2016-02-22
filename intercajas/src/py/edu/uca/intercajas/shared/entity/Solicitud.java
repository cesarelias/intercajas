package py.edu.uca.intercajas.shared.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Solicitud extends EntityBase {

	private static final long serialVersionUID = 1L;
	
	@NotNull
	private Date fecha;
	@NotNull @Size(max=70) @Column(name="expediente_numero")
	private String expedienteNumero;
	@NotNull
	private Estado estado;
	@NotNull
	@ManyToOne
	private Beneficiario cotizante;
	@NotNull @Column(name="tx_final")
	private Integer txFinal;
	@NotNull
	@ManyToOne
	@JoinColumn(name="caja_gestora_id")
	private Caja cajaGestora;
	
	@OneToMany(mappedBy="solicitud")
	@JsonIgnore
	private List<TiempoServicioDeclarado> listaTiempoServicioDeclarado = new ArrayList<TiempoServicioDeclarado>();

	@OneToMany(mappedBy="solicitud")
	@JsonIgnore
	private List<Mensaje> mensajes;

	@OneToMany(mappedBy="solicitud")
	@JsonIgnore
	private List<CajaDeclarada>  cajasDeclaradas;
	
	@OneToMany(mappedBy="solicitud")
	@JsonIgnore
	private List<SolicitudBeneficiario> beneficiarios;
	
	public enum Estado {
		// estado inicial al crear la solicitud, enviada, con las
		// documentaciones a todas las cajas intervinientes
		Nuevo,
		// todas las cajas intervinientes reconocieron la antiguedad
		ConAntiguedad,
		// todas las cajas itervinientes finiquitaron el beneficio
		Finiquitado
		//No existe el estado Anulado, En todo caso, una vez Aprovado el envio Inicial, deberian denegar todas las cajas, para cerrar el tramite.
//		Anulado //Solo el adminitrador puede anular una solicitud, y puede anularno en cualquier instancia. //TODO falta la opcion de anular solicitud
	}

	public Solicitud () {
		super();
	}
	
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<TiempoServicioDeclarado> getListaTiempoServicioDeclarado() {
		return listaTiempoServicioDeclarado;
	}

	public void setListaTiempoServicioDeclarado(
			List<TiempoServicioDeclarado> listaTiempoServicioDeclarado) {
		this.listaTiempoServicioDeclarado = listaTiempoServicioDeclarado;
	}

	public List<Mensaje> getMensajes() {
		return mensajes;
	}

	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}

	public List<CajaDeclarada> getCajasDeclaradas() {
		return cajasDeclaradas;
	}

	public void setCajasDeclaradas(List<CajaDeclarada> cajasDeclaradas) {
		this.cajasDeclaradas = cajasDeclaradas;
	}

	public Integer getTxFinal() {
		return txFinal;
	}

	public void setTxFinal(Integer txFinal) {
		this.txFinal = txFinal;
	}

	public List<SolicitudBeneficiario> getBeneficiarios() {
		return beneficiarios;
	}

	public void setBeneficiarios(List<SolicitudBeneficiario> beneficiarios) {
		this.beneficiarios = beneficiarios;
	}

	public Beneficiario getCotizante() {
		return cotizante;
	}

	public void setCotizante(Beneficiario cotizante) {
		this.cotizante = cotizante;
	}

	public Caja getCajaGestora() {
		return cajaGestora;
	}

	public void setCajaGestora(Caja cajaGestora) {
		this.cajaGestora = cajaGestora;
	}

	public String getExpedienteNumero() {
		return expedienteNumero;
	}

	public void setExpedienteNumero(String expedienteNumero) {
		this.expedienteNumero = expedienteNumero;
	}

}
