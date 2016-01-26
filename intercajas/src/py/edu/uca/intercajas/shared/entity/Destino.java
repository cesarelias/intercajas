package py.edu.uca.intercajas.shared.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Destino extends EntityBase {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Caja destinatario;
	private Boolean leido;
	@ManyToOne
	private Mensaje mensaje;

	public Caja getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Caja destinatario) {
		this.destinatario = destinatario;
	}

	public Boolean getLeido() {
		return leido;
	}

	public void setLeido(Boolean leido) {
		this.leido = leido;
	}

	public Mensaje getMensaje() {
		return mensaje;
	}

	public void setMensaje(Mensaje mensaje) {
		this.mensaje = mensaje;
	}

}
