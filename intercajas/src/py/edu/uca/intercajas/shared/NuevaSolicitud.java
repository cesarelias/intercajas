package py.edu.uca.intercajas.shared;

import java.util.List;

import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.Solicitud;
import py.edu.uca.intercajas.shared.entity.TiempoServicioDeclarado;


/*
 * Esta clase usamos para registar una nueva Solicuitud.
 * Restgwt solo recive una clase como parametro REST, por eso es necesario esta clase
 */
public class NuevaSolicitud {

	Solicitud solicitud;
	List<TiempoServicioDeclarado> listaTiempoServicioDeclarado;
	Mensaje mensaje;
	List<Adjunto> adjuntos;

	public NuevaSolicitud() {

	}

	public NuevaSolicitud(Solicitud solicitud,
			List<TiempoServicioDeclarado> listaTiempoServicioDeclarado,
			Mensaje mensaje, List<Adjunto> adjuntos) {
		this.solicitud = solicitud;
		this.listaTiempoServicioDeclarado = listaTiempoServicioDeclarado;
		this.mensaje = mensaje;
		this.adjuntos = adjuntos;
	}

	public Solicitud getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(Solicitud solicitud) {
		this.solicitud = solicitud;
	}

	public List<TiempoServicioDeclarado> getListaTiempoServicioDeclarado() {
		return listaTiempoServicioDeclarado;
	}

	public void setListaTiempoServicioDeclarado(
			List<TiempoServicioDeclarado> listaTiempoServicioDeclarado) {
		this.listaTiempoServicioDeclarado = listaTiempoServicioDeclarado;
	}

	public Mensaje getMensaje() {
		return mensaje;
	}

	public void setMensaje(Mensaje mensaje) {
		this.mensaje = mensaje;
	}

	public List<Adjunto> getAdjuntos() {
		return adjuntos;
	}

	public void setAdjuntos(List<Adjunto> adjuntos) {
		this.adjuntos = adjuntos;
	}

}
