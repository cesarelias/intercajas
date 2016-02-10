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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import py.edu.uca.intercajas.server.CalculoTiempo;
import py.edu.uca.intercajas.shared.NuevaAutorizacion;
import py.edu.uca.intercajas.shared.NuevaSolicitud;
import py.edu.uca.intercajas.shared.RangoTiempo;
import py.edu.uca.intercajas.shared.UserDTO;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Caja;
import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
import py.edu.uca.intercajas.shared.entity.Destino;
import py.edu.uca.intercajas.shared.entity.Destino.Estado;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.Solicitud;
import py.edu.uca.intercajas.shared.entity.TiempoServicioReconocido;
import py.edu.uca.intercajas.shared.entity.Usuario;
import py.edu.uca.intercajas.shared.entity.Mensaje.Asunto;

@Path("/mensaje")
@Stateless
public class MensajeRest   {

	private static final Logger LOG = Logger.getLogger(MensajeRest.class.getName());

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
//		for (Mensaje m : retorno) {
//			m.getAdjuntos().size(); //lazy
//		}
		return retorno; 
	}

	@Path("/findAllPending")
	@GET
	@Produces("application/json")
	public List<Mensaje> findByNombresDocs(@QueryParam("startRow") int startRow,
												@QueryParam("maxResults") int maxResults) {
		
//		if (nombresDocs == null || nombresDocs.length() == 0) {
//			nombresDocs = "%";
//		} else {
//			nombresDocs = '%' + nombresDocs.toUpperCase() + '%';
//		}
		
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
        	System.out.println("usuario no valido para el llamado rest!");
       	   return;
        }
        
        if (user.getTipo() != Usuario.Tipo.Superior ) {
        	System.out.println("usuario no valido para autorizar/enviar mensaje");
        	return;
        }
        
        Destino d = em.find(Destino.class, nuevaAutorizacion.getDestino().getId());
        if (d==null) {
        	System.out.println("Mensaje no valido");
        	return;
        }        
        
        //TODO esto falta
        //El Destino marcamos como Atendido para NuevaSolicitud o RTS, Para concedido o Denegado, una vez finiquitado todos los solicitantes.
        d.setEstado(Destino.Estado.Atendido);
        em.persist(d);
        
        Mensaje m = d.getMensaje();
        if (m==null || m.getEstado() != Mensaje.Estado.Pendiente) {
        	System.out.println("Mensaje no valido, null o no Pendiente");
        	return;
        }

        CajaDeclarada usuarioCajaDeclarada = getUsuarioCajaDeclarada(m.getSolicitud(), user);
		
        
        //Guardamos el o los adjuntos
		for (Adjunto a : nuevaAutorizacion.getAdjuntos()) {
			a.setMensaje(m);
			em.persist(a);
		}
        
		
		
		if (m.getAsunto() == Mensaje.Asunto.NuevaSolicitud) {
			autorizarNuevaSolicitud(m.getSolicitud()); //Esto pone en true todas las autorizaciones en cada caja declarada
		} else if (m.getAsunto() == Mensaje.Asunto.ReconocimientoTiempoServicio) {
			autorizarReconocimientoTimepoServicio(m, m.getSolicitud(), user);
		} else if (m.getAsunto() == Mensaje.Asunto.Concedido) {
			autorizarConcedido(m, user);
		} else if (m.getAsunto() == Mensaje.Asunto.Denegado) {
			autorizarDenegado(m, user);
		} 
		
		
		//Cambiamos el estado del mensaje a Enviado
		m.setObservacion(nuevaAutorizacion.getDestino().getMensaje().getObservacion());
        m.setEstado(Mensaje.Estado.Enviado);
        em.persist(m);
        
		
	}


	@Path("/anular")
	@POST
	@Consumes("application/json")
	public void anular(Mensaje mensaje, @Context HttpServletRequest req) { 
	
		UserDTO user = userLogin.getValidUser(req.getSession().getId());
        if (user == null) {
        	System.out.println("usuario no valido para el llamado rest!");
       	   return;
        }
        
        if (user.getTipo() != Usuario.Tipo.Superior ) {
        	System.out.println("usuario no valido para autorizar/enviar mensaje");
        	return;
        }
        
        Mensaje m = em.find(Mensaje.class, mensaje.getId());
        
        if (m==null || m.getEstado() != Mensaje.Estado.Pendiente) {
        	System.out.println("Mensaje id no valido");
        	return;
        }
        
		//Cambiamos el estado del mensaje a Anulado
        m.setObservacion(mensaje.getObservacion());
        m.setEstado(Mensaje.Estado.Anulado);
        em.persist(m);
        
        
        
        
	}
	
	
	void autorizarReconocimientoTimepoServicio(Mensaje mensaje, Solicitud solicitud, UserDTO usuario) {
		
		
		CajaDeclarada usuarioCajaDeclarada = getUsuarioCajaDeclarada(solicitud, usuario);
		

		/*
		 * Calculamos y guardamos los TX, tambien al Referencia del mensaje
		 */
		List<RangoTiempo> rangos = new ArrayList<RangoTiempo>();
		
		for (TiempoServicioReconocido tsr : usuarioCajaDeclarada.getListaTiempoServicioReconocido()) {
			rangos.add(new RangoTiempo(tsr.getInicio(), tsr.getFin()));
		}
		
		int txBruto = CalculoTiempo.txBruto(rangos);
		usuarioCajaDeclarada.setTxBruto(txBruto); 
		usuarioCajaDeclarada.setEstado(CajaDeclarada.Estado.ConAntiguedad);

		mensaje.setFecha(new Date());
		mensaje.setReferencia(solicitud.getNumero() + " - " + solicitud.getCotizante().getNombres() + " " + solicitud.getCotizante().getApellidos() + " - " + usuario.getCaja().getSiglas() + " reconoce " +  CalculoTiempo.leeMeses(txBruto) + " de servicios");
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
			solicitud = CalculoTiempo.txNetoFinal(solicitud);
			em.persist(solicitud);
			
			//Esto envia un mensaje a las cajas con el reporte de totalizacion
			envioTotalizacion(solicitud);
			
		}
		
	}
	
	void envioTotalizacion(Solicitud s) {
		
		
		Mensaje m = new Mensaje();

		m.setEstado(Mensaje.Estado.Enviado); //no necesita autorizacion
		m.setRemitente(null);
		m.setAsunto(Asunto.TotalizacionTiempoServicio);
		m.setSolicitud(s);
		m.setFecha(new Date());

		//Escribimos el cuerto del mensaje
		String cuerpo = "";
		cuerpo += "Totalizacion de Tiempo de Servicio\r\n\r\n";
		
		
		for (CajaDeclarada c : s.getCajasDeclaradas() ) {
			cuerpo += c.getCaja().getSiglas() + " txNeto: " + CalculoTiempo.leeMeses(c.getTxNeto()) + "\r\n";
		}
		
		cuerpo += "txFinal : " + CalculoTiempo.leeMeses(s.getTxFinal());

		m.setCuerpo(cuerpo);
		//Fin cuerpo mensaje
		m.setReferencia(s.getNumero() + " - " + s.getCotizante().getNombres() + " " + s.getCotizante().getApellidos() + " - Totalizacion de Tiempo de Servicio : " +  CalculoTiempo.leeMeses(s.getTxFinal()) + " de servicios");

		//TODO enviar como adjunto un reporte en PDF del sistema
//		for (Adjunto a : nuevoReconocimientoTiempoServicio.getAdjuntos()) {
//			a.setMensaje(m);
//			em.persist(a);
//		}
		em.persist(m);
		
		//Enviamos a todas las cajas declaradas
		for (CajaDeclarada c : s.getCajasDeclaradas() ) {
			Destino d = new Destino();
			d.setMensaje(m);
			d.setDestinatario(c.getCaja());
			d.setLeido(false);
			d.setEstado(Estado.Pendiente);
			em.persist(d);
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
        	System.out.println("no corresponde la caja del usuario con el intento de reconocimiento de tiemp de servicios");
        	return null;
        }
        
	}
	
	private void autorizarNuevaSolicitud(Solicitud solicitud) {
		
	}
	
	private void autorizarConcedido(Mensaje m, UserDTO usuario) {
		
		CajaDeclarada usuarioCajaDeclarada = getUsuarioCajaDeclarada(m.getSolicitud(), usuario);
		if (usuarioCajaDeclarada==null){
			throw new IllegalArgumentException("No se encontro la caja declarada");
		}
		
		usuarioCajaDeclarada.setEstado(CajaDeclarada.Estado.Concedido);
		em.persist(usuarioCajaDeclarada);
		
		
	}
	
	private void autorizarDenegado(Mensaje m, UserDTO usuario) {
		
		CajaDeclarada usuarioCajaDeclarada = getUsuarioCajaDeclarada(m.getSolicitud(), usuario);
		if (usuarioCajaDeclarada==null){
			throw new IllegalArgumentException("No se encontro la caja declarada");
		}
		
		usuarioCajaDeclarada.setEstado(CajaDeclarada.Estado.Denegado);
		em.persist(usuarioCajaDeclarada);

		
	}

}