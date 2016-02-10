package py.edu.uca.intercajas.shared;

import java.util.List;

import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Destino;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.Solicitud;
import py.edu.uca.intercajas.shared.entity.TiempoServicioReconocido;


/*
 * Esta clase usamos para registar un nuevo Reconocimiento de Tiempo de Servicio.
 * Restgwt solo recive una clase como parametro REST, por eso es necesario esta clase
 */
public class NuevoReconocimientoTiempoServicio  {

	Solicitud solicitud;
	Destino destino;
	List<TiempoServicioReconocido> listaTiempoServicioReconocido;
	Mensaje mensaje;
	Adjunto[] adjuntos;
	
	public NuevoReconocimientoTiempoServicio(Solicitud solicitud,
			Destino destino,
			List<TiempoServicioReconocido> listaTiempoServicioReconocido,
			Mensaje mensaje, Adjunto[] adjuntos) {
		this.solicitud = solicitud;
		this.destino = destino;
		this.listaTiempoServicioReconocido = listaTiempoServicioReconocido;
		this.mensaje = mensaje;
		this.adjuntos = adjuntos;
	}

	public NuevoReconocimientoTiempoServicio() {

	}

	public Solicitud getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(Solicitud solicitud) {
		this.solicitud = solicitud;
	}

	public Destino getDestino() {
		return destino;
	}

	public void setDestino(Destino destino) {
		this.destino = destino;
	}

	public List<TiempoServicioReconocido> getListaTiempoServicioReconocido() {
		return listaTiempoServicioReconocido;
	}

	public void setListaTiempoServicioReconocido(
			List<TiempoServicioReconocido> listaTiempoServicioReconocido) {
		this.listaTiempoServicioReconocido = listaTiempoServicioReconocido;
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

	
}
