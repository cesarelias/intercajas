package py.edu.uca.intercajas.server.ejb;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import py.edu.uca.intercajas.server.entity.Beneficiario;
import py.edu.uca.intercajas.shared.domain.BeneficiarioClient;


@Path("/")
@Stateless
public class RestExample   {

	@EJB
	GestionBeneficiario gestionBeneficiario;

	@Path("/beneficiarios")
	@GET
	@Produces("application/json")
	public List<BeneficiarioClient> test() {
		List<BeneficiarioClient> lista = new ArrayList<BeneficiarioClient>();
		lista.add(new BeneficiarioClient("1","cesar"));
		lista.add(new BeneficiarioClient("2","graciela"));
		return lista;
		//return gestionBeneficiario.findAll();
	}
	
	@Path("/beneficiarios/beneficiario")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public void insert(BeneficiarioClient beneficiario) { 
		System.out.println("******************************");
		System.out.println("nombre: " + beneficiario.getNombre());
	}
	
}