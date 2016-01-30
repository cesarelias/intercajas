package py.edu.uca.intercajas.server.ejb;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import py.edu.uca.intercajas.shared.UserDTO;
import py.edu.uca.intercajas.shared.entity.Destino;
import py.edu.uca.intercajas.shared.entity.Solicitud;

@Path("/destino")
@Stateless
public class DestinoRest   {


	@PersistenceContext
	EntityManager em;
	
	@EJB
	UserLogin userLoign;

	@Path("/test")
	@GET
	public String test() {
		System.out.println("rest working");
		return "rest working";
	}
	
	@Path("/{id}")
	@GET
	@Produces("application/json")
	public Destino find(@PathParam("id") Long id) {
		return em.find(Destino.class, id);
	}
	
	@Path("/findAll")
	@GET
	@Produces("application/json")
	public List<Destino> findAll() {
		List<Destino> lista = em.createQuery("select b from Destino b", Destino.class).getResultList();
		return lista;
	}
	
	@Path("/findAllPending")
	@GET
	@Produces("application/json")
	public List<Destino> findAllPending(@QueryParam("startRow") int startRow,
										@QueryParam("maxResults") int maxResults,
										@Context HttpServletRequest req) {
		
		UserDTO user = userLoign.getValidUser(req.getSession().getId());
        if (user == null) {
       	   return null;
       }

		if (maxResults > 500) {
			maxResults = 500;
		}
		
		return em.createQuery("select c "
				+ "              from Mensaje a, Solicitud b, Destino c"
				+ "             where a.solicitud = b "
				+ "               and a = c.mensaje "
				+ "               and b.estado = :estado "
				+ "               and c.destinatario.id = :caja_id "
				+ " order by a.fecha desc "
				,Destino.class)
				.setParameter("estado", Solicitud.Estado.Nuevo)
				.setFirstResult(startRow)
				.setMaxResults(maxResults)
				.setParameter("caja_id", user.getCaja().getId())
				.getResultList();
	}
	
}