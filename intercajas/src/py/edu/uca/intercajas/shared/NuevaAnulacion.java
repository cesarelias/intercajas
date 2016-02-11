package py.edu.uca.intercajas.shared;

import py.edu.uca.intercajas.shared.entity.Mensaje;


/*
 * Esta clase usamos para registar una nueva Anulacion.
 * Restgwt solo recive una clase como parametro REST, por eso es necesario esta clase
 */
public class NuevaAnulacion {

	Long destino_id;
	Mensaje mensaje;

	public NuevaAnulacion() {

	}

	public Mensaje getMensaje() {
		return mensaje;
	}

	public void setMensaje(Mensaje mensaje) {
		this.mensaje = mensaje;
	}


	public Long getDestino_id() {
		return destino_id;
	}


	public void setDestino_id(Long destino_id) {
		this.destino_id = destino_id;
	}



}
