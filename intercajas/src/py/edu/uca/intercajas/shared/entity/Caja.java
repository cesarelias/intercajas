package py.edu.uca.intercajas.shared.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Caja extends EntityBase {
	
	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private String siglas;
	private Integer t_min;
	
//	@OneToMany(mappedBy="caja")
//	@JsonIgnore
//	private List<TiempoServicioDeclarado> listaTiempoServicioDeclarado;
//	@OneToMany(mappedBy="caja")
//	@JsonIgnore
//	private List<CajaDeclarada> listaCajaDeclaradas;
//	@OneToMany(mappedBy="caja")
//	@JsonIgnore
//	private List<Empleador> listaEmpleador;
//	@OneToMany(mappedBy="destinatario")
//	@JsonIgnore
//	private List<Destino> listaDestinos;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getSiglas() {
		return siglas;
	}
	public void setSiglas(String siglas) {
		this.siglas = siglas;
	}
	public Integer getT_min() {
		return t_min;
	}
	public void setT_min(Integer t_min) {
		this.t_min = t_min;
	}
//	public List<TiempoServicioDeclarado> getListaTiempoServicioDeclarado() {
//		return listaTiempoServicioDeclarado;
//	}
//	public void setListaTiempoServicioDeclarado(
//			List<TiempoServicioDeclarado> listaTiempoServicioDeclarado) {
//		this.listaTiempoServicioDeclarado = listaTiempoServicioDeclarado;
//	}
//	public List<CajaDeclarada> getListaCajaDeclaradas() {
//		return listaCajaDeclaradas;
//	}
//	public void setListaCajaDeclaradas(List<CajaDeclarada> listaCajaDeclaradas) {
//		this.listaCajaDeclaradas = listaCajaDeclaradas;
//	}
//	public List<Empleador> getListaEmpleador() {
//		return listaEmpleador;
//	}
//	public void setListaEmpleador(List<Empleador> listaEmpleador) {
//		this.listaEmpleador = listaEmpleador;
//	}
//	public List<Destino> getListaDestinos() {
//		return listaDestinos;
//	}
//	public void setListaDestinos(List<Destino> listaDestinos) {
//		this.listaDestinos = listaDestinos;
//	}

}
