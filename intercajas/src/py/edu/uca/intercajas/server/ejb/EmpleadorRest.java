package py.edu.uca.intercajas.server.ejb;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import py.edu.uca.intercajas.shared.UserDTO;
import py.edu.uca.intercajas.shared.entity.Empleador;
import py.edu.uca.intercajas.shared.entity.TiempoServicioReconocido;

@Path("/empleador")
@Stateless
public class EmpleadorRest   {

	@EJB
	UserLogin userLogin;

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
	
	@Path("/findEmpleadorByNombre")
	@GET
	@Produces("application/json")
	public List<Empleador> findEmpleadorByNombre(@QueryParam("nombre") String nombre,
												@QueryParam("startRow") int startRow,
												@QueryParam("maxResults") int maxResults) {
		
		if (nombre == null || nombre.length() == 0) {
			nombre = "%";
		} else {
			nombre = '%' + nombre.toUpperCase() + '%';
		}
		
		if (maxResults > 500) {
			maxResults = 500;
		}
		
		return em.createQuery("select e "
				+ "              from Empleador e "
				+ "             where UPPER(e.nombre) like :nombre "
				+ " order by e.nombre asc"
				,Empleador.class)
				.setParameter("nombre", nombre)
				.setFirstResult(startRow)
				.setMaxResults(maxResults)
				.getResultList();
	}

	@Path("/actualizar")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public void actualizar(Empleador empleador, @Context HttpServletRequest req) { 
	
		UserDTO user = userLogin.getValidUser(req.getSession().getId());
        if (user == null) {
        	throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("usuario no valido").build());
        }

        //controlamos que no se pueda modificar el empleador si ya se uso en alguna solicitud;
        if (empleador != null && empleador.getId() != null) {
        	
        	List<TiempoServicioReconocido> lista = 
        	em.createQuery("select t "
        			+ "       from TiempoServicioReconocido t "
        			+ "       where empleador.id = :empleador_id", TiempoServicioReconocido.class)
        	     .setParameter("empleador_id", empleador.getId())
        	     .getResultList();
        	
        	if (lista.size() > 0 ) {
        		throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("No puede modificar un empleador que ya se utilizo en alguna solicitud").build());
        	}
        }
        
        em.merge(empleador);
        empleador.setCaja(user.getCaja());
		
	}

	
}