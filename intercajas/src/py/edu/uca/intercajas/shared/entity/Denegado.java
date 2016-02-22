package py.edu.uca.intercajas.shared.entity;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("denegado")
@Entity
public class Denegado extends Finiquito {

	private static final long serialVersionUID = 1L;

	private Motivo motivo;
	
	public enum Motivo {
		NoReuneEdad, NoReuneAntiguedad, NoReuneEdadAntiguedad, NoEsAfiliado, Otro
	}

	public Motivo getMotivo() {
		return motivo;
	}

	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
	}
	
}
