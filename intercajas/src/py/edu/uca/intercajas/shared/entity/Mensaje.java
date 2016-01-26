package py.edu.uca.intercajas.shared.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Mensaje extends EntityBase {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Caja remitente;
	private String referencia;
	private Asunto asunto;
	@ManyToOne
	@JsonIgnoreProperties({"beneficiario"})
	private Solicitud solicitud;
	private Date fecha;	
	@OneToMany(mappedBy = "mensaje")
	@JsonIgnore
	private List<Adjunto> adjuntos;

	public enum Asunto {
		NuevaSolicitud, ReconocimientoTiempoServicio, TotalizacionTiempoServicio, Finiquito
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

}
