package py.edu.uca.intercajas.shared;

import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Destino;
import py.edu.uca.intercajas.shared.entity.Mensaje;


/*
 * Esta clase usamos para registar una nueva Autorizacion.
 * Restgwt solo recive una clase como parametro REST, por eso es necesario esta clase
 */
public class NuevaAutorizacion {

	Destino destino;
	Adjunto[] adjuntos;

	public NuevaAutorizacion() {

	}

	public Destino getDestino() {
		return destino;
	}

	public void setDestino(Destino destino) {
		this.destino = destino;
	}

	public Adjunto[] getAdjuntos() {
		return adjuntos;
	}

	public void setAdjuntos(Adjunto[] adjuntos) {
		this.adjuntos = adjuntos;
	}

	public NuevaAutorizacion(Mensaje mensaje, Destino destino,
			Adjunto[] adjuntos) {
		this.destino = destino;
		this.adjuntos = adjuntos;
	}


}
