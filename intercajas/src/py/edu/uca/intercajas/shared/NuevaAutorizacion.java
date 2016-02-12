package py.edu.uca.intercajas.shared;

import py.edu.uca.intercajas.shared.entity.Adjunto;


/*
 * Esta clase usamos para registar una nueva Autorizacion.
 * Restgwt solo recive una clase como parametro REST, por eso es necesario esta clase
 */
public class NuevaAutorizacion {

	Long mensaje_id;
	String observacion;
	Adjunto[] adjuntos;

	public NuevaAutorizacion() {

	}


	public Adjunto[] getAdjuntos() {
		return adjuntos;
	}

	public void setAdjuntos(Adjunto[] adjuntos) {
		this.adjuntos = adjuntos;
	}


	public Long getMensaje_id() {
		return mensaje_id;
	}


	public void setMensaje_id(Long mensaje_id) {
		this.mensaje_id = mensaje_id;
	}


	public String getObservacion() {
		return observacion;
	}


	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}



}
