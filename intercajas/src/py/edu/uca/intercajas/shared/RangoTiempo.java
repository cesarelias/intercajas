package py.edu.uca.intercajas.shared;

import java.util.Date;


public class RangoTiempo {

	public int desde;
	public int hasta;

	public RangoTiempo(Date desde, Date hasta) {

		this.desde = (desde.getYear() * 12) + desde.getMonth();
		this.hasta = (hasta.getYear() * 12) + hasta.getMonth();

	}

}
