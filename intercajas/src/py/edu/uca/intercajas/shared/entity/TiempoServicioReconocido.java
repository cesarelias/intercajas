package py.edu.uca.intercajas.shared.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class TiempoServicioReconocido extends EntityBase {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Empleador empleador;
	private Date inicio;
	private Date fin;
	@ManyToOne
	private CajaDeclarada cajaDeclarada;

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

}
