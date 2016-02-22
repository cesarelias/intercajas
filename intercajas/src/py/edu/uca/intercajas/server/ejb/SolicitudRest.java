package py.edu.uca.intercajas.server.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

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
import py.edu.uca.intercajas.shared.ConsultaEstadoMensaje;
import py.edu.uca.intercajas.shared.ConsultaEstadoSolicitudBeneficiario;
import py.edu.uca.intercajas.shared.NuevaSolicitud;
import py.edu.uca.intercajas.shared.NuevoReconocimientoTiempoServicio;
import py.edu.uca.intercajas.shared.RangoTiempo;
import py.edu.uca.intercajas.shared.UserDTO;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Beneficiario;
import py.edu.uca.intercajas.shared.entity.Caja;
import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
import py.edu.uca.intercajas.shared.entity.Concedido;
import py.edu.uca.intercajas.shared.entity.Denegado;
import py.edu.uca.intercajas.shared.entity.Destino;
import py.edu.uca.intercajas.shared.entity.Finiquito;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.SolicitudBeneficiario;
import py.edu.uca.intercajas.shared.entity.TiempoServicioDeclarado;
import py.edu.uca.intercajas.shared.entity.Mensaje.Asunto;
import py.edu.uca.intercajas.shared.entity.Solicitud;
import py.edu.uca.intercajas.shared.entity.TiempoServicioReconocido;

@Path("/solicitud")
@Stateless
public class SolicitudRest   {

	private static final Logger LOG = Logger.getLogger(SolicitudRest.class.getName());

	@PersistenceContext
	EntityManager em;
	
	@EJB
	UserLogin userLogin;
	
	@Path("/test")
	@GET
	public String test() {
		
		//Caja declarada en cuestion.
		CajaDeclarada cajaDeclarada = em.find(CajaDeclarada.class, 1L);
		
		List<ConsultaEstadoSolicitudBeneficiario> listaConsulta = new ArrayList<ConsultaEstadoSolicitudBeneficiario>();
		ConsultaEstadoSolicitudBeneficiario consulta;
		
//		Solicitud s = cajaDeclarada.getSolicitud();
		
		for (SolicitudBeneficiario sb : cajaDeclarada.getSolicitud().getBeneficiarios()) {
			
			consulta = new ConsultaEstadoSolicitudBeneficiario();
			consulta.setSolicitudBeneficiario(sb);

			for (Finiquito f : sb.getFiniquitos()) {
				if (f.getCajaDeclarada().getId() == cajaDeclarada.getId() && f.getMensaje().getEstado() != Mensaje.Estado.Anulado) {
					consulta.setAutorizado(f.getAutorizado());
					if (f instanceof Concedido) {
						consulta.setEstado(ConsultaEstadoSolicitudBeneficiario.Estado.Concedido);
					} else if (f instanceof Denegado) {
						consulta.setEstado(ConsultaEstadoSolicitudBeneficiario.Estado.Denegado);
					}
				}
			}
			
			if (consulta.getEstado() == null)  {
				consulta.setEstado(ConsultaEstadoSolicitudBeneficiario.Estado.Pendiente);
			}

			listaConsulta.add(consulta);
			
		}
		
		
		///para impresion
		for (ConsultaEstadoSolicitudBeneficiario c : listaConsulta) {
			System.out.println(c.getSolicitudBeneficiario().getBeneficiario().toString() + " estado: " + c.getEstado().toString() );
		}
		
		
		System.out.println("rest working");
		return "rest working";
	}
	
	@Path("/")
	@GET
	@Produces("application/json")
	public Solicitud find(@QueryParam("id") Long id) {
		return em.find(Solicitud.class, id);
	}
	
	@Path("/findAll")
	@GET
	@Produces("application/json")
	public List<Solicitud> findAll() {
		return em.createQuery("select b from Solicitud b", Solicitud.class).getResultList();
	}
	
	@Path("/nuevoReconocimientoTiempoServicio")
	@POST
	@Consumes("application/json")
	public void nuevoReconocimientoTiempoServicio(NuevoReconocimientoTiempoServicio nuevoReconocimientoTiempoServicio, @Context HttpServletRequest req) {
		
		//TODO 
		//falta controlar que no se pueda reconocer mas tiempo del tmin 
		
		UserDTO user = userLogin.getValidUser(req.getSession().getId());
        if (user == null) {
        	throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("usuario no valido").build());
       }

