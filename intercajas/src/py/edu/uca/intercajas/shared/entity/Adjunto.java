package py.edu.uca.intercajas.shared.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Adjunto extends EntityBase {

	private static final long serialVersionUID = 1L;

	private String nombreArchivo;
	private String rutaArchivo;
	private Tipo tipo;
	
	@ManyToOne
	@JsonIgnore
	private Mensaje mensaje;
	
	public enum Tipo {
		DocumentoIdentidad, Solicitud, ReconocimientoTiempoServicio, Resolucion, Liquidacion, Otro
	}
	
	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public Mensaje getMensaje() {
		return mensaje;
	}

	public void setMensaje(Mensaje mensaje) {
		this.mensaje = mensaje;
	}

	public String getRutaArchivo() {
		return rutaArchivo;
	}

	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
	
}
