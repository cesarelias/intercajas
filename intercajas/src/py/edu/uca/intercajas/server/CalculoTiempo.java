package py.edu.uca.intercajas.server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import py.edu.uca.intercajas.shared.CajaRango;
import py.edu.uca.intercajas.shared.RangoTiempo;
import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
import py.edu.uca.intercajas.shared.entity.Solicitud;
import py.edu.uca.intercajas.shared.entity.TiempoServicioDeclarado;
import py.edu.uca.intercajas.shared.entity.TiempoServicioReconocido;

public class CalculoTiempo {


	@PersistenceContext
	EntityManager em;
	
	/*
	 * Esta clase debe calcular el tx por caja, en base a los tiempos declarados
	 * por el momento no calcula, solo crea la lista de DISTINCT caja
	 */

	public static Solicitud txNetoFinal(Solicitud s) {
		
		List<CajaDeclarada> cajasDeclaradas = s.getCajasDeclaradas();
		
		Map<Integer, Integer> cs = new HashMap<Integer, Integer>();
		int txFinal = 0;

		// Creamos la Cantidad de Meses Simultaneos en el map CS
		
		List<CajaRango> cajaRangos = new ArrayList<CajaRango>();
		List<RangoTiempo> rangos = null;
		
		//cargamos las Cajas, con sus rangos consolidados
		for (CajaDeclarada cd : cajasDeclaradas ) {
			cajaRangos.add(new CajaRango(cd, consolidarRangosPorcaja(cd)));
		};

		
		
		for (CajaRango cr : cajaRangos) {
			for (RangoTiempo rt : cr.rangos) {
				for (int i = rt.desde; i <= rt.hasta; i++) {
					if (!cs.containsKey(i)) {
						cs.put(i, 1);
						txFinal++; 
					} else {
						cs.put(i, cs.get(i) + 1);
					}
				}
			}
		}

		// Computamos los tiempos por cajas
		final BigDecimal uno = new BigDecimal("1");
		for (CajaRango cr : cajaRangos) {
			BigDecimal meses = new BigDecimal("0");
			for (RangoTiempo rt : cr.rangos) {
				for (int i = rt.desde; i <= rt.hasta; i++) {
					meses = meses.add(uno.divide(new BigDecimal(cs.get(i)), 20,
							BigDecimal.ROUND_HALF_UP));
				}
			}
			cr.meses = meses.intValue();
		}

		for (CajaDeclarada c : cajasDeclaradas) {
			for (CajaRango cr : cajaRangos) {
				if (c.getId() == cr.cajaDeclarada.getId()) {
					c.setTxNeto(cr.meses);
				}
			}
		}
		
		s.setTxFinal(txFinal);
		
		return s;

	}
	
	
	static public int txBrutoPorTSR(List<TiempoServicioReconocido> lista) {
		List<RangoTiempo> rangos = new ArrayList<RangoTiempo>();
		
		
		for (TiempoServicioReconocido t : lista) {
			rangos.add(new RangoTiempo(t.getInicio(), t.getFin()));
		}
		
		return txBruto(rangos);
		
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
				c.setTxBruto(0);
				c.setTxDeclarado(0);
				c.setTxNeto(0);
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
			c.setTxDeclarado(txBruto(rangos));
						
		}
		
		return retorno;
	}
	
	
	//tx calculado en base a lista de RangoTiempo
	public static int txBruto(List<RangoTiempo> rangos) {
		
		RangoTiempo r1 = null;
		RangoTiempo r2 = null;
		boolean seguir=true;
		int i=0,ii=0;
		
		while (seguir) {
			seguir = false;
			for (i=0; i < rangos.size()-1; i++ ) {
				r1 = rangos.get(i);
				for (ii=i+1; ii < rangos.size(); ii++) {
					r2 = rangos.get(ii);
					if (r1.desde <= r2.hasta && r1.hasta >= r2.desde) {
						seguir = true;
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
	
	//ragosBrutos por CajaDeclarada
	public static List<RangoTiempo> consolidarRangosPorcaja(CajaDeclarada cajaDeclarada) {
		
		
		List<RangoTiempo> rangos = new ArrayList<RangoTiempo>();
		for (TiempoServicioReconocido t : cajaDeclarada.getListaTiempoServicioReconocido()) {
			rangos.add(new RangoTiempo(t.getInicio(), t.getFin()));
		}
		
		
		RangoTiempo r1 = null;
		RangoTiempo r2 = null;
		boolean seguir=true;
		int i=0,ii=0;
		
		while (seguir) {
			seguir = false;
			for (i=0; i < rangos.size()-1; i++ ) {
				r1 = rangos.get(i);
				for (ii=i+1; ii < rangos.size(); ii++) {
					r2 = rangos.get(ii);
					if (r1.desde <= r2.hasta && r1.hasta >= r2.desde) {
						seguir = true;
						rangos.get(i).desde = r1.desde < r2.desde ? r1.desde : r2.desde;
						rangos.get(i).hasta = r1.hasta > r2.hasta ? r1.hasta : r2.hasta;
						rangos.remove(ii);
					}
				}
			}
		}
		
		return rangos;
		
	}

	public static String leeMeses(int meses) {
		return meses / 12 + " años " + meses % 12 + " meses";
	}

}
