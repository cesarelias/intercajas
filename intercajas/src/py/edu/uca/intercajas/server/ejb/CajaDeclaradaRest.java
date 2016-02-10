package py.edu.uca.intercajas.server.ejb;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import py.edu.uca.intercajas.shared.UserDTO;
import py.edu.uca.intercajas.shared.entity.Caja;
import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
import py.edu.uca.intercajas.shared.entity.Solicitud;

@Path("/cajaDeclarada")
@Stateless
public class CajaDeclaradaRest   {

	private static final Logger LOG = Logger.getLogger(CajaDeclaradaRest.class.getName());

	@PersistenceContext
	EntityManager em;
	
	@EJB
	UserLogin userLogin;

	@Path("/test")
	@GET
	public String test() {
		System.out.println("rest working");
		
		return "rest working";
	}
	
	@Path("/")
	@GET
	@Produces("application/json")
	public CajaDeclarada find(@QueryParam("id") Long id) {
		System.out.println("**************************************id :"+id);
		return em.find(CajaDeclarada.class, id);
	}
	
	@Path("/findBySolicitudId")
	@GET
	@Produces("application/json")
	public List<CajaDeclarada> findBySolicitudId(@QueryParam("solicitud_id") Long solicitud_id) {
		return em.createQuery("select b "
				+ "              from CajaDeclarada b "
				+ "             where b.solicitud.id = :solicitud_id"
                           , CajaDeclarada.class)
                           .setParameter("solicitud_id", solicitud_id)
                           .getResultList();
	}

	@Path("/findCajaDeclaraadBySolicitudIdAndCurrentUser")
	@GET
	@Produces("application/json")
	public CajaDeclarada findCajaDeclaraadBySolicitudIdAndCurrentUser(@QueryParam("solicitud_id") Long solicitud_id, @Context HttpServletRequest req) {
		
		UserDTO user = userLogin.getValidUser(req.getSession().getId());
        if (user == null) {
        	System.out.println("usuario no valido para el llamado rest!");
       	   return null;
       }
        
        
        try {
        	
        	return        	
        			em.createQuery("select b "
					+ "              from CajaDeclarada b "
					+ "             where b.solicitud.id = :solicitud_id "
					+ "               and b.caja.id = :caja_id"
	                           , CajaDeclarada.class)
	                           .setParameter("solicitud_id", solicitud_id)
	                           .setParameter("caja_id", user.getCaja().getId())
	                           .getSingleResult();
        	
        	
        	
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }

	}
	
	
}