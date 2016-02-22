package py.edu.uca.intercajas.shared.entity;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Caja extends EntityBase {
	
	private static final long serialVersionUID = 1L;
	
	@Size(min=1,max=70) @NotNull 
	private String nombre;
	@Size(min=1, max=15) @NotNull
	private String siglas;
	@Min(value=1) @Max(value=1200) @NotNull
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
