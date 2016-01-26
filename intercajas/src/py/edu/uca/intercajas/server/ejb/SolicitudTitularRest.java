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

import py.edu.uca.intercajas.shared.NuevaSolicitudTitular;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Caja;
import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
import py.edu.uca.intercajas.shared.entity.Destino;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.TiempoServicioDeclarado;
import py.edu.uca.intercajas.shared.entity.SolicitudTitular;

@Path("/solicitudTitular")
@Stateless
public class SolicitudTitularRest   {

	private static final Logger LOG = Logger.getLogger(SolicitudTitularRest.class.getName());
	

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
	public SolicitudTitular find(@PathParam("id") Long id) {
		return em.find(SolicitudTitular.class, id);
	}
	
	@Path("/findAll")
	@GET
	@Produces("application/json")
	public List<SolicitudTitular> findAll() {
		List<SolicitudTitular> lista = em.createQuery("select b from SolicitudTitular b", SolicitudTitular.class).getResultList();
		return lista;
	}
	
	/*
	 * Este metodo, crea la solicitud, el adjunto(s), las cajas declaras (al menos dos), y hace el envio (mensaje + destino(s))
	 */
	@Path("/nuevo")
	@POST
	@Consumes("application/json")
	public void nuevo(NuevaSolicitudTitular nuevaSolicitudTitular) {
		
		if (nuevaSolicitudTitular.getMensaje() == null) {
				throw new IllegalArgumentException("Al insertar una nueva solitidu, debe adjuntarle un unico mensaje");
		}
		
		//TODO controlar que existan al menos dos cajas declaradas
		if (nuevaSolicitudTitular.getListaTiempoServicioDeclarado() == null || nuevaSolicitudTitular.getListaTiempoServicioDeclarado().size() <= 0 ) {
			throw new IllegalArgumentException("Al insertar una nueva solitidu, debe adjuntarle los tiempos de servicios");
		}
		
		SolicitudTitular solicitudTitular = nuevaSolicitudTitular.getSolicitudTitular();
		Mensaje m = nuevaSolicitudTitular.getMensaje();
		
		m.setSolicitud(solicitudTitular);
		m.setFecha(new Date());
		m.setRemitente(em.find(Caja.class, 1L)); //TODO Esto debe ser la caja asociada al inicio de sesion
		for (Adjunto a : nuevaSolicitudTitular.getAdjuntos()) {
			a.setMensaje(m);
			em.persist(a);
		}
		em.persist(m);
		
		for (TiempoServicioDeclarado pad : nuevaSolicitudTitular.getListaTiempoServicioDeclarado()) {
			pad.setSolicitud(solicitudTitular);
			em.persist(pad);
		}
		
		//Creamos las CajasDeclaradas en base a los TiemposServiciosDeclarados
		//Tambien creamos los detinatarios del mensaje
		List<CajaDeclarada> cajaDeclaradas = CalculoTiempo.tx_calculado(nuevaSolicitudTitular.getListaTiempoServicioDeclarado());
		for (CajaDeclarada c : cajaDeclaradas ) {
			c.setSolicitud(solicitudTitular);
			em.persist(c);
			
			Destino d = new Destino();
			d.setMensaje(m);
			d.setDestinatario(c.getCaja());
			d.setLeido(false);
			em.persist(d);
			
			
		}
		
		em.persist(solicitudTitular);
		LOG.info("Solicitud titular persisted");
	}

}