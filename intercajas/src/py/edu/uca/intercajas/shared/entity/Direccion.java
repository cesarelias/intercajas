package py.edu.uca.intercajas.shared.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

@Embeddable
public class Direccion {
	
	@Column(name="calle_principal",  length = 70) @Size(max=70) @Length
	private String callePrincipal;
	@Column(name="numero_casa",  length = 10) @Size(max=10)
	private String numeroCasa;
	@Column(length = 70) @Size(max=70)
	private String barrio;
	@Column(length = 70) @Size(max=70)
	private String ciudad;
	@Column(length = 70) @Size(max=70)
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
