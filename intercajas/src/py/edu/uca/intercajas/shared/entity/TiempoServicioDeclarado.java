package py.edu.uca.intercajas.shared.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(name = "tiempo_servicio_declarado")
@Entity
public class TiempoServicioDeclarado extends EntityBase {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ManyToOne
	private Caja caja;
	@NotNull @Size(max=70)
	private String lugar;
	@NotNull
	private Date inicio;
	@NotNull
	private Date fin;
	@NotNull
	@ManyToOne
	@JsonIgnore //Esto evita la recursividad infinita con JSON (Cuando traigo una Solicitud, me trae sus periodos, y cada periodo la misma solicitud, y asi se repite infinitamente)
	private Solicitud solicitud;
	
	
	public Date getInicio() {
		return inicio;
	}
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	public Date getFin() {
		return fin;
	}
	public void setFin(Date fin) {
		this.fin = fin;
	}

	public Caja getCaja() {
		return caja;
	}
	public void setCaja(Caja caja) {
		this.caja = caja;
	}

	public Solicitud getSolicitud() {
		return solicitud;
	}
	
	public void setSolicitud(Solicitud solicitud) {
		this.solicitud = solicitud;
	}
	public String getLugar() {
		return lugar;
	}
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	
}
