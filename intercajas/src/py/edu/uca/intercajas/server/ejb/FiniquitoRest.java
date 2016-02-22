package py.edu.uca.intercajas.server.ejb;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import py.edu.uca.intercajas.shared.CalculoTiempo;
import py.edu.uca.intercajas.shared.NuevoConcedido;
import py.edu.uca.intercajas.shared.NuevoDenegado;
import py.edu.uca.intercajas.shared.NuevoReconocimientoTiempoServicio;
import py.edu.uca.intercajas.shared.UserDTO;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Caja;
import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
import py.edu.uca.intercajas.shared.entity.Denegado;
import py.edu.uca.intercajas.shared.entity.Destino;
import py.edu.uca.intercajas.shared.entity.Finiquito;
import py.edu.uca.intercajas.shared.entity.Concedido;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.SolicitudBeneficiario;

@Path("/finiquito")
@Stateless
public class FiniquitoRest {

	@PersistenceContext
	EntityManager em;

	@EJB
	UserLogin userLogin;

	@GET
	@Produces("application/json")
	public Finiquito find(@QueryParam("id") Long id) {
		return em.find(Finiquito.class, id);
	}

	@Path("/findAll")
	@GET
	@Produces("application/json")
	public List<Finiquito> findAll() {
		return em.createQuery("select b from Finiquito b", Finiquito.class)
				.getResultList();
	}

