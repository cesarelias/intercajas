package py.edu.uca.intercajas.shared;

import java.util.List;

import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.SolicitudTitular;
import py.edu.uca.intercajas.shared.entity.TiempoServicioDeclarado;


/*
 * Esta clase usamos para registar una nueva Solicuitud.
 * Restgwt solo recive una clase como parametro REST, por eso es necesario esta clase
 */
public class NuevaSolicitudTitular {

	SolicitudTitular solicitudTitular;
	List<TiempoServicioDeclarado> listaTiempoServicioDeclarado;
	Mensaje mensaje;
	List<Adjunto> adjuntos;

	public NuevaSolicitudTitular() {

	}

	public NuevaSolicitudTitular(SolicitudTitular solicitudTitular,
			List<TiempoServicioDeclarado> listaTiempoServicioDeclarado,
			Mensaje mensaje, List<Adjunto> adjuntos) {
		this.solicitudTitular = solicitudTitular;
		this.listaTiempoServicioDeclarado = listaTiempoServicioDeclarado;
		this.mensaje = mensaje;
		this.adjuntos = adjuntos;
	}

	public SolicitudTitular getSolicitudTitular() {
		return solicitudTitular;
	}

	public void setSolicitudTitular(SolicitudTitular solicitudTitular) {
		this.solicitudTitular = solicitudTitular;
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
