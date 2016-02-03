package py.edu.uca.intercajas.shared.entity;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("denegado")
@Entity
public class Denegado extends Finiquito {

	private static final long serialVersionUID = 1L;

	//Esto mejorarlos con una tabla de motivos, y relacion muchos a muchos, porque asi funciona en java, pero no se puede hacer un Select SQL	
	private Motivo motivo;
	//TODO mejorar el caso de que un solicitante ya este en gozo de algun beneficio, en este caso, no deveria de Totalizarse los Tiempos de periodos
	public enum Motivo {
		NoRuuneEdadMinima,
		NoReuneAntiguedadMinima,
		Otro
		// ConBeneficioVigente, //Eso no deberia de darse nunca, porque ya no
		// deberia de consolidarse el tiempo de aporte
	}

	public Motivo getMotivo() {
		return motivo;
	}

	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
	}
	
}
