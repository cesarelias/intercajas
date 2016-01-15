package py.edu.uca.intercajas.server.ejb;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import py.edu.uca.intercajas.server.entity.Beneficiario;
import py.edu.uca.intercajas.server.entity.PeriodoAporteDeclarado;
import py.edu.uca.intercajas.server.entity.SolicitudTitular;
import py.edu.uca.intercajas.server.entity.enums.TipoDocumentoIdentidad;
import py.edu.uca.intercajas.shared.domain.BeneficiarioClient;


@Path("/")
@Stateless
public class RestExample   {

	private static final Logger LOG = Logger.getLogger(GestionBeneficiario.class.getName());
	

	@PersistenceContext
	EntityManager em;
	
	@Path("/beneficiarios/beneficiario")
	@POST
	@Produces("application/json")
	public Beneficiario find(Long id) {
		System.out.println("**************************************id :"+id);
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

	@Path("/beneficiarios/findByNombresDocs")
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

	@Path("/beneficiarios/nuevo")
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
	           
	@Path("/beneficiarios/actualizar")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public void actualizarBeneficiario(Beneficiario beneficiario)  {
		em.merge(beneficiario);
		LOG.info("Beneficiario merged");
	}

	
	
	
	
	//esto es solo test, debe estar en gestionSolicitud
	@Path("/beneficiarios/solicitud")
	@POST
	@Consumes("application/json")
	public void nuevoSolicitudTitular(SolicitudTitular solicitudTitular) {
		if (solicitudTitular.getListaPeriodoAporteDeclarados() == null){
				System.out.println("no debe ser nulo los periodosDeAportesDeclarados");
		} else {
			for (PeriodoAporteDeclarado pad : solicitudTitular.getListaPeriodoAporteDeclarados()) {
				System.out.println("pad.getLugar(): " + "pag.get");
				pad.setSolicitud(solicitudTitular);
				em.persist(pad);
			}
		}
		
		em.persist(solicitudTitular);
		LOG.info("Solicitud titular persisted");
		
	}
	
	
/*	
	
	@Path("/beneficiarios")
	@GET
	@Produces("application/json")
	public List<BeneficiarioClient> test() {
		
		return null;
	}

	
	@Path("/beneficiarios/nuevoBeneficiario")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public void insert(BeneficiarioClient beneficiario) throws Exception { 
		System.out.println("******************************");
		System.out.println("nombre: " + beneficiario.getNombre());
		
		throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("Aqui va el mensaje de error!").build());
		
	}
*/
	
}