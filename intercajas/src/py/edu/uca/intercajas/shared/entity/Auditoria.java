package py.edu.uca.intercajas.shared.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

import com.sun.istack.internal.NotNull;

@Entity
public class Auditoria extends EntityBase {

	private static final long serialVersionUID = 1L;

	@NotNull @Column(columnDefinition = "text") 
	private String operacion;
	@NotNull
	private Date fecha;
	@NotNull @Size(max=70) @Column(name="nombre_usuario")
	private String nombreUsuario;
	@NotNull @Size(max=70) @Column(name="caja_siglas")
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
