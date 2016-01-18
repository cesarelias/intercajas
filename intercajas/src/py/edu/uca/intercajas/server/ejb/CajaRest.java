package py.edu.uca.intercajas.server.ejb;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import py.edu.uca.intercajas.server.entity.Caja;

@Path("/caja")
@Stateless
public class CajaRest   {

	private static final Logger LOG = Logger.getLogger(CajaRest.class.getName());
	

	@PersistenceContext
	EntityManager em;

	@Path("/test")
	@GET
	public String test() {
		System.out.println("rest working");
		return "rest working";
	}
	
	@Path("/{id}")
	@GET
	@Produces("application/json")
	public Caja find(@PathParam("id") Long id) {
		System.out.println("**************************************id :"+id);
		return em.find(Caja.class, id);
	}
	
	@Path("/findAll")
	@GET
	@Produces("application/json")
	public List<Caja> findAll() {
		return em.createQuery("select b from Caja b",Caja.class).getResultList();
	}

}