package py.edu.uca.intercajas.shared.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.internal.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Solicitud extends EntityBase {

	private static final long serialVersionUID = 1L;
	
	private Date fecha;
	private String numero;
	@NotNull
	private Estado estado;

	@OneToMany(mappedBy="solicitud")
	@JsonIgnore
	private List<TiempoServicioDeclarado> listaTiempoServicioDeclarado = new ArrayList<TiempoServicioDeclarado>();

	@OneToMany(mappedBy="solicitud")
	@JsonIgnore
	private List<Mensaje> mensajes;

	@OneToMany(mappedBy="solicitud")
	@JsonIgnore
	private List<CajaDeclarada>  cajasDeclaradas;
	
	
	public enum Estado {
		// estado inicial al crear la solicitud, enviada, con las
		// documentaciones a todas las cajas intervinientes
		Nuevo,
		// todas las cajas intervinientes reconocieron la antiguedad
		ConAntiguedad,
		// todas las cajas itervinientes finiquitaron el beneficio
		Finiquitado
	}

	public Solicitud () {
		super();
	}
	
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

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<TiempoServicioDeclarado> getListaTiempoServicioDeclarado() {
		return listaTiempoServicioDeclarado;
	}

	public void setListaTiempoServicioDeclarado(
			List<TiempoServicioDeclarado> listaTiempoServicioDeclarado) {
		this.listaTiempoServicioDeclarado = listaTiempoServicioDeclarado;
	}

	public List<Mensaje> getMensajes() {
		return mensajes;
	}

	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}

	public List<CajaDeclarada> getCajasDeclaradas() {
		return cajasDeclaradas;
	}

	public void setCajasDeclaradas(List<CajaDeclarada> cajasDeclaradas) {
		this.cajasDeclaradas = cajasDeclaradas;
	}

}
