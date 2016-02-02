package py.edu.uca.intercajas.shared;

import java.util.List;

public class CalculoTiempo {


	
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
	
	public static String leeMeses(int meses) {
		return meses / 12 + " años " + meses % 12 + " meses";
	}

}
