package py.edu.uca.intercajas.server.ejb;

import java.util.Date;
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
import javax.ws.rs.QueryParam;

import py.edu.uca.intercajas.shared.NuevaSolicitudTitular;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Caja;
import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
import py.edu.uca.intercajas.shared.entity.Destino;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.Solicitud;
import py.edu.uca.intercajas.shared.entity.TiempoServicioDeclarado;
import py.edu.uca.intercajas.shared.entity.SolicitudTitular;

@Path("/destino")
@Stateless
public class DestinoRest   {

	private static final Logger LOG = Logger.getLogger(DestinoRest.class.getName());
	

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
										@QueryParam("maxResults") int maxResults) {
		
		if (maxResults > 500) {
			maxResults = 500;
		}
		
		return em.createQuery("select c "
				+ "              from Mensaje a, Solicitud b, Destino c"
				+ "             where a.solicitud = b "
				+ "               and a = c.mensaje "
				+ "               and b.estado = :estado"
				+ " order by a.fecha desc "
				,Destino.class)
				.setParameter("estado", Solicitud.Estado.Nuevo)
				.setFirstResult(startRow)
				.setMaxResults(maxResults)
				.getResultList();
	}
	
}