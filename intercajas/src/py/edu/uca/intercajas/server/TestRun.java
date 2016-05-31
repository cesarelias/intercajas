package py.edu.uca.intercajas.server;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.google.gwt.dev.util.collect.HashMap;
import com.ibm.icu.math.BigDecimal;

import py.edu.uca.intercajas.server.pdfSign.Signatures;
import py.edu.uca.intercajas.shared.entity.Caja;

public class TestRun {

	public static void main(String[] args) throws GeneralSecurityException, IOException {

		Security.addProvider(new BouncyCastleProvider());
		Signatures s = new Signatures();
			s.verifySignatures("cesar", "/home/cesar/Descargas/firmas/decreto_firmado_cesar.pdf");
	
	}
	public void txFinal() {
		/*
		 * 
		 * La formula para el calculo de txNeto seria
		 *    sumatoria de 1/cs
		 *    donde cs = cantidad meses simultaneos. 
		 * 
		 * el txNeto se redondea.menos sin decimales
		 * 
		 * el txFinal es simplemente la candidad de meses sin repetirse que existen en los rangos
		 * 
		 * 
		 */
		
		
		//Simulamos al calculo de tx_neto para cada cajaDeclarada, y el txFinal de la solicitud!
		class RangoTiempo {

			int desde;
			int hasta;

			public RangoTiempo(Date desde, Date hasta) {
				this.desde = (desde.getYear() * 12) + desde.getMonth();
				this.hasta = (desde.getYear() * 12) + desde.getMonth();
			}

			public RangoTiempo(int desde, int hasta) {
				this.desde = desde;
				this.hasta = hasta;
			}
		}
		;

		class CajaRango {

			Caja caja;
			List<RangoTiempo> rangos;
			int meses;

			public CajaRango(Caja caja, List<RangoTiempo> rangos) {
				this.caja = caja;
				this.rangos = rangos;
			}

		}
		;

		Map<Integer, Integer> cs = new HashMap<Integer, Integer>();

		List<RangoTiempo> rangosCajaA = new ArrayList<RangoTiempo>();
		rangosCajaA.add(new RangoTiempo(1, 2));
		rangosCajaA.add(new RangoTiempo(4, 7));

		List<RangoTiempo> rangosCajaB = new ArrayList<RangoTiempo>();
		rangosCajaB.add(new RangoTiempo(5, 10));

		List<RangoTiempo> rangosCajaC = new ArrayList<RangoTiempo>();
		rangosCajaC.add(new RangoTiempo(7, 14));

		List<CajaRango> cajasRangos = new ArrayList<CajaRango>();

		Caja cajaA = new Caja();
		cajaA.setSiglas("A");
		Caja cajaB = new Caja();
		cajaB.setSiglas("B");
		Caja cajaC = new Caja();
		cajaC.setSiglas("C");

		cajasRangos.add(new CajaRango(cajaA, rangosCajaA));
		cajasRangos.add(new CajaRango(cajaB, rangosCajaB));
		cajasRangos.add(new CajaRango(cajaC, rangosCajaC));

		int txFinal = 0;

		// Creamos la Cantidad de Meses Simultaneos en el map CS
		for (CajaRango cr : cajasRangos) {
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
		for (CajaRango cr : cajasRangos) {
			BigDecimal meses = new BigDecimal("0");
			for (RangoTiempo rt : cr.rangos) {
				for (int i = rt.desde; i <= rt.hasta; i++) {
					meses = meses.add(uno.divide(new BigDecimal(cs.get(i)), 20,
							BigDecimal.ROUND_HALF_UP));
				}
			}
			cr.meses = meses.intValue();
		}

		for (CajaRango cr : cajasRangos) {
			System.out.println(cr.caja.getSiglas() + " - " + cr.meses);
		}
		
		System.out.println("txFinal: " + txFinal);
		
	}

	public static void meses() {

		class RangoTiempo {

			int desde;
			int hasta;

			public RangoTiempo(Date desde, Date hasta) {
				this.desde = (desde.getYear() * 12) + desde.getMonth();
				this.hasta = (desde.getYear() * 12) + desde.getMonth();
			}

			public RangoTiempo(int desde, int hasta) {
				this.desde = desde;
				this.hasta = hasta;
			}

		}

		List<RangoTiempo> rangos = new ArrayList<RangoTiempo>();

		rangos.add(new RangoTiempo(40, 50));
		rangos.add(new RangoTiempo(10, 20));
		rangos.add(new RangoTiempo(20, 40));
		rangos.add(new RangoTiempo(30, 40));

		RangoTiempo r1 = null;
		RangoTiempo r2 = null;
		boolean repetir = true;
		int i = 0, ii = 0;

		while (repetir) {
			repetir = false;
			for (i = 0; i < rangos.size() - 1; i++) {
				r1 = rangos.get(i);
				for (ii = i + 1; ii < rangos.size(); ii++) {
					r2 = rangos.get(ii);
					if (r1.desde <= r2.hasta && r1.hasta >= r2.desde) {
						repetir = true;
						rangos.get(i).desde = r1.desde < r2.desde ? r1.desde
								: r2.desde;
						rangos.get(i).hasta = r1.hasta > r2.hasta ? r1.hasta
								: r2.hasta;
						rangos.remove(ii);
					}
				}
			}
		}

		int meses = 0;
		for (RangoTiempo r : rangos) {
			meses += r.hasta - r.desde + 1;
			System.out.println("desde: " + r.desde + " hasta: " + r.hasta);
		}

		System.out.println("Total meses: " + meses);

		System.out.println(meses / 12 + " años " + meses % 12 + " meses");

	}
}
