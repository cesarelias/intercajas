package py.edu.uca.intercajas.server.entity;

import javax.persistence.*;

@Embeddable
public class Direccion {
	
	@Column(length = 30) 
	private String callePrincipal;
	@Column(length = 7)
	private String numeroCasa;
	@Column(length = 20)
	private String barrio;
	@Column(length = 30)
	private String ciudad;
	@Column(length = 30)
	private String departamento;
	public String getCallePrincipal() {
		return callePrincipal;
	}
	public void setCallePrincipal(String callePrincipal) {
		this.callePrincipal = callePrincipal;
	}
	public String getNumeroCasa() {
		return numeroCasa;
	}
	public void setNumeroCasa(String numeroCasa) {
		this.numeroCasa = numeroCasa;
	}
	public String getBarrio() {
		return barrio;
	}
	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

}
