package py.edu.uca.intercajas.server.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Solicitud extends EntityBase {

	private static final long serialVersionUID = 1L;

	private Date fecha;
	private String numero;
	@OneToMany(mappedBy="solicitud")
	private List<PeriodoAporteDeclarado> listaPeriodoAporteDeclarados;
	
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public List<PeriodoAporteDeclarado> getListaPeriodoAporteDeclarados() {
		return listaPeriodoAporteDeclarados;
	}
	public void setListaPeriodoAporteDeclarados(
			List<PeriodoAporteDeclarado> listaPeriodoAporteDeclarados) {
		this.listaPeriodoAporteDeclarados = listaPeriodoAporteDeclarados;
	}
	
}