	@Path("/denegar")
	@POST
	@Consumes("application/json")
	public void denegar(NuevoDenegado nuevoDenegado,
			@Context HttpServletRequest req) {

		UserDTO user = userLogin.getValidUser(req.getSession().getId());
		if (user == null) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("usuario no valido").build());
		}

		
		SolicitudBeneficiario sb = em.find(SolicitudBeneficiario.class, nuevoDenegado.getSolicitudBeneficiarioId());
		
		if (sb == null) {
			//TODO falta mostrar el error
			return;
		}

		
		CajaDeclarada cd = null;
		try {
			cd = em.createQuery("select cd "
					+ "                          from CajaDeclarada cd "
					+ "                         where cd.caja.id = :caja_id "
					+ "                           and cd.solicitud.id = :solicitud_id", CajaDeclarada.class)
					.setParameter("caja_id", user.getCaja().getId())
					.setParameter("solicitud_id", sb.getSolicitud().getId())
					.getSingleResult();
		} catch (NoResultException e) {
			//todo falta mostrar error al usuario, 
			e.printStackTrace();
			return ;
		}
		
		if (cd == null) {
			//esto nunca debria pasar, pero por si akso, avitamos el nullPointerExceptin
			return;
		}
		
		if (cd.getEstado() != CajaDeclarada.Estado.ConAntiguedad) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("La solicitud del beneficiario XXXX debe estar en estado -ConAntiguedad- para poder denegar").build());
		}
		
		//Creamos el mensaje
		Mensaje m = new Mensaje();
		
		m.setEstado(Mensaje.Estado.Pendiente);
		m.setSolicitud(sb.getSolicitud());
		m.setFecha(new Date());
		m.setRemitente(em.find(Caja.class, user.getCaja().getId()));
		m.setReferencia(sb.getSolicitud().getExpedienteNumero() + " - " + sb.getSolicitud().getCotizante().getNombres() + " " + sb.getSolicitud().getCotizante().getApellidos() + " - " + user.getCaja().getSiglas() + " Deniega Beneficio ");
		m.setCuerpo(nuevoDenegado.getCuerpoMensaje());
		m.setAsunto(Mensaje.Asunto.Denegado);
		for (Adjunto a : nuevoDenegado.getAdjuntos()) {
			a.setMensaje(m);
			em.persist(a);
		}
		
		em.persist(m);
		
		//Creamos el denegado
		Denegado d = new Denegado();
		d.setCajaDeclarada(cd);
		d.setSolicitudBeneficiario(sb);
		d.setNumeroResolucion(nuevoDenegado.getNumeroResolucion());
		d.setMotivo(nuevoDenegado.getMotivo());
		d.setAutorizado(false);
		d.setMensaje(m);
		
		em.persist(d);
		
		//Destinamos a todas las cajas declaradas
		for (CajaDeclarada cdd : sb.getSolicitud().getCajasDeclaradas() ) {
			Destino des = new Destino();
			des.setMensaje(m);
			des.setDestinatario(cdd.getCaja());
			des.setLeido(false);
//			des.setEstado(Destino.Estado.Informativo);
			em.persist(des);
		}
		
		userLogin.registrarAuditoria(user, "Deniega beneficio - Solicitud " + m.getSolicitud().getExpedienteNumero() + " Resolucion " + d.getNumeroResolucion() + " Cotizante " + m.getSolicitud().getCotizante().toString() + " Solicitante " + sb.getBeneficiario().toString() + " Motivo " + d.getMotivo());
	}

	
	@Path("/conceder")
	@POST
	@Consumes("application/json")
	public void conceder(NuevoConcedido nuevoConcedido,
			@Context HttpServletRequest req) {

		UserDTO user = userLogin.getValidUser(req.getSession().getId());
		if (user == null) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("usuario no valido").build());
		}

		
		SolicitudBeneficiario sb = em.find(SolicitudBeneficiario.class, nuevoConcedido.getSolicitudBeneficiarioId());
		
		if (sb == null) {
			//TODO falta mostrar el error
			return;
		}
		
		CajaDeclarada cd = null;
		try {
			cd = em.createQuery("select cd "
					+ "                          from CajaDeclarada cd "
					+ "                         where cd.caja.id = :caja_id "
					+ "                           and cd.solicitud.id = :solicitud_id", CajaDeclarada.class)
					.setParameter("caja_id", user.getCaja().getId())
					.setParameter("solicitud_id", sb.getSolicitud().getId())
					.getSingleResult();
		} catch (NoResultException e) {
			//todo falta mostrar error al usuario, 
			e.printStackTrace();
			return ;
		}
		
		if (cd == null) {
			//esto nunca debria pasar, pero por si akso, avitamos el nullPointerExceptin
			return;
		}
		
		if (cd.getEstado() != CajaDeclarada.Estado.ConAntiguedad) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("La solicitud del beneficiario XXXX debe estar en estado -ConAntiguedad- para poder conceder").build());
		}

		//Creamos el mensaje
		Mensaje m = new Mensaje();
		
		m.setEstado(Mensaje.Estado.Pendiente);
		m.setSolicitud(sb.getSolicitud());
		m.setFecha(new Date());
		m.setRemitente(em.find(Caja.class, user.getCaja().getId()));
		m.setReferencia(sb.getSolicitud().getExpedienteNumero() + " - " + sb.getSolicitud().getCotizante().getNombres() + " " + sb.getSolicitud().getCotizante().getApellidos() + " - " + user.getCaja().getSiglas() + " Concede Beneficio ");
		m.setCuerpo(nuevoConcedido.getCuerpoMensaje());
		m.setAsunto(Mensaje.Asunto.Concedido);
		for (Adjunto a : nuevoConcedido.getAdjuntos()) {
			a.setMensaje(m);
			em.persist(a);
		}
		
		em.persist(m);

		
		Concedido c = new Concedido();
		c.setCajaDeclarada(cd);
		c.setSolicitudBeneficiario(sb);
		c.setNumeroResolucion(nuevoConcedido.getNumeroResolucion());
		c.setTx(nuevoConcedido.getTx());
		c.setTmin(nuevoConcedido.getTmin());
		c.setBt(nuevoConcedido.getBt());
		c.setBx(nuevoConcedido.getBx());
		c.setAutorizado(false);
		c.setMensaje(m);
		
		em.persist(c);		
		
		//Destinamos a todas las cajas declaradas
		for (CajaDeclarada cdd : sb.getSolicitud().getCajasDeclaradas() ) {
			Destino d = new Destino();
			d.setMensaje(m);
			d.setDestinatario(cdd.getCaja());
			d.setLeido(false);
//			d.setEstado(Destino.Estado.Informativo);
			em.persist(d);
		}

		//Registramos auditoria
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(0);
		df.setMinimumFractionDigits(0);
		userLogin.registrarAuditoria(user, "Concede beneficio - Solicitud " + m.getSolicitud().getExpedienteNumero() + " Resolucion " + c.getNumeroResolucion() + " Cotizante " + m.getSolicitud().getCotizante().toString() + " Solicitante " + sb.getBeneficiario().toString() + " Monto Final " + df.format(c.getBx()));

	}
	
}