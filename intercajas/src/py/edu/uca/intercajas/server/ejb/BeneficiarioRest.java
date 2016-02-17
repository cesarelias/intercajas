package py.edu.uca.intercajas.server.ejb;


import java.util.List;
import java.util.logging.Logger;

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

import org.eclipse.jetty.server.Authentication.User;

import py.edu.uca.intercajas.shared.UserDTO;
import py.edu.uca.intercajas.shared.entity.Beneficiario;
import py.edu.uca.intercajas.shared.entity.DocumentoIdentidad.TipoDocumentoIdentidad;
import py.edu.uca.intercajas.shared.entity.Solicitud;


@Path("/beneficiario")
@Stateless
public class BeneficiarioRest   {

	private static final Logger LOG = Logger.getLogger(BeneficiarioRest.class.getName());
	

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
	public Beneficiario find(@PathParam("id") Long id) {
		return em.find(Beneficiario.class, id);
	}
	
	public Beneficiario findByDocumento(String numeroDocumento, TipoDocumentoIdentidad tipoDocumento) {
		List<Beneficiario> beneficiarios = em.createQuery("select b from Beneficiario b where "
				+ "b.documento.numeroDocumento = :numeroDocumento and b.documento.tipoDocumento = :tipoDocumento",Beneficiario.class)
				.setParameter("numeroDocumento", numeroDocumento)
				.setParameter("tipoDocumento", tipoDocumento)
				.getResultList();
		if (beneficiarios.size() == 0) {
			return null;
		} ;
		return beneficiarios.get(0);
		
	}

	
	public List<Beneficiario> findAll() {
		return em.createQuery("select b from Beneficiario b",Beneficiario.class).getResultList();
	}

	@Path("/findByNombresDocs")
	@GET
	@Produces("application/json")
	public List<Beneficiario> findByNombresDocs(@QueryParam("nombresDocs") String nombresDocs,
												@QueryParam("startRow") int startRow,
												@QueryParam("maxResults") int maxResults) {
		
		if (nombresDocs == null || nombresDocs.length() == 0) {
			nombresDocs = "%";
		} else {
			nombresDocs = '%' + nombresDocs.toUpperCase() + '%';
		}
		
		if (maxResults > 500) {
			maxResults = 500;
		}
		
		return em.createQuery("select b "
				+ "              from Beneficiario b "
				+ "             where UPPER(b.nombres) like :nombresDocs "
				+ "                or UPPER(b.apellidos) like :nombresDocs"
				+ "                or UPPER(b.documento.numeroDocumento) like :nombresDocs "
				+ " order by b.nombres, b.apellidos asc"
				,Beneficiario.class)
				.setParameter("nombresDocs", nombresDocs)
				.setFirstResult(startRow)
				.setMaxResults(maxResults)
				.getResultList();
	}

	@Path("/nuevo")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Long insertarBeneficiario(Beneficiario beneficiario)  {
		System.out.println("PERSIST LLAMADO");
			em.persist(beneficiario);
			LOG.info("Beneficiario persisted");
			try { 
				return beneficiario.getId();
			} catch (Exception e) {
				throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity(e.getMessage()).build());
			}
	}
	           
	@Path("/actualizar")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public void actualizarBeneficiario(Beneficiario beneficiario, @Context HttpServletRequest req) { 
	
		UserDTO user = userLogin.getValidUser(req.getSession().getId());
        if (user == null) {
        	throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("Usuario no valido").build());
        }
		
		
		List<Solicitud> lista = em.createQuery("select s "
				+ "                         from Solicitud s "
				+ "                        where s.cotizante.id = :beneficiario_id", Solicitud.class)
				.setParameter("beneficiario_id", beneficiario.getId())
				.getResultList();
		
		if (lista.size() > 0) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("No puede modificar un beneficiario que exista en alguna solicitud").build());
		}
			
		em.merge(beneficiario);
		LOG.info("Beneficiario merged");
	}

	
}