        CajaDeclarada usuarioCajaDeclarada = null;
        try {
	        usuarioCajaDeclarada = em.createQuery("select c"
					+ "                              from CajaDeclarada c"
					+ "                             where solicitud.id = :solicitud_id"
					+ "                               and caja.id      = :caja_id", CajaDeclarada.class)
					                           .setParameter("solicitud_id", nuevoReconocimientoTiempoServicio.getSolicitud().getId())
					                           .setParameter("caja_id", user.getCaja().getId())
					                           .getSingleResult();
        } catch (NoResultException e) {
        	//TODO esto converit a mensaje que le llegue al usuario
        	throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("no corresponde la caja del usuario con el intento de reconocimiento de tiemp de servicios").build());
        }
		
		Solicitud s = em.find(Solicitud.class,nuevoReconocimientoTiempoServicio.getSolicitud().getId());
		if (s==null) { 
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("No existe la solicitud").build());
		}
		
		if (nuevoReconocimientoTiempoServicio.getMensaje() == null) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("Al reconocer tiempo de servicio , debe adjuntarle un unico mensaje").build());
		}
		
		Mensaje m = nuevoReconocimientoTiempoServicio.getMensaje();
		m.setEstado(Mensaje.Estado.Pendiente);
		m.setAsunto(Mensaje.Asunto.ReconocimientoTiempoServicio);
		m.setSolicitud(s);
		m.setFecha(new Date()); //la fecha no se si es al crear o al autorizar
		m.setRemitente(em.find(Caja.class, user.getCaja().getId()));
		
		for (Adjunto a : nuevoReconocimientoTiempoServicio.getAdjuntos()) {
			a.setMensaje(m);
			em.persist(a);
		}
		em.persist(m);

		List<RangoTiempo> rangos = new ArrayList<RangoTiempo>();
		for (TiempoServicioReconocido tsr : nuevoReconocimientoTiempoServicio.getListaTiempoServicioReconocido()) {
			rangos.add(new RangoTiempo(tsr.getInicio(), tsr.getFin()));
			tsr.setCajaDeclarada(usuarioCajaDeclarada); //Esto asegura que los tiempos reconocidos provienen de la caja asociada al usuario!
			tsr.setAutorizado(false);
			tsr.setMensaje(m);
			em.persist(tsr);
		}
		
		//Enviamos a todas las cajas declaradas
		for (CajaDeclarada c : s.getCajasDeclaradas() ) {
			Destino d = new Destino();
			d.setMensaje(m);
			d.setDestinatario(c.getCaja());
			d.setLeido(false);
//			d.setEstado(Destino.Estado.Pendiente);
			em.persist(d);
		}
		
		userLogin.registrarAuditoria(user, "Nuevo Reconocimiento Tiempo Servicio" + s.getExpedienteNumero() + " Cotizante: " + s.getCotizante().toString() + " Reconoce " +CalculoTiempo.leeMeses(CalculoTiempo.txBruto(rangos)));
		
		LOG.info("Solicitud titular persisted");

	}
	
	
	/*
	 * Este metodo, crea la solicitud, el adjunto(s), las cajas declaras (al menos dos), y hace el envio (mensaje + destino(s))
	 */
	@Path("/nuevo")
	@POST
	@Consumes("application/json")
	public void nuevo(NuevaSolicitud nuevaSolicitud, @Context HttpServletRequest req) {
		
		UserDTO user = userLogin.getValidUser(req.getSession().getId());
        if (user == null) {
        	throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("usuario no valido").build());
       }
		
		if (nuevaSolicitud.getMensaje() == null) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("Al insertar una nueva solitidu, debe adjuntarle un unico mensaje").build());
		}
		
		//TODO controlar que existan al menos dos cajas declaradas
		if (nuevaSolicitud.getListaTiempoServicioDeclarado() == null || nuevaSolicitud.getListaTiempoServicioDeclarado().size() <= 0 ) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("Al insertar una nueva solitidu, debe adjuntarle los tiempos de servicios").build());
		}
		
		Solicitud solicitud = nuevaSolicitud.getSolicitud();
		Mensaje m = nuevaSolicitud.getMensaje();
		
		Beneficiario b = em.find(Beneficiario.class, solicitud.getCotizante().getId());
		solicitud.setCotizante(b); //eso es necesario, cuando damos de alta a un beneficiario al crear la solicitud

		m.setEstado(Mensaje.Estado.Pendiente);
		m.setSolicitud(solicitud);
		m.setFecha(new Date());
		m.setRemitente(em.find(Caja.class, user.getCaja().getId())); //La caja remitemte, corresponde a la caja asociada al usuario de inicio de sesion
		for (Adjunto a : nuevaSolicitud.getAdjuntos()) {
			a.setMensaje(m);
			em.persist(a);
		}
		em.persist(m);
		
		for (TiempoServicioDeclarado pad : nuevaSolicitud.getListaTiempoServicioDeclarado()) {
			pad.setSolicitud(solicitud);
			em.persist(pad);
		}
		
		//Creamos las CajasDeclaradas en base a los TiemposServiciosDeclarados
		//Tambien creamos los detinatarios del mensaje
		List<CajaDeclarada> cajaDeclaradas = CalculoTiempo.tx_declarado(nuevaSolicitud.getListaTiempoServicioDeclarado());
		for (CajaDeclarada c : cajaDeclaradas ) {
			c.setSolicitud(solicitud);
			c.setEstado(CajaDeclarada.Estado.Nuevo);
			em.persist(c);
			
			Destino d = new Destino();
			d.setMensaje(m);
			d.setDestinatario(c.getCaja());
			d.setLeido(false);
//			d.setEstado(Destino.Estado.Pendiente);
			em.persist(d);
			
		}
		
		solicitud.setCajaGestora(user.getCaja()); //Donde inicia es caja gestora
		solicitud.setTxFinal(0); //iniciamos con 0 meses
		solicitud.setFecha(new Date()); //fecha del dia
		em.persist(solicitud);

		
		for (SolicitudBeneficiario sb : nuevaSolicitud.getListaSolicitudBeneficiario()) {
			sb.setSolicitud(solicitud);
			sb.setEstado(SolicitudBeneficiario.Estado.Pendiente);
			em.persist(sb);
		}

		userLogin.registrarAuditoria(user, "Nueva Solicitud " + solicitud.getExpedienteNumero() + " Cotizante: " + solicitud.getCotizante().toString());
		
		LOG.info("Solicitud titular persisted");
		
	}

	@Path("/findSolicitudBeneficioBySolicitudId")
	@GET
	@Produces("application/json")
	public List<SolicitudBeneficiario> findSolicitudBeneficioBySolicitudId(@QueryParam("id") Long id) {
		Solicitud s = em.find(Solicitud.class, id);
		if (s==null) {
			return null;
		}
		s.getBeneficiarios().size(); //si no hacemos esto, da lazy exception en con json
		return s.getBeneficiarios();
	}

	private Destino destinoOriginario(Mensaje m, UserDTO user) {

		Mensaje.Asunto asunto = null;
		
		if (m.getAsunto() == Mensaje.Asunto.NuevaSolicitud) {
			return null; //
		} else if (m.getAsunto() == Mensaje.Asunto.ReconocimientoTiempoServicio) {
			asunto = Mensaje.Asunto.NuevaSolicitud;
		} else if (m.getAsunto() == Mensaje.Asunto.Concedido || m.getAsunto() == Mensaje.Asunto.Denegado) {
			asunto = Mensaje.Asunto.TotalizacionTiempoServicio;
		}
		
		try {
			//Recuperamos el Mensaje Solitidud - Destino originario del mensaje a ser anulado.
			Destino d = em.createQuery("select d "
					+ "                   from Mensaje m, Destino d "
					+ "                  where m.id = d.mensaje.id "
					+ "                    and m.solicitud.id = :solicitud_id"
					+ "                    and m.asunto = :asunto "
					+ "                    and d.destinatario.id = :caja", Destino.class)
					.setParameter("asunto", asunto)
					.setParameter("solicitud_id", m.getSolicitud().getId())
					.setParameter("caja", user.getCaja().getId())
					.getSingleResult();
			
			return d;
			
		} catch (NoResultException e) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("No es posible recuperar el destinoOriginario").build());
		}
		
	}
	
	private List<ConsultaEstadoSolicitudBeneficiario> consultaEstadoSolicitudBeneficiario(CajaDeclarada cajaDeclarada) {
		
		List<ConsultaEstadoSolicitudBeneficiario> listaConsulta = new ArrayList<ConsultaEstadoSolicitudBeneficiario>();
		ConsultaEstadoSolicitudBeneficiario consulta;
		
		for (SolicitudBeneficiario sb : cajaDeclarada.getSolicitud().getBeneficiarios()) {
			
			consulta = new ConsultaEstadoSolicitudBeneficiario();
			consulta.setSolicitudBeneficiario(sb);

			for (Finiquito f : sb.getFiniquitos()) {
				if (f.getCajaDeclarada().getId() == cajaDeclarada.getId() && f.getMensaje().getEstado() != Mensaje.Estado.Anulado) {
					consulta.setAutorizado(f.getAutorizado());
					if (f instanceof Concedido) {
						consulta.setEstado(ConsultaEstadoSolicitudBeneficiario.Estado.Concedido);
					} else if (f instanceof Denegado) {
						consulta.setEstado(ConsultaEstadoSolicitudBeneficiario.Estado.Denegado);
					}
				}
			}
			
			if (consulta.getEstado() == null)  {
				consulta.setEstado(ConsultaEstadoSolicitudBeneficiario.Estado.Pendiente);
			}

			listaConsulta.add(consulta);
			
		}

		return listaConsulta;
		
	}
	
	@Path("/consultEstadoMensaje")
	@GET
	@Produces("application/json")
	public ConsultaEstadoMensaje consultEstadoMensaje(@QueryParam("mensaje_id") Long mensaje_id, @Context HttpServletRequest req) {
	
		UserDTO user = userLogin.getValidUser(req.getSession().getId());
        if (user == null) {
        	throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("usuario no valido").build());
       }

        
		Mensaje m = em.find(Mensaje.class, mensaje_id);
		if(m==null){
			throw new IllegalArgumentException("Mensaje id no valido");
		}
		
		if (m.getEstado() == Mensaje.Estado.Anulado) {
			throw new IllegalArgumentException("Mensaje anulado!");
		}
		
        CajaDeclarada usuarioCajaDeclarada = null;
        try {
	        usuarioCajaDeclarada = em.createQuery("select c"
					+ "                              from CajaDeclarada c"
					+ "                             where solicitud.id = :solicitud_id"
					+ "                               and caja.id      = :caja_id", CajaDeclarada.class)
					                           .setParameter("solicitud_id", m.getSolicitud().getId())
					                           .setParameter("caja_id", user.getCaja().getId())
					                           .getSingleResult();
        } catch (NoResultException e) {
        	//TODO esto converit a mensaje que le llegue al usuario
        	throw new IllegalArgumentException("no corresponde la caja del usuario con el intento de reconocimiento de tiemp de servicios");
        }
	
		ConsultaEstadoMensaje consultaEstadoMensaje = new ConsultaEstadoMensaje();
		
		if (m.getAsunto() == Mensaje.Asunto.NuevaSolicitud) {
			
			List<TiempoServicioReconocido> lista =
						em.createQuery("select t"
								+ "       from TiempoServicioReconocido t, Mensaje m"
								+ "      where t.mensaje.id = m.id"
								+ "        and m.estado != :estadoMensaje"
								+ "        and m.solicitud.id = :solicitud_id"
								+ "        and t.cajaDeclarada.id = :usuarioCajaDeclarada_id", TiempoServicioReconocido.class)
								.setParameter("solicitud_id", m.getSolicitud().getId())
								.setParameter("usuarioCajaDeclarada_id", usuarioCajaDeclarada.getId())
								.setParameter("estadoMensaje", Mensaje.Estado.Anulado)
								.getResultList();
			
			consultaEstadoMensaje.setEstadoRTS(ConsultaEstadoMensaje.EstadoRTS.SIN_RTS);
			
			for (TiempoServicioReconocido tsr : lista) {
				if (tsr.getAutorizado()) {
					consultaEstadoMensaje.setEstadoRTS(ConsultaEstadoMensaje.EstadoRTS.CON_RTS_AUTORIZADO);
				} else if (!tsr.getAutorizado()) {
					consultaEstadoMensaje.setEstadoRTS(ConsultaEstadoMensaje.EstadoRTS.CON_RTS_SIN_AUTORIZACION);
				}
			}
			
		} else if (m.getAsunto() == Mensaje.Asunto.ReconocimientoTiempoServicio) {
			//esto es solo informativo
		} else if (m.getAsunto() == Mensaje.Asunto.TotalizacionTiempoServicio) {
			consultaEstadoMensaje.setEstadoRTS(ConsultaEstadoMensaje.EstadoRTS.NO_APLICA);
			//ya hicimos mas arriba. traemos la lista de ConsultaEstadoSolicitudBeneficiario :)
			consultaEstadoMensaje.setListaConsultaEstadoSolicitudBeneficiario(consultaEstadoSolicitudBeneficiario(usuarioCajaDeclarada));
		} else if (m.getAsunto() == Mensaje.Asunto.Concedido) {
			//esto es solo informativo
		} else if (m.getAsunto() == Mensaje.Asunto.Denegado) {
			//esto es solo informativo
		}
		
		return consultaEstadoMensaje;
	}
	
	
} 