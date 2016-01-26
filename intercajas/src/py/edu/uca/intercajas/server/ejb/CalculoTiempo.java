package py.edu.uca.intercajas.server.ejb;

import java.util.ArrayList;
import java.util.List;

import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
import py.edu.uca.intercajas.shared.entity.TiempoServicioDeclarado;

public class CalculoTiempo {

	/*
	 * Esta clase debe calcular el tx por caja, en base a los tiempos declarados
	 * por el momento no calcula, solo crea la lista de DISTINCT caja
	 */
	//TODO falta calcular el TX
	static public List<CajaDeclarada> tx_calculado(List<TiempoServicioDeclarado> tiempos) {
		
		List<CajaDeclarada> retorno = new ArrayList<CajaDeclarada>();
		
		//Insertamos las diferentes cajas
		boolean existe = false;
		for (TiempoServicioDeclarado t : tiempos) {
			for (CajaDeclarada c : retorno) {
				if (c.getCaja().getId().equals(t.getCaja().getId())) {
					existe = true;
				}
			}
			if (!existe) {
				CajaDeclarada c = new CajaDeclarada();
				c.setCaja(t.getCaja());
				c.setTx_calculado(0);
				c.setTx_declarado(0);
				c.setSolicitud(t.getSolicitud());
				retorno.add(c);
			}
			existe = false;
		}
		return retorno;
	}

}
