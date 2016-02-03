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

import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.Solicitud;

@Path("/mensaje")
@Stateless
public class MensajeRest   {

	private static final Logger LOG = Logger.getLogger(MensajeRest.class.getName());

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
	public Mensaje find(@PathParam("id") Long id) {
		System.out.println("**************************************id :"+id);
		Mensaje m = em.find(Mensaje.class, id);
//		m.getAdjuntos().size(); //esto soluciona el lazyLoad Exception al producir JSON
//		m.getSolicitud().getListaTiempoServicioDeclarado().size(); //esto soluciona el lazyLoad Exception al producir JSON
		return m;
	}
	
	@Path("/findAll")
	@GET
	@Produces("application/json")
	public List<Mensaje> findAll() {
		List<Mensaje> retorno = em.createQuery("select b from Mensaje b", Mensaje.class).getResultList();
//		for (Mensaje m : retorno) {
//			m.getAdjuntos().size(); //lazy
//		}
		return retorno; 
	}

	@Path("/findAllPending")
	@GET
	@Produces("application/json")
	public List<Mensaje> findByNombresDocs(@QueryParam("startRow") int startRow,
												@QueryParam("maxResults") int maxResults) {
		
//		if (nombresDocs == null || nombresDocs.length() == 0) {
//			nombresDocs = "%";
//		} else {
//			nombresDocs = '%' + nombresDocs.toUpperCase() + '%';
//		}
		
		if (maxResults > 500) {
			maxResults = 500;
		}
		
		
		return  em.createQuery("select a "
				+ "              from Mensaje a, Solicitud b"
				+ "             where a.solicitud = b "
				+ "               and b.estado = :estado"
				+ " order by a.fecha desc "
				,Mensaje.class)
				.setParameter("estado", Solicitud.Estado.Nuevo)
				.setFirstResult(startRow)
				.setMaxResults(maxResults)
				.getResultList();
		
	}
	
	
}