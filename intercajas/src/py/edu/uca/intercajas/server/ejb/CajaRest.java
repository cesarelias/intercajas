package py.edu.uca.intercajas.server.ejb;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jws.WebParam;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
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
import py.edu.uca.intercajas.shared.entity.Caja;
import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
import py.edu.uca.intercajas.shared.entity.Empleador;
import py.edu.uca.intercajas.shared.entity.TiempoServicioDeclarado;
import py.edu.uca.intercajas.shared.entity.TiempoServicioReconocido;
import py.edu.uca.intercajas.shared.entity.Usuario;

@Path("/caja")
@Stateless
public class CajaRest {

	private static final Logger LOG = Logger
			.getLogger(CajaRest.class.getName());

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

	@Path("/{id}")
	@GET
	@Produces("application/json")
	public Caja find(@PathParam("id") Long id) {
		System.out.println("**************************************id :" + id);
		return em.find(Caja.class, id);
	}

	@Path("/findAll")
	@GET
	@Produces("application/json")
	public List<Caja> findAll() {
		return em.createQuery("select b from Caja b", Caja.class)
				.getResultList();
	}

	@Path("/findByNombreSigla")
	@GET
	@Produces("application/json")
	public List<Caja> findByNombreSigla(
			@QueryParam("nombreSigla") String nombreSigla,
			@QueryParam("startRow") int startRow,
			@QueryParam("maxResults") int maxResults,
			@Context HttpServletRequest req) {

		if (nombreSigla == null || nombreSigla.length() == 0) {
			nombreSigla = "%";
		} else {
			nombreSigla = '%' + nombreSigla.toUpperCase() + '%';
		}

		return em
				.createQuery(
						"select c from Caja c where nombre like :nombreSigla or siglas like :nombreSigla",
						Caja.class).setParameter("nombreSigla", nombreSigla)
				.getResultList();

	}

	@Path("/actualizar")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public void actualizar(Caja caja, @Context HttpServletRequest req) {

		UserDTO user = userLogin.getValidUser(req.getSession().getId());
        if (user == null) {
        	throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("usuario no valido").build());
        }
		
        Caja c = null;
        if (caja.getId() == null) { //Nueva caja
        	c = new Caja();

        	userLogin.registrarAuditoria(user, "Nueva caja de jubilacion " +
        			" nombre: " +  caja.getNombre() + 
        			" sigla: " +  caja.getSiglas() +
        			" tmin: " +  caja.getT_min());

        } else {
        	c = em.find(Caja.class, caja.getId());
        	if (c==null) {
        		throw new WebApplicationException(Response.status(Status.FORBIDDEN)
        				.entity("No existe la Caja de Jubilacion").build());
        	}
        	
        	userLogin.registrarAuditoria(user, "Cambio datos caja de jubilacion (anterior/nuevo) " +
        			" nombre: " + c.getNombre() + "/" + caja.getNombre() + 
        			" sigla: " + c.getSiglas() + "/" + caja.getSiglas() +
        			" tmin: " + c.getT_min() + "/" + caja.getT_min());
        }
        
		
		
		c.setNombre(caja.getNombre());
		c.setSiglas(caja.getSiglas());
		c.setT_min(caja.getT_min());
		
		em.persist(c);

	}

	@Path("/eliminar")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void eliminar(@WebParam(name = "caja_id") Long caja_id) {

		Caja c = em.find(Caja.class, caja_id);

		if (c == null) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN)
					.entity("No existe la Caja id").build());
		}

		if (em.createQuery(
				"select c from Empleador c where caja.id = :caja_id",
				Empleador.class).setParameter("caja_id", caja_id)
				.setMaxResults(1)
				.getResultList().size()	 > 0 ) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN)
							.entity("No puede eliminar Caja de Jubilacion, exste en Empleadores")
							.build());
		}

		
		if (em.createQuery(
				"select c from Usuario c where caja.id = :caja_id",
				Usuario.class).setParameter("caja_id", caja_id)
				.setMaxResults(1)
				.getResultList().size()	 > 0 ) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN)
							.entity("No puede eliminar Caja de Jubilacion, exste en Usuario")
							.build());
		}

		
		if (em.createQuery(
				"select c from TiempoServicioDeclarado c where caja.id = :caja_id",
				TiempoServicioDeclarado.class).setParameter("caja_id", caja_id)
				.setMaxResults(1)
				.getResultList().size()	 > 0 ) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN)
							.entity("No puede eliminar Caja de Jubilacion, exste en Tiempo Servicio Declarado")
							.build());
		}

		
		if (em.createQuery(
				"select c from CajaDeclarada c where caja.id = :caja_id",
				CajaDeclarada.class).setParameter("caja_id", caja_id)
				.setMaxResults(1)
				.getResultList().size()	 > 0 ) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN)
							.entity("No puede eliminar Caja de Jubilacion, exste en Caja Declarada")
							.build());
		}

		
		em.remove(c);

	}

}