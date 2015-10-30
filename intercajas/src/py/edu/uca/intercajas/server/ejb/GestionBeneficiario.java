package py.edu.uca.intercajas.server.ejb;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import py.edu.uca.intercajas.server.entity.Beneficiario;
import py.edu.uca.intercajas.shared.UnknownException;

@Stateless
public class GestionBeneficiario {
	private static final Logger LOG = Logger
			.getLogger(GestionBeneficiario.class.getName());
	
	

	@PersistenceContext
	EntityManager em;
	

	public Beneficiario find(Long id) {
		return em.find(Beneficiario.class, id);
	}
	
	public Boolean insertarBeneficiario(Beneficiario beneficiario)
			throws UnknownException {
		
		try {
			em.persist(beneficiario);
			LOG.info("Beneficiario persisted");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("Beneficiario no pesisted: " + e.getMessage());
			throw new UnknownException(e.getMessage());
		}
		return true;
	}

}
