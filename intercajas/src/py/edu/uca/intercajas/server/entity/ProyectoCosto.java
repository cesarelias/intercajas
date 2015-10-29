package py.edu.uca.intercajas.server.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class ProyectoCosto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long idProyectoCosto;
	private Long idProyecto;
	private Long idCosto;
	private Double monto;
	private Double cantidad;
	private String motivo;
	private Long version;
	@Transient
	private String operacion;
	
	public Long getIdProyectoCosto() {
		return idProyectoCosto;
	}
	public void setIdProyectoCosto(Long idProyectoCosto) {
		this.idProyectoCosto = idProyectoCosto;
	}
	public Long getIdProyecto() {
		return idProyecto;
	}
	public void setIdProyecto(Long idProyecto) {
		this.idProyecto = idProyecto;
	}
	public Long getIdCosto() {
		return idCosto;
	}
	public void setIdCosto(Long idCosto) {
		this.idCosto = idCosto;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public Double getCantidad() {
		return cantidad;
	}
	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public String getOperacion() {
		return operacion;
	}
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}
	
		
}
