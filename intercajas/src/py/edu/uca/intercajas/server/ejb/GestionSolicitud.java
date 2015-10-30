package py.edu.uca.intercajas.server.ejb;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.edu.uca.intercajas.server.entity.Solicitud;

@Stateless
public class GestionSolicitud {
	private static final Logger LOG = Logger
			.getLogger(GestionSolicitud.class.getName());
	

	@PersistenceContext
	EntityManager em;
	

	public Solicitud find(Long id) {
		LOG.info("GestionSolicitud.find llamado");
		return em.find(Solicitud.class, id);
	}
	

}
