package py.edu.uca.intercajas.shared.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CajaDeclarada extends EntityBase {

	private static final long serialVersionUID = 1L;

	private Integer txDeclarado;
	private Integer txBruto;
	private Integer txNeto;
	@ManyToOne
	private Solicitud solicitud;
	@ManyToOne
	private Caja caja;
	private Estado estado;
	@OneToMany(mappedBy="cajaDeclarada")
	@JsonIgnore
	private List<TiempoServicioReconocido> listaTiempoServicioReconocido;
	
	
	public enum Estado {
		Nuevo,
		ConAntiguedad,
		Concedido,
		Denegado
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

	public Integer getTxDeclarado() {
		return txDeclarado;
	}

	public void setTxDeclarado(Integer txDeclarado) {
		this.txDeclarado = txDeclarado;
	}

	public Integer getTxBruto() {
		return txBruto;
	}

	public void setTxBruto(Integer txBruto) {
		this.txBruto = txBruto;
	}

	public Integer getTxNeto() {
		return txNeto;
	}

	public void setTxNeto(Integer txNeto) {
		this.txNeto = txNeto;
	}

	public List<TiempoServicioReconocido> getListaTiempoServicioReconocido() {
		return listaTiempoServicioReconocido;
	}

	public void setListaTiempoServicioReconocido(
			List<TiempoServicioReconocido> listaTiempoServicioReconocido) {
		this.listaTiempoServicioReconocido = listaTiempoServicioReconocido;
	}

	
	
}
