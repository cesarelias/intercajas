package py.edu.uca.intercajas.shared.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.internal.NotNull;

@Entity
public class Adjunto extends EntityBase {

	private static final long serialVersionUID = 1L;

	@NotNull @Size(max=70) @Column(name="nombre_archivo")
	private String nombreArchivo;
	@NotNull @Size(max=70) @Column(name="ruta_archivo")
	private String rutaArchivo;
	@NotNull
	private Tipo tipo;
	
	@NotNull
	@ManyToOne
	@JsonIgnore
	private Mensaje mensaje;
	
	public enum Tipo {
		DocumentoIdentidad, Solicitud, ReconocimientoTiempoServicio, Resolucion, Liquidacion, NotaInterinstitucional, TotalizacionTiempoServicio
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
