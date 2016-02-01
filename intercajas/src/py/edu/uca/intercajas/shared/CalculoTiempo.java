package py.edu.uca.intercajas.shared;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.ibm.icu.util.Calendar;

import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
import py.edu.uca.intercajas.shared.entity.TiempoServicioDeclarado;
import py.edu.uca.intercajas.shared.entity.TiempoServicioReconocido;

public class CalculoTiempo {


	
	/*
	 * Esta clase debe calcular el tx por caja, en base a los tiempos declarados
	 * por el momento no calcula, solo crea la lista de DISTINCT caja
	 */


	static public int tx_calculado(List<TiempoServicioReconocido> lista) {
		List<RangoTiempo> rangos = new ArrayList<RangoTiempo>();
		
		
		for (TiempoServicioReconocido t : lista) {
			rangos.add(new RangoTiempo(t.getInicio(), t.getFin()));
		}
		
		return calculoMeses(rangos);
		
	}
	
	static public List<CajaDeclarada> tx_declarado(List<TiempoServicioDeclarado> tiempos) {
		
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
		
		//Ahora "retorno" contiene las DIFERNETES cajas.

		//Calculamos el TX por cada caja distinct
		
		List<RangoTiempo> rangos = new ArrayList<RangoTiempo>();
		CajaDeclarada c = null;
		for (int i =0; i< retorno.size(); i++) {
			c = retorno.get(i);
			rangos = new ArrayList<RangoTiempo>();
			for (TiempoServicioDeclarado t : tiempos) {
				if (c.getCaja().getId() == t.getCaja().getId()) {
					rangos.add(new RangoTiempo(t.getInicio(), t.getFin()));
				}
			}
			c.setTx_declarado(calculoMeses(rangos));
						
		}
		
		return retorno;
	}
	
	
	public static int calculoMeses(List<RangoTiempo> rangos) {
		
		

		RangoTiempo r1 = null;
		RangoTiempo r2 = null;
		boolean repetir = true;
		int i=0,ii=0;
		
		while (repetir && rangos.size() >= 2) {
			repetir = false;
			for (i=0; i < rangos.size()-1; i++ ) {
				r1 = rangos.get(i);
				for (ii=i+1; ii < rangos.size(); ii++) {
					r2 = rangos.get(ii);
					if (r1.desde <= r2.hasta && r1.hasta >= r2.desde) {
//						repetir = true;
						rangos.get(i).desde = r1.desde < r2.desde ? r1.desde : r2.desde;
						rangos.get(i).hasta = r1.hasta > r2.hasta ? r1.hasta : r2.hasta;
						rangos.remove(ii);
					}
				}
			}
		}
		
		int meses = 0;
		for (RangoTiempo r : rangos) {
			meses += r.hasta - r.desde + 1;
		}

		return meses;
		

	}
	
	public static String leeMeses(int meses) {
		return meses / 12 + " años " + meses % 12 + " meses";
	}
		


}
