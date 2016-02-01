package py.edu.uca.intercajas.shared.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class CajaDeclarada extends EntityBase {

	private static final long serialVersionUID = 1L;

	private Integer tx_declarado;
	private Integer tx_calculado;
	@ManyToOne
	private Solicitud solicitud;
	@ManyToOne
	private Caja caja;
	private Estado estado;
	
	public enum Estado {
		Nuevo,
		ConAntiguedad,
		Finiquitado
	}

	public Integer getTx_declarado() {
		return tx_declarado;
	}

	public void setTx_declarado(Integer tx_declarado) {
		this.tx_declarado = tx_declarado;
	}

	public Integer getTx_calculado() {
		return tx_calculado;
	}

	public void setTx_calculado(Integer tx_calculado) {
		this.tx_calculado = tx_calculado;
	}

	public Solicitud getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(Solicitud solicitud) {
		this.solicitud = solicitud;
	}

	public Caja getCaja() {
		return caja;
	}

	public void setCaja(Caja caja) {
		this.caja = caja;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	
}
