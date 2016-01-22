package py.edu.uca.intercajas.server.ejb;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import py.edu.uca.intercajas.shared.entity.TiempoServicioDeclarado;
import py.edu.uca.intercajas.shared.entity.SolicitudTitular;

@Path("/solicitudTitular")
@Stateless
public class SolicitudTitularRest   {

	private static final Logger LOG = Logger.getLogger(SolicitudTitularRest.class.getName());
	

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
	public SolicitudTitular find(@PathParam("id") Long id) {
		return em.find(SolicitudTitular.class, id);
	}
	
	@Path("/findAll")
	@GET
	@Produces("application/json")
	public List<SolicitudTitular> findAll() {
		List<SolicitudTitular> lista = em.createQuery("select b from SolicitudTitular b", SolicitudTitular.class).getResultList();
		return lista;
	}
	
	@Path("/nuevo")
	@POST
	@Consumes("application/json")
	public void nuevo(SolicitudTitular solicitudTitular) {
		if (solicitudTitular.getListaTiempoServicioDeclarado() == null){
				System.out.println("no debe ser nulo los periodosDeAportesDeclarados");
		} else {
			for (TiempoServicioDeclarado pad : solicitudTitular.getListaTiempoServicioDeclarado()) {
				System.out.println("pad.getLugar(): " + "pag.get");
				pad.setSolicitud(solicitudTitular);
				em.persist(pad);
			}
		}
		
		em.persist(solicitudTitular);
		LOG.info("Solicitud titular persisted");
		
	}

}