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
import javax.ws.rs.QueryParam;

import py.edu.uca.intercajas.server.entity.Empleador;

@Path("/empleador")
@Stateless
public class EmpleadorRest   {

	private static final Logger LOG = Logger.getLogger(EmpleadorRest.class.getName());
	

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
	public Empleador find(@PathParam("id") Long id) {
		System.out.println("**************************************id :"+id);
		return em.find(Empleador.class, id);
	}
	
	@Path("/findAll")
	@GET
	@Produces("application/json")
	public List<Empleador> findAll() {
		return em.createQuery("select b from Empleador b",Empleador.class).getResultList();
	}

	@Path("findBycaja_id")
	@GET
	@Produces("application/json")
	public List<Empleador> findBycaja(@QueryParam("caja_id") Long caja_id) {
		return em.createQuery("select e from Empleador e where e.caja.id = :caja_id", Empleador.class)
				.setParameter("caja_id", caja_id)
				.getResultList();
	}	
	
}