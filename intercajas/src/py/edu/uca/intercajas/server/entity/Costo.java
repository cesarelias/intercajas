package py.edu.uca.intercajas.server.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Transient;


@Entity
public class Costo extends EntityBase implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombre;
	private String correo;

	@Transient
	private String operacion;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getOperacion() {
		return operacion;
	}
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}
	
		
}
