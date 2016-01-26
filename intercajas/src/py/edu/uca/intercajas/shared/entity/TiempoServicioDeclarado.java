package py.edu.uca.intercajas.shared.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.fusesource.restygwt.rebind.JsonSerializerGenerators;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class TiempoServicioDeclarado extends EntityBase {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Caja caja;
	private String lugar;
	private Date inicio;
	private Date fin;
	@ManyToOne
	@JsonIgnore //Esto evita la recursividad infinita con JSON (Cuando traigo una Solicitud, me trae sus periodos, y cada periodo la misma solicitud, y asi se repite indefinidamente)
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
