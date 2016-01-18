package py.edu.uca.intercajas.shared.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.NormalScope;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.sun.istack.internal.NotNull;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Solicitud extends EntityBase {

	private static final long serialVersionUID = 1L;

	private Date fecha;
	private String numero;
	@NotNull
	private Estado estado;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="solicitud")
	private List<PeriodoAporteDeclarado> listaPeriodoAporteDeclarados = new ArrayList<PeriodoAporteDeclarado>();

	public enum Estado {
		Nuevo,
		Solicitado,
		AntiguedadParcial,
		AntiguedadCompleta,
		FiniquitoParcial,
		FiniquitoCompleto,
		Anulado
	}
	
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
	
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}

}
