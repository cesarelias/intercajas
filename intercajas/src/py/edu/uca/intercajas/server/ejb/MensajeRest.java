package py.edu.uca.intercajas.server.ejb;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import py.edu.uca.intercajas.server.GwMessage;
import py.edu.uca.intercajas.shared.CalculoTiempo;
import py.edu.uca.intercajas.shared.NuevaAnulacion;
import py.edu.uca.intercajas.shared.NuevaAutorizacion;
import py.edu.uca.intercajas.shared.RangoTiempo;
import py.edu.uca.intercajas.shared.UserDTO;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
import py.edu.uca.intercajas.shared.entity.Concedido;
import py.edu.uca.intercajas.shared.entity.Denegado;
import py.edu.uca.intercajas.shared.entity.Destino;
import py.edu.uca.intercajas.shared.entity.Finiquito;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.Mensaje.Asunto;
import py.edu.uca.intercajas.shared.entity.Solicitud;
import py.edu.uca.intercajas.shared.entity.SolicitudBeneficiario;
import py.edu.uca.intercajas.shared.entity.TiempoServicioDeclarado;
import py.edu.uca.intercajas.shared.entity.TiempoServicioReconocido;
import py.edu.uca.intercajas.shared.entity.Usuario;

@Path("/mensaje")
@Stateless
public class MensajeRest   {

	@PersistenceContext
	EntityManager em;
	
	@EJB
	UserLogin userLogin;

	@EJB
	ReportRest reporteRest;
	
	@EJB
	GwMessage gwMessage;

	@Path("/test")
	@GET
	public String test() {
		System.out.println("rest working");
		
		
		System.out.println(System.getProperty("principalDir").toString());
		
		gwMessage.sendEmail("cesarelias.py@gmail.com", "no-reply", "Asunto prueba", "Mensaje de prueba");
		
//		
//		List<Finiquito> finiquitos = em.createQuery("select f "
//                + "  from Finiquito f, CajaDeclarada cd "
//                + " where f.cajaDeclarada.id = cd.id "
//                + "   and f.autorizado = true "
//                + "   and cd.id = :caja_declarada_id",Finiquito.class)
//                .setParameter("caja_declarada_id", 2L)
//                .getResultList();
//		
//		for (Finiquito f : finiquitos) {
//			System.out.println(f.getNumeroResolucion());
//		}
		
		return "rest working";
		
	}
	
	@Path("/{id}")
	@GET
	@Produces("application/json")
	public Mensaje find(@PathParam("id") Long id) {
		System.out.println("**************************************id :"+id);
		Mensaje m = em.find(Mensaje.class, id);
//		m.getAdjuntos().size(); //esto soluciona el lazyLoad Exception al producir JSON
//		m.getSolicitud().getListaTiempoServicioDeclarado().size(); //esto soluciona el lazyLoad Exception al producir JSON
		return m;
	}
	
	@Path("/findAll")
	@GET
	@Produces("application/json")
	public List<Mensaje> findAll() {
		List<Mensaje> retorno = em.createQuery("select b from Mensaje b", Mensaje.class).getResultList();
		return retorno; 
	}

	@Path("/findAllPending")
	@GET
	@Produces("application/json")
	public List<Mensaje> findByNombresDocs(@QueryParam("startRow") int startRow,
												@QueryParam("maxResults") int maxResults) {
		
		if (maxResults > 500) {
			maxResults = 500;
		}
		
		return  em.createQuery("select a "
				+ "              from Mensaje a, Solicitud b"
				+ "             where a.solicitud = b "
				+ "               and b.estado = :estado"
				+ " order by a.fecha desc "
				,Mensaje.class)
				.setParameter("estado", Solicitud.Estado.Nuevo)
				.setFirstResult(startRow)
				.setMaxResults(maxResults)
				.getResultList();
		
	}
	
	
	@Path("/autorizar")
	@POST
	@Consumes("application/json")
	public void autorizar(NuevaAutorizacion nuevaAutorizacion, @Context HttpServletRequest req) {
		
		UserDTO user = userLogin.getValidUser(req.getSession().getId());
        if (user == null) {
        	throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("usuario no valido").build());
        }
        
