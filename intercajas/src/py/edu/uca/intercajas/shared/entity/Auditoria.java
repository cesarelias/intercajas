package py.edu.uca.intercajas.shared.entity;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Auditoria extends EntityBase {

	private static final long serialVersionUID = 1L;

	private String operacion;
	private Date fecha;
	private String nombreUsuario;
	private String cajaSiglas;
	
	public String getOperacion() {
		return operacion;
	}
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public String getCajaSiglas() {
		return cajaSiglas;
	}
	public void setCajaSiglas(String cajaSiglas) {
		this.cajaSiglas = cajaSiglas;
	}
	
}
