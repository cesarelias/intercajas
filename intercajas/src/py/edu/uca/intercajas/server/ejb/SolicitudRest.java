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
import javax.ws.rs.core.Context;

import py.edu.uca.intercajas.server.CalculoTiempo;
import py.edu.uca.intercajas.shared.NuevaSolicitud;
import py.edu.uca.intercajas.shared.NuevoReconocimientoTiempoServicio;
import py.edu.uca.intercajas.shared.RangoTiempo;
import py.edu.uca.intercajas.shared.UserDTO;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Caja;
import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
import py.edu.uca.intercajas.shared.entity.Destino;
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
		System.out.println("rest working");
		return "rest working";
	}
	
	@Path("/")
	@GET
	@Produces("application/json")
	public Solicitud find(@QueryParam("id") Long id) {
		System.out.println("**************************************id :"+id);
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
        	System.out.println("usuario no valido para el llamado rest!");
       	   return;
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
        	System.out.println("no corresponde la caja del usuario con el intento de reconocimiento de tiemp de servicios");
        	return;
        }
		
        
		Solicitud s = em.find(Solicitud.class,nuevoReconocimientoTiempoServicio.getSolicitud().getId());
		if (s==null) { 
			return;
		}
		
		if (nuevoReconocimientoTiempoServicio.getMensaje() == null) {
				throw new IllegalArgumentException("Al reconocer tiempo de servicio , debe adjuntarle un unico mensaje");
		}
		
		Destino destino = em.find(Destino.class, nuevoReconocimientoTiempoServicio.getDestino().getId());
		if (destino == null) {
			throw new IllegalArgumentException("No existe el detino");
		}
		
		destino.setEstado(Destino.Estado.Atendido);
		em.persist(destino);
		
		
		for (TiempoServicioReconocido tsr : nuevoReconocimientoTiempoServicio.getListaTiempoServicioReconocido()) {
			tsr.setEmpleador(null); //TODO Esto falta !!!!
			tsr.setCajaDeclarada(usuarioCajaDeclarada); //Esto asegura que los tiempos reconocidos provienen de la caja asociada al usuario!
			em.persist(tsr);
		}
		
		
//		Esto lo hacemos ahora al Autorizar, no al crear el nuevo reconocimiento de tiempo de servicio
//		usuarioCajaDeclarada.setEstado(CajaDeclarada.Estado.ConAntiguedad);
//		em.persist(usuarioCajaDeclarada);
		
		Mensaje m = nuevoReconocimientoTiempoServicio.getMensaje();
		
		m.setEstado(Mensaje.Estado.Pendiente);
		m.setSolicitud(s);
		m.setFecha(new Date()); //la fecha no se si es al crear o al autorizar
		m.setRemitente(em.find(Caja.class, user.getCaja().getId()));
		
		//la referencia cargamos al autorizar
//		m.setReferencia(s.getNumero() + " - " + s.getCotizante().getNombres() + " " + s.getCotizante().getApellidos() + " - " + user.getCaja().getSiglas() + " reconoce " +  CalculoTiempo.leeMeses(txBruto) + " de servicios");
		for (Adjunto a : nuevoReconocimientoTiempoServicio.getAdjuntos()) {
			a.setMensaje(m);
			em.persist(a);
		}
		em.persist(m);

		//Enviamos a todas las cajas declaradas
		for (CajaDeclarada c : s.getCajasDeclaradas() ) {
			Destino d = new Destino();
			d.setMensaje(m);
			d.setDestinatario(c.getCaja());
			d.setLeido(false);
			d.setEstado(Destino.Estado.Pendiente);
			em.persist(d);
		}
		
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
        	System.out.println("usuario no valido para el llamado rest!");
       	   return;
       }
		
		if (nuevaSolicitud.getMensaje() == null) {
				throw new IllegalArgumentException("Al insertar una nueva solitidu, debe adjuntarle un unico mensaje");
		}
		
		//TODO controlar que existan al menos dos cajas declaradas
		if (nuevaSolicitud.getListaTiempoServicioDeclarado() == null || nuevaSolicitud.getListaTiempoServicioDeclarado().size() <= 0 ) {
			throw new IllegalArgumentException("Al insertar una nueva solitidu, debe adjuntarle los tiempos de servicios");
		}
		
		Solicitud solicitud = nuevaSolicitud.getSolicitud();
		Mensaje m = nuevaSolicitud.getMensaje();
		
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
			d.setEstado(Destino.Estado.Pendiente);
			em.persist(d);
			
		}
		
		solicitud.setTxFinal(0); //iniciamos con 0 meses
		solicitud.setFecha(new Date()); //fecha del dia
		em.persist(solicitud);
		
		//Como la solicitud es del titular, hacemos SolicitudBenefiario = al cotizante
		SolicitudBeneficiario sb = new SolicitudBeneficiario();
		sb.setBeneficiario(solicitud.getCotizante());
		sb.setSolicitud(solicitud);
		
		em.persist(sb);
		
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

	
}