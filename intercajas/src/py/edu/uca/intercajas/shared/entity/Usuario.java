package py.edu.uca.intercajas.shared.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Usuario extends EntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull @Size(max=70) 
	private String correo;
	@NotNull @Size(max=32)
	private String clave;
	@NotNull @Size(max=70)
	private String nombre;
	@NotNull @Size(max=70)
	private String descripcion;
	@NotNull @ManyToOne
	private Caja caja;
	@NotNull
	private Tipo tipo;
	@NotNull
	private boolean activo;

	public enum Tipo {
		Gestor, Superior, Administrador
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Caja getCaja() {
		return caja;
	}

	public void setCaja(Caja caja) {
		this.caja = caja;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

}
