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

import py.edu.uca.intercajas.client.LoginService;
import py.edu.uca.intercajas.server.CalculoTiempo;
import py.edu.uca.intercajas.shared.NuevaSolicitudTitular;
import py.edu.uca.intercajas.shared.NuevoReconocimientoTiempoServicio;
import py.edu.uca.intercajas.shared.RangoTiempo;
import py.edu.uca.intercajas.shared.UserDTO;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Caja;
import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
import py.edu.uca.intercajas.shared.entity.Destino;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.TiempoServicioDeclarado;
import py.edu.uca.intercajas.shared.entity.Solicitud;
import py.edu.uca.intercajas.shared.entity.SolicitudTitular;
import py.edu.uca.intercajas.shared.entity.TiempoServicioReconocido;
import py.edu.uca.intercajas.shared.entity.Mensaje.Asunto;

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
		
		UserDTO user = userLogin.getValidUser(req.getSession().getId());
        if (user == null) {
        	System.out.println("usuario no valido para el llamado rest!");
       	   return;
       }

        Boolean txCompleto = true;
        
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
				throw new IllegalArgumentException("Al reconocer timepo de servicio , debe adjuntarle un unico mensaje");
		}
		
		
		List<RangoTiempo> rangos = new ArrayList<RangoTiempo>();
		
		for (TiempoServicioReconocido tsr : nuevoReconocimientoTiempoServicio.getListaTiempoServicioReconocido()) {
			rangos.add(new RangoTiempo(tsr.getInicio(), tsr.getFin()));
			tsr.setEmpleador(null); //TODO Esto falta !!!!
			tsr.setCajaDeclarada(usuarioCajaDeclarada); //Esto asegura que los tiempos reconocidos provienen de la caja asociada al usuario!
			em.persist(tsr);
		}
		
		int txBruto = CalculoTiempo.txBruto(rangos);
		usuarioCajaDeclarada.setTxBruto(txBruto); 
		
		usuarioCajaDeclarada.setEstado(CajaDeclarada.Estado.ConAntiguedad);
		
		
		List<TiempoServicioReconocido> lista = em.createQuery("select t"
				                                + "              from TiempoServicioReconocido t "
				                                + "             where cajaDeclarada.solicitud.id = :solicitud_id",
				                                TiempoServicioReconocido.class)
				                                .setParameter("solicitud_id", s.getId())
				                                .getResultList();
		
		
		em.persist(usuarioCajaDeclarada);
		
		Mensaje m = nuevoReconocimientoTiempoServicio.getMensaje();
		
		m.setSolicitud(s);
		m.setFecha(new Date());
		m.setRemitente(em.find(Caja.class, user.getCaja().getId()));
		
		//TODO sacar o mejorar esto!!!
		//esto esta mientras nomas 
		SolicitudTitular ss = (SolicitudTitular) s;
		
		m.setReferencia(s.getNumero() + " - " + ss.getBeneficiario().getNombres() + " " + ss.getBeneficiario().getApellidos() + " - " + user.getCaja().getSiglas() + " reconoce " +  CalculoTiempo.leeMeses(txBruto) + " de servicios");
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
			em.persist(d);
		}
		
//		//Verificamos si todas las cajas tiene estado ConAntiguedad, para pasar el estado de la solicitud a ConAntiguedad 
		for (CajaDeclarada c : s.getCajasDeclaradas() ) {
			if (c.getEstado() != CajaDeclarada.Estado.ConAntiguedad) {
				txCompleto = false;
			}
		}
		// estee no es el caso
		if (txCompleto) {
			s.setEstado(Solicitud.Estado.ConAntiguedad);
			//calculamos cada el txNeto de cada CajaDeclarada y el txFinal guardamos en la solicitud
			s = CalculoTiempo.txNetoFinal(s);
			em.persist(s);
			
			//Esto envia un mensaje a las cajas con el reporte de totalizacion
			envioTotalizacion(s);
			
		}
		
		LOG.info("Solicitud titular persisted");

	}
	
	
	
	void envioTotalizacion(Solicitud s) {
		
		//TODO sacar o mejorar esto!!!
		//esto esta mientras nomas 
		SolicitudTitular ss = (SolicitudTitular) s;
		
		
		Mensaje m = new Mensaje();

		m.setRemitente(null);
		m.setAsunto(Asunto.TotalizacionTiempoServicio);
		m.setSolicitud(s);
		m.setFecha(new Date());
		m.setCuerpo("Este es un mensaje automatico, se envia la totalizacion de aportes:_vamos a mejorar el contenido del mimo.");
		m.setReferencia(s.getNumero() + " - " + ss.getBeneficiario().getNombres() + " " + ss.getBeneficiario().getApellidos() + " - Totalizacion de Tiempo de Servicio : " +  CalculoTiempo.leeMeses(ss.getTxFinal()) + " de servicios");

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
			em.persist(d);
		}

	}
	
}