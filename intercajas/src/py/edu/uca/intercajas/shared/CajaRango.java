package py.edu.uca.intercajas.shared;

import java.util.List;

import py.edu.uca.intercajas.shared.entity.Caja;
import py.edu.uca.intercajas.shared.entity.CajaDeclarada;

public class CajaRango {

	public CajaDeclarada cajaDeclarada;
	public List<RangoTiempo> rangos;
	public int meses;

	public CajaRango(CajaDeclarada cajaDeclarada, List<RangoTiempo> rangos) {
		this.cajaDeclarada = cajaDeclarada;
		this.rangos = rangos;
	}

}