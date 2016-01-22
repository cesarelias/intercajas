package py.edu.uca.intercajas.shared.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.NormalScope;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.sun.istack.internal.NotNull;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Solicitud extends EntityBase {

	private static final long serialVersionUID = 1L;

	private Date fecha;
	private String numero;
	@NotNull
	private Estado estado;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="solicitud")
	private List<TiempoServicioDeclarado> listaTiempoServicioDeclarado = new ArrayList<TiempoServicioDeclarado>();

	public enum Estado {
		Nuevo, //estado inicial al crear la solicitud
		Solicitado, //solicitud enviada, con las documentaciones a todas las cajas intervinientes
		ConAntiguedad, //todas las cajas intervinientes informacon la antiguedad
		Finiquitado, //todas las cajas intervinientes finiquitaron la solicitud
		Anulado //Solo se puede anular una solicitus con estado Nuevo, una vez enviada, se dede finiquitar indefectiblemente.
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

}