        if (user.getTipo() != Usuario.Tipo.Superior ) {
        	throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("usuario no valido para autorizar/enviar mensaje").build());
        }
        
        Mensaje m = em.find(Mensaje.class, nuevaAutorizacion.getMensaje_id());
        if (m==null || m.getEstado() != Mensaje.Estado.Pendiente) {
        	throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("Mensaje no valido para Autorizacion").build());
        }

        //Guardamos el o los adjuntos
		for (Adjunto a : nuevaAutorizacion.getAdjuntos()) {
			a.setMensaje(m);
			em.persist(a);
		}
		
		if (m.getAsunto() == Mensaje.Asunto.NuevaSolicitud) {
			autorizarNuevaSolicitud(m.getSolicitud(), user); 
		} else if (m.getAsunto() == Mensaje.Asunto.ReconocimientoTiempoServicio) {
			autorizarReconocimientoTiempoServicio(m, m.getSolicitud(), user);
		} else if (m.getAsunto() == Mensaje.Asunto.Concedido || m.getAsunto() == Mensaje.Asunto.Denegado) {
			autorizarFiniquito(m, user);
		} 
		
		//Cambiamos el estado del mensaje a Enviado
		m.setObservacion(nuevaAutorizacion.getObservacion());
        m.setEstado(Mensaje.Estado.Enviado);
        m.setAutorizado(true);
        em.persist(m);
        
		
	}


	@Path("/anular")
	@POST
	@Consumes("application/json")
	public void anular(NuevaAnulacion nuevaAnulacion, @Context HttpServletRequest req) { 
	
		System.out.println("llego aqui");
		UserDTO user = userLogin.getValidUser(req.getSession().getId());
        if (user == null) {
        	throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("usuario no valido").build());
        }
        
        if (user.getTipo() != Usuario.Tipo.Superior ) {
        	throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("usuario no valido para autorizar/enviar mensaje").build());
        }
        
        Mensaje m = em.find(Mensaje.class, nuevaAnulacion.getMensaje_id());
        
        if (m==null || m.getEstado() != Mensaje.Estado.Pendiente || m.getRemitente().getId() != user.getCaja().getId()) {
        	throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("Mensaje no valido").build());
        }
        
		//Cambiamos el estado del mensaje a Anulado
        m.setObservacion(nuevaAnulacion.getObvervacion());
        m.setEstado(Mensaje.Estado.Anulado);
        em.persist(m);
        
        userLogin.registrarAuditoria(user, "Anula (desautoriza) envio de Mensaje - Solicitud : " +  m.getSolicitud().getExpedienteNumero() + " Asunto: " + m.getAsunto().toString());
        
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

	void envioTotalizacion(Solicitud s) {
		
		Mensaje m = new Mensaje();

		m.setEstado(Mensaje.Estado.Enviado); //no necesita autorizacion
		m.setAutorizado(true);
		m.setRemitente(null);
		m.setAsunto(Asunto.TotalizacionTiempoServicio);
		m.setSolicitud(s);
		m.setFecha(new Date());
		
		//Escribimos el cuerto del mensaje
		String cuerpo = "";
		cuerpo += "Totalizacion de Tiempo de Servicio\r\n\r\n";
		
		for (CajaDeclarada c : s.getCajasDeclaradas() ) {
			cuerpo += c.getCaja().getSiglas() + ": " + CalculoTiempo.leeMeses(c.getTxNeto()) + "\r\n";
		}
		
		cuerpo += "Tiempo Neto : " + CalculoTiempo.leeMeses(s.getTxFinal());

		m.setCuerpo(cuerpo);
		//Fin cuerpo mensaje
		m.setReferencia(s.getExpedienteNumero() + " - " + s.getCotizante().getNombres() + " " + s.getCotizante().getApellidos() + " - Totalizacion de Tiempo de Servicio : " +  CalculoTiempo.leeMeses(s.getTxFinal()) + " de servicios");

		em.persist(m);
		
		//Enviamos a todas las cajas declaradas
		for (CajaDeclarada c : s.getCajasDeclaradas() ) {
			Destino d = new Destino();
			d.setMensaje(m);
			d.setDestinatario(c.getCaja());
//			d.setLeido(false);
//			d.setEstado(Estado.Pendiente);
			em.persist(d);
		}
		
		
		//Enviamos el reporte adjunto
		String reporteTotalizacion = reporteRest.totalizacion(s.getId());
		
		if (reporteTotalizacion != null) { //por si no funciono el reporte
			Adjunto a = new Adjunto();
			a.setMensaje(m);
			
			a.setNombreArchivo(reporteTotalizacion);
			a.setRutaArchivo("/reports/");
			a.setTipo(Adjunto.Tipo.TotalizacionTiempoServicio);
			
			em.persist(a);
			
		} else {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("No se pudo crear el informe de Totalizacion").build());
		}
		
		
	}

	
	private CajaDeclarada getUsuarioCajaDeclarada(Solicitud solicitud, UserDTO usuario) {
        CajaDeclarada usuarioCajaDeclarada = null;
        try {
	        usuarioCajaDeclarada = em.createQuery("select c"
					+ "                              from CajaDeclarada c"
					+ "                             where solicitud.id = :solicitud_id"
					+ "                               and caja.id      = :caja_id", CajaDeclarada.class)
					                           .setParameter("solicitud_id", solicitud.getId())
					                           .setParameter("caja_id", usuario.getCaja().getId())
					                           .getSingleResult();
	        return usuarioCajaDeclarada;
        } catch (NoResultException e) {
        	//TODO esto converit a mensaje que le llegue al usuario
        	throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("no corresponde la caja del usuario con el intento de reconocimiento de tiemp de servicios").build());
        }
        
	}
	
	private void autorizarNuevaSolicitud(Solicitud solicitud, UserDTO user) {
		userLogin.registrarAuditoria(user, "Autoriza Nueva Solicitud : " + solicitud.getExpedienteNumero() + " Cotizante: " + solicitud.getCotizante().toString());
	}
	
	private void autorizarFiniquito(Mensaje m, UserDTO user) {
		
		CajaDeclarada usuarioCajaDeclarada = getUsuarioCajaDeclarada(m.getSolicitud(), user);
		if (usuarioCajaDeclarada==null){
			throw new IllegalArgumentException("No se encontro la caja declarada");
		}

		Finiquito finiquitoAutorizado = null;
		//Marcamos como autorizado
		for(SolicitudBeneficiario sb : m.getSolicitud().getBeneficiarios()) {
			for (Finiquito f : sb.getFiniquitos()) {
				if (f.getMensaje().getId() == m.getId()) {
					finiquitoAutorizado = f;
					f.setAutorizado(true);
					em.persist(f);
				}
			}
		}
		
		//Verificamos que Todos los Beneficiarios de la caja del usuario tengan finiquito para marcar como Finiquitado
		List<Finiquito> finiquitos = em.createQuery("select f "
				                       + "  from Finiquito f, CajaDeclarada cd "
				                       + " where f.cajaDeclarada.id = cd.id "
				                       + "   and f.autorizado = true "
				                       + "   and cd.id = :caja_declarada_id",Finiquito.class)
				                       .setParameter("caja_declarada_id", usuarioCajaDeclarada.getId())
				                       .getResultList();
		
		if (finiquitos != null & finiquitos.size() == usuarioCajaDeclarada.getSolicitud().getBeneficiarios().size()) {
			usuarioCajaDeclarada.setEstado(CajaDeclarada.Estado.Finiquitado);
			em.persist(usuarioCajaDeclarada);
		}
		
		
		
		boolean todasFiniquitadas = true;
		//Si todas las CajasDeclaradas Finituiadas, entoces Solicitud Finiquitada
		for (CajaDeclarada cs : m.getSolicitud().getCajasDeclaradas()) {
			if (cs.getEstado() != CajaDeclarada.Estado.Finiquitado) {
				todasFiniquitadas = false;
			}
		}
				
		if (todasFiniquitadas) {
			Solicitud s = m.getSolicitud();
			s.setEstado(Solicitud.Estado.Finiquitado);
			em.persist(s);
		}
		
		
		
		userLogin.registrarAuditoria(user, "Autoriza Finiquito Solicitud : " + finiquitoAutorizado.getSolicitudBeneficiario().getSolicitud().getExpedienteNumero() + " Resolucion Finiquito " + finiquitoAutorizado.getNumeroResolucion());
		
	}
	
	void autorizarReconocimientoTiempoServicio(Mensaje mensaje, Solicitud solicitud, UserDTO user) {
		
		
		CajaDeclarada usuarioCajaDeclarada = getUsuarioCajaDeclarada(solicitud, user);
		

		/*
		 * Calculamos y guardamos los TX, tambien al Referencia del mensaje
		 */
		List<RangoTiempo> rangos = new ArrayList<RangoTiempo>();
		
		for (TiempoServicioReconocido tsr : usuarioCajaDeclarada.getListaTiempoServicioReconocido()) {
			if (tsr.getMensaje().getId() == mensaje.getId()) { 			//Autorizamos los TSR que corresponan al mensaje
				rangos.add(new RangoTiempo(tsr.getInicio(), tsr.getFin()));
				tsr.setAutorizado(true); //Validamos los registros
				em.persist(tsr);
			}
		}
		
		int txBruto = CalculoTiempo.txBruto(rangos);
		usuarioCajaDeclarada.setTxBruto(txBruto); 
		usuarioCajaDeclarada.setEstado(CajaDeclarada.Estado.ConAntiguedad);

		mensaje.setFecha(new Date());
		mensaje.setReferencia(solicitud.getExpedienteNumero() + " - " + solicitud.getCotizante().getNombres() + " " + solicitud.getCotizante().getApellidos() + " - " + user.getCaja().getSiglas() + " reconoce " +  CalculoTiempo.leeMeses(txBruto) + " de servicios");
		
		em.persist(mensaje);
		
				
		/*
		 * Aqui verificamos al aprobar, si todas las cajas declaras cuentan con antiguedad, enviamos la Totalizacion de Tiempo de Servicio
		 */
		boolean txCompleto = true;
//		//Verificamos si todas las cajas tiene estado ConAntiguedad, para pasar el estado de la solicitud a ConAntiguedad 
		for (CajaDeclarada c : solicitud.getCajasDeclaradas() ) {
			if (c.getEstado() != CajaDeclarada.Estado.ConAntiguedad) {
				txCompleto = false;
			}
		}
 
		if (txCompleto) {
			solicitud.setEstado(Solicitud.Estado.ConAntiguedad);
			//calculamos cada el txNeto de cada CajaDeclarada y el txFinal guardamos en la solicitud
			solicitud = CalculoTiempo.txNetoFinal(solicitud); //Esto calcula el txFinal de la solicitud y el txNeto de cada cajaDeclarada
			for (CajaDeclarada cds : solicitud.getCajasDeclaradas()) {
				em.persist(cds); //persistimos los txneto de cada caja declarada
			}
			em.persist(solicitud);
			
			em.flush(); //Con eso aseguramos que existan los registros para el reporte de totalizacion
			//Esto envia un mensaje a las cajas con el reporte de totalizacion
			envioTotalizacion(solicitud);
			
		}
		
		userLogin.registrarAuditoria(user, "Autoriza Reconocimiento Tiempo Servicio - Solicitud : " + mensaje.getSolicitud().getExpedienteNumero() + " Reconoce " + CalculoTiempo.leeMeses(txBruto));
		
	}

	
	@Path("/detalleAutorizarMensaje")
	@GET
	@Produces("text/plain")
	public String detalleAutorizarMensaje(@QueryParam("mensaje_id") Long mensaje_id) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(0);
		df.setMinimumFractionDigits(0);
