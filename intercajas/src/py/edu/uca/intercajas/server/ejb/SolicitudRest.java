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

import py.edu.uca.intercajas.shared.entity.TiempoServicioDeclarado;
import py.edu.uca.intercajas.shared.entity.Solicitud;
import py.edu.uca.intercajas.shared.entity.SolicitudTitular;

@Path("/solicitud")
@Stateless
public class SolicitudRest   {

	private static final Logger LOG = Logger.getLogger(SolicitudRest.class.getName());

	@PersistenceContext
	EntityManager em;

	@Path("/test")
	@GET
	public String test() {
		System.out.println("rest working");
		
		List<Solicitud> lista = em.createQuery("select b from Solicitud b", Solicitud.class).getResultList();
		for (Solicitud s : lista) {
			for (TiempoServicioDeclarado p : s.getListaTiempoServicioDeclarado()) {
				System.out.println(p.getLugar());
			}
		}
		
		return "rest working";
	}
	
	@Path("/{id}")
	@GET
	@Produces("application/json")
	public Solicitud find(@PathParam("id") Long id) {
		System.out.println("**************************************id :"+id);
		return em.find(Solicitud.class, id);
	}
	
	@Path("/findAll")
	@GET
	@Produces("application/json")
	public List<Solicitud> findAll() {
		return em.createQuery("select b from Solicitud b", Solicitud.class).getResultList();
	}
	
}