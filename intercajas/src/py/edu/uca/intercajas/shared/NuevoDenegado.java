package py.edu.uca.intercajas.shared;

import java.util.Date;
import java.util.List;

import py.edu.uca.intercajas.shared.entity.Adjunto;

public class NuevoDenegado {

	Adjunto[] adjuntos;
	String cuerpoMensaje;
	String numeroResolucion;
	Date fechaResolucion;
	Long solicitudBeneficiarioId;
	String movito;

	public NuevoDenegado() {

	}

	public NuevoDenegado(String numeroResolucion, Date fechaResolucion,
			Long solicitudBeneficiarioId, String movito) {
		this.numeroResolucion = numeroResolucion;
		this.fechaResolucion = fechaResolucion;
		this.solicitudBeneficiarioId = solicitudBeneficiarioId;
		this.movito = movito;
	}

	public String getNumeroResolucion() {
		return numeroResolucion;
	}

	public void setNumeroResolucion(String numeroResolucion) {
		this.numeroResolucion = numeroResolucion;
	}

	public Date getFechaResolucion() {
		return fechaResolucion;
	}

	public void setFechaResolucion(Date fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}

	public Long getSolicitudBeneficiarioId() {
		return solicitudBeneficiarioId;
	}

	public void setSolicitudBeneficiarioId(Long solicitudBeneficiarioId) {
		this.solicitudBeneficiarioId = solicitudBeneficiarioId;
	}

	public String getMovito() {
		return movito;
	}

	public void setMovito(String movito) {
		this.movito = movito;
	}

	public Adjunto[] getAdjuntos() {
		return adjuntos;
	}

	public void setAdjuntos(Adjunto[] adjuntos) {
		this.adjuntos = adjuntos;
	}

	public String getCuerpoMensaje() {
		return cuerpoMensaje;
	}

	public void setCuerpoMensaje(String cuerpoMensaje) {
		this.cuerpoMensaje = cuerpoMensaje;
	}

}
