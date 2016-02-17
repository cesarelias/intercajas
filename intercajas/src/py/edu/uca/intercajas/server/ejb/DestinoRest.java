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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import py.edu.uca.intercajas.shared.UserDTO;
import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
import py.edu.uca.intercajas.shared.entity.Destino;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.Solicitud;
import py.edu.uca.intercajas.shared.entity.Usuario;

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
	
	@Path("/findMisPendientes")
	@GET
	@Produces("application/json")
	public List<Destino> findMisPendientes(@QueryParam("startRow") int startRow,
										@QueryParam("maxResults") int maxResults,
										@Context HttpServletRequest req) {
		
		UserDTO user = userLoign.getValidUser(req.getSession().getId());
        if (user == null) {
        	throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("usuario no valido").build());
       }

		if (maxResults > 500) {
			maxResults = 500;
		}
		
		
		if (user.getTipo() == Usuario.Tipo.Gestor || user.getTipo() == Usuario.Tipo.Administrador) {
			
			return em.createQuery("select c "
					+ "              from Mensaje a, Solicitud b, Destino c"
					+ "             where a.solicitud.id = b.id "
					+ "               and a.id = c.mensaje.id "
					+ "               and a.estado = :estadoMensaje"
					+ "               and c.destinatario.id = :caja_id "
					+ "               and not exists "
					+ "                 (select cd"
					+ "                    from CajaDeclarada cd "
					+ "                   where cd.caja.id = :caja_id"
					+ "                     and cd.estado = :estadoCajaDeclarada"
					+ "                     and b.id = cd.solicitud.id)"
					+ " order by a.fecha desc "
					, Destino.class)
					.setParameter("estadoMensaje", Mensaje.Estado.Enviado)
					.setParameter("estadoCajaDeclarada", CajaDeclarada.Estado.Finiquitado)
					.setFirstResult(startRow)
					.setMaxResults(maxResults)
					.setParameter("caja_id", user.getCaja().getId())
					.getResultList();
			
		} else if (user.getTipo() == Usuario.Tipo.Superior) {
			
			return em.createQuery("select c "
					+ "              from Mensaje a, Solicitud b, Destino c"
					+ "             where a.solicitud.id = b.id "
					+ "               and a.id = c.mensaje.id "					
					+ "               and (b.estado <> :estadoSolicitud) "
					+ "               and a.estado = :estadoMensaje"
					+ "               and a.remitente.id = :caja_id "
					+ "               and c.destinatario.id = :caja_id "
					+ " order by a.fecha desc "
					, Destino.class)
					.setParameter("estadoSolicitud", Solicitud.Estado.Finiquitado)
					.setParameter("estadoMensaje", Mensaje.Estado.Pendiente)
					.setFirstResult(startRow)
					.setMaxResults(maxResults)
					.setParameter("caja_id", user.getCaja().getId())
					.getResultList();
			
		}
		
		return null;
		
	}
	
	@Path("/findMisFiniquitados")
	@GET
	@Produces("application/json")
	public List<Destino> findMisFiniquitados(@QueryParam("startRow") int startRow,
										@QueryParam("maxResults") int maxResults,
										@Context HttpServletRequest req) {
		
		UserDTO user = userLoign.getValidUser(req.getSession().getId());
        if (user == null) {
        	throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("usuario no valido").build());
       }

		if (maxResults > 500) {
			maxResults = 500;
		}
		
		if (user.getTipo() == Usuario.Tipo.Gestor || user.getTipo() == Usuario.Tipo.Administrador || user.getTipo() == Usuario.Tipo.Superior) {
			
			return em.createQuery("select c "
					+ "              from Mensaje a, Solicitud b, Destino c"
					+ "             where a.solicitud.id = b.id "
					+ "               and a.id = c.mensaje.id "
					+ "               and a.estado = :estadoMensaje"
					+ "               and a.remitente.id = :caja_id "
					+ "               and c.destinatario.id = :caja_id"
					+ "               and not exists "
					+ "                 (select cd"
					+ "                    from CajaDeclarada cd "
					+ "                   where cd.caja.id = :caja_id"
					+ "                     and cd.estado <> :estadoCajaDeclarada"
					+ "                     and b.id = cd.solicitud.id)"
					+ " order by a.fecha desc "
					, Destino.class)
					.setParameter("estadoMensaje", Mensaje.Estado.Enviado)
					.setParameter("estadoCajaDeclarada", CajaDeclarada.Estado.Finiquitado)
					.setFirstResult(startRow)
					.setMaxResults(maxResults)
					.setParameter("caja_id", user.getCaja().getId())
					.getResultList();
			
//		} else if (user.getTipo() == Usuario.Tipo.Superior) {
//			
//			return em.createQuery("select c "
//					+ "              from Mensaje a, Solicitud b, Destino c"
//					+ "             where a.solicitud.id = b.id "
//					+ "               and a.id = c.mensaje.id "					
//					+ "               and (b.estado <> :estadoSolicitud) "
//					+ "               and a.estado = :estadoMensaje"
//					+ "               and a.remitente.id = :caja_id "
//					+ "               and c.destinatario.id = :caja_id "
//					+ " order by a.fecha desc "
//					, Destino.class)
//					.setParameter("estadoSolicitud", Solicitud.Estado.Finiquitado)
//					.setParameter("estadoMensaje", Mensaje.Estado.Pendiente)
//					.setFirstResult(startRow)
//					.setMaxResults(maxResults)
//					.setParameter("caja_id", user.getCaja().getId())
//					.getResultList();
			
		}
		
		return null;
		
	}

	
	@Path("/findPendientes")
	@GET
	@Produces("application/json")
	public List<Destino> findPendientes(@QueryParam("startRow") int startRow,
										@QueryParam("maxResults") int maxResults,
										@Context HttpServletRequest req) {
		
		UserDTO user = userLoign.getValidUser(req.getSession().getId());
        if (user == null) {
        	throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("usuario no valido").build());
       }

		if (maxResults > 500) {
			maxResults = 500;
		}
		
		
		if (user.getTipo() == Usuario.Tipo.Gestor || user.getTipo() == Usuario.Tipo.Administrador) {
			
			return em.createQuery("select c "
					+ "              from Mensaje a, Solicitud b, Destino c"
					+ "             where a.solicitud.id = b.id "
					+ "               and a.id = c.mensaje.id "
					+ "               and a.estado = :estadoMensaje"
					+ "               and a.remitente.id = :caja_id "
					+ "               and c.destinatario.id = :caja_id"
					+ "               and exists "
					+ "                 (select cd"
					+ "                    from CajaDeclarada cd "
					+ "                   where cd.estado <> :estadoCajaDeclarada"
					+ "                     and b.id = cd.solicitud.id)"
					+ " order by a.fecha desc "
					, Destino.class)
					.setParameter("estadoMensaje", Mensaje.Estado.Enviado)
					.setParameter("estadoCajaDeclarada", CajaDeclarada.Estado.Finiquitado)
					.setFirstResult(startRow)
					.setMaxResults(maxResults)
					.setParameter("caja_id", user.getCaja().getId())
					.getResultList();
			
//		} else if (user.getTipo() == Usuario.Tipo.Superior) {
//			
//			return em.createQuery("select c "
//					+ "              from Mensaje a, Solicitud b, Destino c"
//					+ "             where a.solicitud.id = b.id "
//					+ "               and a.id = c.mensaje.id "					
//					+ "               and (b.estado <> :estadoSolicitud) "
//					+ "               and a.estado = :estadoMensaje"
//					+ "               and a.remitente.id = :caja_id "
//					+ "               and c.destinatario.id = :caja_id "
//					+ " order by a.fecha desc "
//					, Destino.class)
//					.setParameter("estadoSolicitud", Solicitud.Estado.Finiquitado)
//					.setParameter("estadoMensaje", Mensaje.Estado.Pendiente)
//					.setFirstResult(startRow)
//					.setMaxResults(maxResults)
//					.setParameter("caja_id", user.getCaja().getId())
//					.getResultList();
			
		}
		
		return null;
		
	}
	
	@Path("/findFiniquitados")
	@GET
	@Produces("application/json")
	public List<Destino> findFiniquitados(@QueryParam("startRow") int startRow,
										@QueryParam("maxResults") int maxResults,
										@Context HttpServletRequest req) {
		
		UserDTO user = userLoign.getValidUser(req.getSession().getId());
        if (user == null) {
        	throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("usuario no valido").build());
       }

		if (maxResults > 500) {
			maxResults = 500;
		}
		
		
		if (user.getTipo() == Usuario.Tipo.Gestor || user.getTipo() == Usuario.Tipo.Administrador) {
			
			return em.createQuery("select c "
					+ "              from Mensaje a, Solicitud b, Destino c"
					+ "             where a.solicitud.id = b.id "
					+ "               and a.id = c.mensaje.id "
					+ "               and a.estado = :estadoMensaje"
					+ "               and a.remitente.id = :caja_id "
					+ "               and c.destinatario.id = :caja_id"
					+ "               and not exists "
					+ "                 (select cd"
					+ "                    from CajaDeclarada cd "
					+ "                   where cd.estado <> :estadoCajaDeclarada"
					+ "                     and b.id = cd.solicitud.id)"
					+ " order by a.fecha desc "
					, Destino.class)
					.setParameter("estadoMensaje", Mensaje.Estado.Enviado)
					.setParameter("estadoCajaDeclarada", CajaDeclarada.Estado.Finiquitado)
					.setFirstResult(startRow)
					.setMaxResults(maxResults)
					.setParameter("caja_id", user.getCaja().getId())
					.getResultList();
			
//		} else if (user.getTipo() == Usuario.Tipo.Superior) {
//			
//			return em.createQuery("select c "
//					+ "              from Mensaje a, Solicitud b, Destino c"
//					+ "             where a.solicitud.id = b.id "
//					+ "               and a.id = c.mensaje.id "					
//					+ "               and (b.estado <> :estadoSolicitud) "
//					+ "               and a.estado = :estadoMensaje"
//					+ "               and a.remitente.id = :caja_id "
//					+ "               and c.destinatario.id = :caja_id "
//					+ " order by a.fecha desc "
//					, Destino.class)
//					.setParameter("estadoSolicitud", Solicitud.Estado.Finiquitado)
//					.setParameter("estadoMensaje", Mensaje.Estado.Pendiente)
//					.setFirstResult(startRow)
//					.setMaxResults(maxResults)
//					.setParameter("caja_id", user.getCaja().getId())
//					.getResultList();
			
		}
		
		return null;
		
	}
	
	@Path("/findAnulados")
	@GET
	@Produces("application/json")
	public List<Destino> findAnulados(@QueryParam("startRow") int startRow,
										@QueryParam("maxResults") int maxResults,
										@Context HttpServletRequest req) {
		

		UserDTO user = userLoign.getValidUser(req.getSession().getId());
		if (user == null) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("usuario no valido").build());
		}

		if (maxResults > 500) {
			maxResults = 500;
		}
		
		if (user.getTipo() == Usuario.Tipo.Gestor || user.getTipo() == Usuario.Tipo.Administrador) {
			
			return em.createQuery("select c "
					+ "              from Mensaje a, Solicitud b, Destino c"
					+ "             where a.solicitud.id = b.id "
					+ "               and a.id = c.mensaje.id "
					+ "               and a.remitente.id = :caja_id "
					+ "               and c.destinatario.id = :caja_id"
					+ " order by a.fecha desc "
					, Destino.class)
					.setFirstResult(startRow)
					.setMaxResults(maxResults)
					.setParameter("caja_id", user.getCaja().getId())
					.getResultList();
			
//		} else if (user.getTipo() == Usuario.Tipo.Superior) {
//			
//			return em.createQuery("select c "
//					+ "              from Mensaje a, Solicitud b, Destino c"
//					+ "             where a.solicitud.id = b.id "
//					+ "               and a.id = c.mensaje.id "					
//					+ "               and (b.estado <> :estadoSolicitud) "
//					+ "               and a.estado = :estadoMensaje"
//					+ "               and a.remitente.id = :caja_id "
//					+ "               and c.destinatario.id = :caja_id "
//					+ " order by a.fecha desc "
//					, Destino.class)
//					.setParameter("estadoSolicitud", Solicitud.Estado.Finiquitado)
//					.setParameter("estadoMensaje", Mensaje.Estado.Pendiente)
//					.setFirstResult(startRow)
//					.setMaxResults(maxResults)
//					.setParameter("caja_id", user.getCaja().getId())
//					.getResultList();
			
		}
		
		return null;
		
	}
	
}