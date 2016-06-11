package py.edu.uca.intercajas.server.ejb;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

import py.edu.uca.intercajas.shared.BandejaParam;
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

	
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	
	
	@Path("/test")
	@GET
	public String test() {
		
		Date d = null;
		System.out.println(em.createQuery("from Mensaje e where (e.fecha = :fecha or cast(:fecha as date) is null))").setParameter("fecha", null).getResultList().size());
		
		
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
	@POST
	@Produces("application/json")
	public List<Destino> findMisPendientes(BandejaParam parametros, @Context HttpServletRequest req) {
	
	
		UserDTO user = userLoign.getValidUser(req.getSession().getId());
        if (user == null) {
        	throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("usuario no valido").build());
       }

        if (parametros.maxResults == null || parametros.maxResults > 500) {
        	parametros.maxResults = 500;
        }

        if (user.getTipo() == Usuario.Tipo.Gestor || user.getTipo() == Usuario.Tipo.Administrador) {

        	if (parametros.fecha_desde != null) System.out.println(parametros.getFecha_desde());
        	if (parametros.fecha_hasta != null) System.out.println(parametros.getFecha_hasta());
        	
			return em.createQuery("select c "
					+ "              from Mensaje a, Solicitud b, Destino c"
					+ "             where a.solicitud.id = b.id "
					+ "               and a.id = c.mensaje.id "
					+ "               and a.estado = :estadoMensaje"
					+ "               and c.destinatario.id = :caja_id "
					+ "               and (a.remitente.id = :remitente_id or :remitente_id is null)"
					+ "               and (b.cotizante.id = :beneficiario_id  or :beneficiario_id is null)"
					+ "               and (cast(a.fecha as date) >= :fecha_desde or cast(:fecha_desde as date) is null)"
					+ "               and (cast(a.fecha as date) <= :fecha_hasta or cast(:fecha_hasta as date) is null)"
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
					.setFirstResult(parametros.startRow)
					.setMaxResults(parametros.maxResults)
					.setParameter("beneficiario_id", parametros.beneficiario_id)
					.setParameter("remitente_id", parametros.remitente_id)
					.setParameter("caja_id", user.getCaja().getId())
					.setParameter("fecha_desde", parametros.fecha_desde)
					.setParameter("fecha_hasta", parametros.fecha_hasta)
					.getResultList();
			
		} else if (user.getTipo() == Usuario.Tipo.Superior) {
			
			System.out.println("entro aqui");
			
			return em.createQuery("select c "
					+ "              from Mensaje a, Solicitud b, Destino c"
					+ "             where a.solicitud.id = b.id "
					+ "               and a.id = c.mensaje.id "					
					+ "               and (b.estado <> :estadoSolicitud) "
					+ "               and a.estado = :estadoMensaje"
					+ "               and a.remitente.id = :caja_id "
					+ "               and c.destinatario.id = :caja_id "
					+ "               and (a.remitente.id = :remitente_id or :remitente_id is null)"
					+ "               and (b.cotizante.id = :beneficiario_id  or :beneficiario_id is null)"
					+ "               and (cast(a.fecha as date) >= :fecha_desde or cast(:fecha_desde as date) is null)"
					+ "               and (cast(a.fecha as date) <= :fecha_hasta or cast(:fecha_hasta as date) is null)"
					+ " order by a.fecha desc "
					, Destino.class)
					.setParameter("estadoSolicitud", Solicitud.Estado.Finiquitado)
					.setParameter("estadoMensaje", Mensaje.Estado.Pendiente)
					.setFirstResult(parametros.startRow)
					.setMaxResults(parametros.maxResults)
					.setParameter("caja_id", user.getCaja().getId())
					.setParameter("beneficiario_id", parametros.beneficiario_id)
					.setParameter("remitente_id", parametros.remitente_id)
					.setParameter("caja_id", user.getCaja().getId())
					.setParameter("fecha_desde", parametros.fecha_desde)
					.setParameter("fecha_hasta", parametros.fecha_hasta)

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