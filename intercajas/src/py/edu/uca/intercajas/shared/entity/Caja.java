package py.edu.uca.intercajas.shared.entity;

import javax.persistence.Entity;

@Entity
public class Caja extends EntityBase {
	
	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private String siglas;
	private Integer t_min;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getSiglas() {
		return siglas;
	}
	public void setSiglas(String siglas) {
		this.siglas = siglas;
	}
	public Integer getT_min() {
		return t_min;
	}
	public void setT_min(Integer t_min) {
		this.t_min = t_min;
	}

}
