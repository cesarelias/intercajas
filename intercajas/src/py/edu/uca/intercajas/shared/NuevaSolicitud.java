package py.edu.uca.intercajas.shared;

import java.util.ArrayList;
import java.util.List;

import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Destino;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.Solicitud;
import py.edu.uca.intercajas.shared.entity.SolicitudBeneficiario;
import py.edu.uca.intercajas.shared.entity.TiempoServicioDeclarado;

/*
 * Esta clase usamos para registar una nueva Solicuitud.
 * Restgwt solo recive una clase como parametro REST, por eso es necesario esta clase
 */
public class NuevaSolicitud {

	Solicitud solicitud;
	List<TiempoServicioDeclarado> listaTiempoServicioDeclarado;
	Mensaje mensaje;
	Adjunto[] adjuntos;
	List<SolicitudBeneficiario> listaSolicitudBeneficiario = new ArrayList<SolicitudBeneficiario>();

	public NuevaSolicitud() {

	}

	public NuevaSolicitud(Solicitud solicitud,
			List<TiempoServicioDeclarado> listaTiempoServicioDeclarado,
			Mensaje mensaje, Adjunto[] adjuntos) {
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

	public Adjunto[] getAdjuntos() {
		return adjuntos;
	}

	public void setAdjuntos(Adjunto[] adjuntos) {
		this.adjuntos = adjuntos;
	}

	public List<SolicitudBeneficiario> getListaSolicitudBeneficiario() {
		return listaSolicitudBeneficiario;
	}

	public void setListaSolicitudBeneficiario(
			List<SolicitudBeneficiario> listaSolicitudBeneficiario) {
		this.listaSolicitudBeneficiario = listaSolicitudBeneficiario;
	}

}