//		df.setGroupingUsed(false);

		Mensaje m = em.find(Mensaje.class, mensaje_id);
		
		if (m==null) {
			return null;
		}
		
		String detalleHTML = "";

		detalleHTML += "<font color='blue'>Mensaje</font><br>";
		detalleHTML += "Creado el " + sdf.format(m.getFecha()) + "<br>";
		detalleHTML += "Asunto: " + m.getAsunto().toString() + "<br>";
		detalleHTML += "<font color='blue'>Solicitud</font><br>";
		detalleHTML += "Creado el " + sdf.format(m.getSolicitud().getFecha()) + "<br>";
		detalleHTML += "Cotizante:  " + m.getSolicitud().getCotizante().toString() + "<br>";
		detalleHTML += "<font color='blue'>Solicitante</font><br>";
		for (SolicitudBeneficiario sb : m.getSolicitud().getBeneficiarios()) {
			if (sb.getTipo() == SolicitudBeneficiario.Tipo.Titular) {
				detalleHTML += "" + m.getSolicitud().getCotizante().toString() + "<br>";
			} else {
				detalleHTML += "" + sb.getBeneficiario().toString() + " - " + sb.getParentesco().toString() + "<br>";
			}
		}	
		
		detalleHTML += "<font color='blue'>Cajas Declaradas</font><br>";
		
		for (CajaDeclarada cd : m.getSolicitud().getCajasDeclaradas()) {
			detalleHTML += cd.getCaja().getSiglas() + " - " + CalculoTiempo.leeMeses(cd.getTxDeclarado()) + "<br>";
		}
		
		if (m.getAsunto() ==  Mensaje.Asunto.ReconocimientoTiempoServicio) {
			List<RangoTiempo> rangos = new ArrayList<RangoTiempo>();
			
			for (TiempoServicioReconocido tsr : m.getListaTiempoServicioReconocidos()) {
				rangos.add(new RangoTiempo(tsr.getInicio(), tsr.getFin()));
			}
			detalleHTML += "<font color='blue'>Tiempo Servicio Reconocido</font><br>";
			detalleHTML += "" + CalculoTiempo.leeMeses(CalculoTiempo.txBruto(rangos)) + "<br>";
			
		} else if (m.getAsunto() == Mensaje.Asunto.Concedido) {
			detalleHTML += "<font color='blue'>Finiquito - Concedido</font><br>";
			Finiquito f = m.getListaFiniquitos().get(0); //siempre deberia de haber una sola fila
			Concedido c = (Concedido) f;
			detalleHTML += "Resolucion N°: " + c.getNumeroResolucion() + "<br>";
			detalleHTML += "Monto Final Gs: " + df.format(c.getBx()) + "<br>";
		} else if (m.getAsunto() == Mensaje.Asunto.Denegado) {
			detalleHTML += "<font color='blue'>Finiquito - Denedago</font><br>";
			Finiquito f = m.getListaFiniquitos().get(0); //siempre deberia de haber una sola fila
			Denegado d = (Denegado) f;
			detalleHTML += "Resolucion N°: " + d.getNumeroResolucion() + "<br>";
			detalleHTML += "Motivo: " + d.getMotivo().toString() + "<br>";
		}
		
		return detalleHTML;
		
	}
	
}