package py.edu.uca.intercajas.shared.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Mensaje extends EntityBase {

	private static final long serialVersionUID = 1L;

//	@NotNull(message="remitente no puede ser nulo")
	//si puede ser nulo, el envio de TOTALIZACION lo envia el sistema, no una caja de jubilacion
	@ManyToOne
	@JoinColumn(name="remitente_id")
	private Caja remitente;
	@NotNull(message="refrencia no puede ser nulo") @Size(max=255)
	private String referencia;
	@NotNull(message="asunto no puede ser nulo")
	private Asunto asunto;
	@NotNull(message="cuerpo no puede ser nulo") @Column(columnDefinition = "text")
	private String cuerpo;
	@NotNull(message="solicitud no puede ser nulo")
	@ManyToOne
	@JsonIgnoreProperties({"beneficiario"})
	private Solicitud solicitud;
	@NotNull(message="estado no puede ser nulo") 
	private Estado estado;
	@Size(max=255)
	private String observacion; //guardamos el motivo en la descripcion al Autorizar o al Anular
	@NotNull(message="fecha no puede ser nulo")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
	@NotNull(message="autorizado no puede ser nulo")
	private boolean autorizado;
	
	@OneToMany(mappedBy = "mensaje")
	@JsonIgnore
	private List<Adjunto> adjuntos;

	@OneToMany(mappedBy = "mensaje")
	@JsonIgnore
	private List<TiempoServicioReconocido> listaTiempoServicioReconocidos;
	
	@OneToMany(mappedBy = "mensaje")
	@JsonIgnore
	private List<Finiquito> listaFiniquitos;
	
	public List<TiempoServicioReconocido> getListaTiempoServicioReconocidos() {
		return listaTiempoServicioReconocidos;
	}

	public void setListaTiempoServicioReconocidos(
			List<TiempoServicioReconocido> listaTiempoServicioReconocidos) {
		this.listaTiempoServicioReconocidos = listaTiempoServicioReconocidos;
	}

	public enum Estado {
		Pendiente, Enviado, Anulado 
	}

	public enum Asunto {
		NuevaSolicitud, ReconocimientoTiempoServicio, TotalizacionTiempoServicio, Concedido, Denegado
	}

	public Caja getRemitente() {
		return remitente;
	}

	public void setRemitente(Caja remitente) {
		this.remitente = remitente;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Solicitud getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(Solicitud solicitud) {
		this.solicitud = solicitud;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public List<Adjunto> getAdjuntos() {
		return adjuntos;
	}

	public void setAdjuntos(List<Adjunto> adjuntos) {
		this.adjuntos = adjuntos;
	}

	public Asunto getAsunto() {
		return asunto;
	}

	public void setAsunto(Asunto asunto) {
		this.asunto = asunto;
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
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

	public List<Finiquito> getListaFiniquitos() {
		return listaFiniquitos;
	}

	public void setListaFiniquitos(List<Finiquito> listaFiniquitos) {
		this.listaFiniquitos = listaFiniquitos;
	}


}
