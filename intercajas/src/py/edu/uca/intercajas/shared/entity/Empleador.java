package py.edu.uca.intercajas.shared.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Empleador extends EntityBase {

	private static final long serialVersionUID = 1L;

	
	private String nombre;
	@ManyToOne
	private Caja caja;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Caja getCaja() {
		return caja;
	}
	public void setCaja(Caja caja) {
		this.caja = caja;
	}
	
	
	
}
