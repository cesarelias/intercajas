package py.edu.uca.intercajas.shared.entity;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Envio extends EntityBase {

	private static final long serialVersionUID = 1L;

	private Date fecha;
	private Caja origen;
	private Caja destino;
	private String referencia;
	private String cuerpo;
	private Motivo motivo;
	private Solicitud solicitud;
	
	public enum Motivo {
		Solicitud,
		Antiguedad,
		Finiquito,
		Aclaratodia
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Caja getOrigen() {
		return origen;
	}

	public void setOrigen(Caja origen) {
		this.origen = origen;
	}

	public Caja getDestino() {
		return destino;
	}

	public void setDestino(Caja destino) {
		this.destino = destino;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}

	public Motivo getMotivo() {
		return motivo;
	}

	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
	}

	public Solicitud getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(Solicitud solicitud) {
		this.solicitud = solicitud;
	}

}
