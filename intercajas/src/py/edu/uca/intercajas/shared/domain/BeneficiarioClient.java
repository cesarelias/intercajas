package py.edu.uca.intercajas.shared.domain;

public class BeneficiarioClient {

	private final String id;
	private final String nombre;
	
//	@JsonCreator
//	public BeneficiarioClient (@JsonProperty("id") final String id, @JsonProperty("nombre") final String nombre) {
//		this.id = id;
//		this.nombre = nombre;
//	}

	public BeneficiarioClient (final String id, final String nombre) {
		this.id = id;
		this.nombre = nombre;
	}
	
	
	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}
	

}
