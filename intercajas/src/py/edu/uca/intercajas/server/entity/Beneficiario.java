package py.edu.uca.intercajas.server.entity;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import py.edu.uca.intercajas.server.entity.enums.Sexo;

@Entity
@Table(uniqueConstraints=
	@UniqueConstraint(columnNames = {"numeroDocumento", "tipoDocumento"})) 
public class Beneficiario extends EntityBase {

	@Transient
	private static final long serialVersionUID = 1L;

	@Size(min = 4, message = "El nombre debe contener al menos 4 catacteres")
	private String nombres;
	private String apellidos;
	@Past(message="la fecha de nacimiento debe ser una fecha pasada")
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
