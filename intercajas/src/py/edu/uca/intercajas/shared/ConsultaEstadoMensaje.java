package py.edu.uca.intercajas.shared;

import java.util.List;


public class ConsultaEstadoMensaje {

	
	EstadoRTS estadoRTS;
	List<ConsultaEstadoSolicitudBeneficiario> listaConsultaEstadoSolicitudBeneficiario;

	public enum EstadoRTS {
		NO_APLICA, SIN_RTS, CON_RTS_SIN_AUTORIZACION, CON_RTS_AUTORIZADO
	}

	public EstadoRTS getEstadoRTS() {
		return estadoRTS;
	}

	public void setEstadoRTS(EstadoRTS estadoRTS) {
		this.estadoRTS = estadoRTS;
	}

	public List<ConsultaEstadoSolicitudBeneficiario> getListaConsultaEstadoSolicitudBeneficiario() {
		return listaConsultaEstadoSolicitudBeneficiario;
	}

	public void setListaConsultaEstadoSolicitudBeneficiario(
			List<ConsultaEstadoSolicitudBeneficiario> listaConsultaEstadoSolicitudBeneficiario) {
		this.listaConsultaEstadoSolicitudBeneficiario = listaConsultaEstadoSolicitudBeneficiario;
	}

	
	
	
}
