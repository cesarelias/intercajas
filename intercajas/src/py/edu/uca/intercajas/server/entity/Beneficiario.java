package py.edu.uca.intercajas.server.entity;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.Past;

import py.edu.uca.intercajas.server.entity.enums.Sexo;

@Entity
public class Beneficiario extends EntityBase {

	@Transient
	private static final long serialVersionUID = 1L;

//	public enum Sexo {MASCULINO,FEMENINO}
	
	private String nombres;
	private String apellidos;
	@Past
	private Date fechaNacimiento;
	private Sexo sexo;
	@Embedded
	private DocumentoIdentidad documento;
	@Embedded
	private Direccion direccion;
	
	
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public Sexo getSexo() {
		return sexo;
	}
	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}
	public Direccion getDireccion() {
		return direccion;
	}
	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}
	public DocumentoIdentidad getDocumento() {
		return documento;
	}
	public void setDocumento(DocumentoIdentidad documento) {
		this.documento = documento;
	}
	
}
