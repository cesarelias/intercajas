package py.edu.uca.intercajas.shared.domain;

public class BeneficiarioClient {

	private String id;
	private String nombre;
	
//	@JsonCreator
//	public BeneficiarioClient (@JsonProperty("id") String id, @JsonProperty("nombre") String nombre) {
//		this.id = id;
//		this.nombre = nombre;
//	}

//	public BeneficiarioClient() {
//		
//	}
//	  
//	public BeneficiarioClient (String id, String nombre) {
//		this.id = id;
//		this.nombre = nombre;
//	}
	
	
	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	

}
