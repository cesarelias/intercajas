package py.edu.uca.intercajas.shared;

import py.edu.uca.intercajas.shared.entity.Adjunto;

public class NuevoDenegado {

	Adjunto[] adjuntos;
	String cuerpoMensaje;
	String numeroResolucion;
	Long solicitudBeneficiarioId;
	String movito;
	Long destino_id;

	public NuevoDenegado() {

	}

	public NuevoDenegado(String numeroResolucion, 
			Long solicitudBeneficiarioId, String movito) {
		this.numeroResolucion = numeroResolucion;
		this.solicitudBeneficiarioId = solicitudBeneficiarioId;
		this.movito = movito;
	}

	public String getNumeroResolucion() {
		return numeroResolucion;
	}

	public void setNumeroResolucion(String numeroResolucion) {
		this.numeroResolucion = numeroResolucion;
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

	public Long getDestino_id() {
		return destino_id;
	}

	public void setDestino_id(Long destino_id) {
		this.destino_id = destino_id;
	}

}
