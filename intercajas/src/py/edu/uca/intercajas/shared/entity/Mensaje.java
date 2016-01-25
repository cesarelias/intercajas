package py.edu.uca.intercajas.shared.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Mensaje extends EntityBase {

	private static final long serialVersionUID = 1L;

	private Caja remitente;
	private String referencia;
	@ManyToOne
	private Solicitud solicitud;
	private Date fecha;
	
	@OneToMany(mappedBy="mensaje")
	private List<Adjunto> adjuntos;
	
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
	
	public enum Asunto {
		NuevaSolicitud,
		ReconocimientoTiempoServicio,
		TotalizacionTiempoServicio,
		Finiquito
	}

	public List<Adjunto> getAdjuntos() {
		return adjuntos;
	}

	public void setAdjuntos(List<Adjunto> adjuntos) {
		this.adjuntos = adjuntos;
	}

}
