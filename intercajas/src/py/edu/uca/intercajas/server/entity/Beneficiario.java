package py.edu.uca.intercajas.server.entity;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import py.edu.uca.intercajas.server.entity.enums.Sexo;

@Entity
@Table(uniqueConstraints=
	@UniqueConstraint(columnNames = {"numeroDocumento", "tipoDocumento"})) 
public class Beneficiario extends EntityBase {

	@Transient
	private static final long serialVersionUID = 1L;

	@NotNull(message="ingrese un nombre valido")
	@Size(min = 4, message = "El nombre debe contener al menos 4 catacteres")
	private String nombres;
	@NotNull(message="ingrese un apellido valido")
	private String apellidos;
	@NotNull(message="ingrese una fecha de nacimiento valida")
	@Past(message="la fecha de nacimiento debe ser una fecha pasada")
	private Date fechaNacimiento;
	@NotNull(message="ingrese masculino o femenino")
	private Sexo sexo;
	@Embedded

	@NotNull(message="ingrese un documento valido")
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
