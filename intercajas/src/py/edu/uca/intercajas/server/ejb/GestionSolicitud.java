package py.edu.uca.intercajas.server.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import py.edu.uca.intercajas.server.entity.Beneficiario;
import py.edu.uca.intercajas.server.entity.Caja;
import py.edu.uca.intercajas.server.entity.Empleador;
import py.edu.uca.intercajas.server.entity.PeriodoAporteDeclarado;
import py.edu.uca.intercajas.server.entity.Solicitud;
import py.edu.uca.intercajas.server.entity.SolicitudTitular;

@Stateless
public class GestionSolicitud {
	private static final Logger LOG = Logger.getLogger(GestionSolicitud.class
			.getName());

	@PersistenceContext
	EntityManager em;

	public Solicitud find(Long id) {
		LOG.info("GestionSolicitud.find llamado");
		return em.find(Solicitud.class, id);
	}

	public List<Empleador> findEmpleadorByNombre(String nombre) {
		return em.createQuery("from Empleador where nombre = :nombre",Empleador.class).setParameter("nombre", nombre).getResultList();
	}
	
	public Empleador findEmpleadorById(Long id) {
		return em.find(Empleador.class, id);
	}


	public Caja findCajaById(Long id) {
		return em.find(Caja.class, id);
	}
	
	public Long insertarEmpleador(Empleador empleador) {
		System.out.println("*************************************************************test ejb");
		System.out.println(empleador.getCaja().getNombre());
		System.out.println(empleador.getCaja().getId());
		em.persist(empleador);
		return empleador.getId();
	}
	
	public PeriodoAporteDeclarado findPeriodoAporteDeclarado(Long id) {
		LOG.info("GestionSolicitud.findPeriodoAporteDeclarado llamado");
		return em.find(PeriodoAporteDeclarado.class, id);
		
	}
	
	public List<Caja> getCajas() {
		return em.createQuery("from Caja", Caja.class).getResultList();
	}

	public void nuevoSolicitudTitular(SolicitudTitular solicitudTitular) {
		if (solicitudTitular.getListaPeriodoAporteDeclarados() == null){
				System.out.println("no debe ser nulo los periodosDeAportesDeclarados");
		} else {
			for (PeriodoAporteDeclarado pad : solicitudTitular.getListaPeriodoAporteDeclarados()) {
				System.out.println("pad.getLugar(): " + "pag.get");
				pad.setSolicitud(solicitudTitular);
				em.persist(pad);
			}
		}
		
		em.persist(solicitudTitular);
		LOG.info("Solicitud titular persisted");
		
	}

	public List<Empleador> getEmpleador(Long caja_id) {
		return em.createQuery("select e from Empleador e where e.caja.id = :caja_id", Empleador.class)
				.setParameter("caja_id", caja_id)
				.getResultList();
	}
	
}
