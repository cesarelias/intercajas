package py.edu.uca.intercajas.shared;

import py.edu.uca.intercajas.shared.entity.SolicitudBeneficiario;

public class ConsultaEstadoSolicitudBeneficiario {

	SolicitudBeneficiario solicitudBeneficiario;
	Estado estado;
	boolean autorizado;
	
	public ConsultaEstadoSolicitudBeneficiario() {
		super();
	}

	public boolean isAutorizado() {
		return autorizado;
	}


	public void setAutorizado(boolean autorizado) {
		this.autorizado = autorizado;
	}

	public SolicitudBeneficiario getSolicitudBeneficiario() {
		return solicitudBeneficiario;
	}


	public void setSolicitudBeneficiario(SolicitudBeneficiario solicitudBeneficiario) {
		this.solicitudBeneficiario = solicitudBeneficiario;
	}

	public Estado getEstado() {
		return estado;
	}


	public void setEstado(Estado estado) {
		this.estado = estado;
	}


	public enum Estado {
		Pendiente, Denegado, Concedido
	}
	
	
	
}
