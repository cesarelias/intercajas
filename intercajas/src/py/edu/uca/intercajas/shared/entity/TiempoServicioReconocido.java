package py.edu.uca.intercajas.shared.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "tiempo_servicio_reconocido")
@Entity
public class TiempoServicioReconocido extends EntityBase {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ManyToOne
	private Empleador empleador;
	@NotNull
	private Date inicio;
	@NotNull
	private Date fin;
	@NotNull
	@ManyToOne
	private Mensaje mensaje;
	@NotNull
	@ManyToOne
	@JoinColumn(name="caja_declarada_id")
	private CajaDeclarada cajaDeclarada;
	@NotNull
	private boolean autorizado;

	public Empleador getEmpleador() {
		return empleador;
	}

	public void setEmpleador(Empleador empleador) {
		this.empleador = empleador;
	}

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

	public CajaDeclarada getCajaDeclarada() {
		return cajaDeclarada;
	}

	public void setCajaDeclarada(CajaDeclarada cajaDeclarada) {
		this.cajaDeclarada = cajaDeclarada;
	}
	public boolean getAutorizado() {
		return autorizado;
	}
	
	public boolean isAutorizado() {
		return autorizado;
	}

	public void setAutorizado(boolean autorizado) {
		this.autorizado = autorizado;
	}

	public Mensaje getMensaje() {
		return mensaje;
	}

	public void setMensaje(Mensaje mensaje) {
		this.mensaje = mensaje;
	}

}
