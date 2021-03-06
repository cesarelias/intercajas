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

import py.edu.uca.intercajas.shared.entity.Adjunto;

@Path("/adjunto")
@Stateless
public class AdjuntoRest   {

	private static final Logger LOG = Logger.getLogger(AdjuntoRest.class.getName());

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
	public Adjunto find(@PathParam("id") Long id) {
		return em.find(Adjunto.class,id);
	}
	
	@Path("/findByMensajeId")
	@GET
	@Produces("application/json")
	public List<Adjunto> findByMensajeId(@QueryParam("mensaje_id") Long mensaje_id) {
		return em.createQuery("select b"
				+ "              from Adjunto b"
				+ "             where b.mensaje.id = :mensaje_id", Adjunto.class)
				.setParameter("mensaje_id", mensaje_id)
				.getResultList();
	}

}