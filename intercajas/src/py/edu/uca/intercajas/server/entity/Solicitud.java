package py.edu.uca.intercajas.server.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Solicitud extends EntityBase {

	private static final long serialVersionUID = 1L;

	private Date fecha;
	private String numero;
	
	
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
}
