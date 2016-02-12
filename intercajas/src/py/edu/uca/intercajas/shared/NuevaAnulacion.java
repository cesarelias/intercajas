package py.edu.uca.intercajas.shared;



/*
 * Esta clase usamos para registar una nueva Anulacion.
 * Restgwt solo recive una clase como parametro REST, por eso es necesario esta clase
 */
public class NuevaAnulacion {

	Long mensaje_id;
	String obvervacion;

	public NuevaAnulacion() {

	}

	public Long getMensaje_id() {
		return mensaje_id;
	}

	public void setMensaje_id(Long mensaje_id) {
		this.mensaje_id = mensaje_id;
	}

	public String getObvervacion() {
		return obvervacion;
	}

	public void setObvervacion(String obvervacion) {
		this.obvervacion = obvervacion;
	}




}
