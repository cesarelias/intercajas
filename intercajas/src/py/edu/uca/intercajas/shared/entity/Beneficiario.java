package py.edu.uca.intercajas.shared.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Entity
@Table(uniqueConstraints=
	@UniqueConstraint(columnNames = {"numero_documento", "tipo_documento"})) 
public class Beneficiario extends EntityBase {

	@Transient
	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(min = 2, max=70)
	private String nombres;
	@NotNull @Size(min = 2, max=70)
	private String apellidos;
	@NotNull
	@Past @Column(name="fecha_nacimiento")
	private Date fechaNacimiento;
	@NotNull
	private Sexo sexo;
	@Embedded
	@NotNull
	private DocumentoIdentidad documento;
	@Embedded
	private Direccion direccion;
	
	public enum Sexo {
		MASCULINO,
		FEMENINO
	}
	
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
	
	@Override
	public String toString() {
		return nombres + " " + apellidos;
	}
	
}
