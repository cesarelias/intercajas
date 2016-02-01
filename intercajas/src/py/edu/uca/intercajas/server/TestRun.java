package py.edu.uca.intercajas.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ibm.icu.util.Calendar;

public class TestRun {


	
	public static void main(String[] args) {
	
		
		class RangoTiempo {
			
			int desde;
			int hasta;
			public RangoTiempo (Date desde, Date hasta) {
				
				this.desde = (desde.getYear() * 12) + desde.getMonth();
				this.hasta = (desde.getYear() * 12) + desde.getMonth();
			    
			}
		}	

		
		List<RangoTiempo> rangos = new ArrayList<RangoTiempo>();

		rangos.add(new RangoTiempo(new Date(), new Date()));
		
		
		RangoTiempo r1 = null;
		RangoTiempo r2 = null;
		boolean repetir = true;
		int i=0,ii=0;
		
		try {
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
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
