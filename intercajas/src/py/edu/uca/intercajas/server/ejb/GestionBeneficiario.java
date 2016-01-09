package py.edu.uca.intercajas.server.ejb;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import py.edu.uca.intercajas.server.entity.Beneficiario;
import py.edu.uca.intercajas.server.entity.enums.TipoDocumentoIdentidad;
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
	
	public Beneficiario findByDocumento(String numeroDocumento, TipoDocumentoIdentidad tipoDocumento) {
		List<Beneficiario> beneficiarios = em.createQuery("select b from Beneficiario b where "
				+ "b.documento.numeroDocumento = :numeroDocumento and b.documento.tipoDocumento = :tipoDocumento",Beneficiario.class)
				.setParameter("numeroDocumento", numeroDocumento)
				.setParameter("tipoDocumento", tipoDocumento)
				.getResultList();
		if (beneficiarios.size() == 0) {
			return null;
		} ;
		return beneficiarios.get(0);
		
	}

	public List<Beneficiario> findAll() {
		return em.createQuery("select b from Beneficiario b",Beneficiario.class).getResultList();
	}

	public List<Beneficiario> findByNombresDocs(String nombresDocs, int startRow, int maxResults) {
		
		if (nombresDocs == null || nombresDocs.length() == 0) {
			nombresDocs = "%";
		} else {
			nombresDocs = '%' + nombresDocs.toUpperCase() + '%';
		}
		
		return em.createQuery("select b "
				+ "              from Beneficiario b "
				+ "             where UPPER(b.nombres) like :nombresDocs "
				+ "                or UPPER(b.apellidos) like :nombresDocs"
				+ "                or UPPER(b.documento.numeroDocumento) like :nombresDocs "
				+ " order by b.nombres, b.apellidos asc"
				,Beneficiario.class)
				.setParameter("nombresDocs", nombresDocs)
				.setFirstResult(startRow)
				.setMaxResults(maxResults)
				.getResultList();
	}

	
	public Long insertarBeneficiario(Beneficiario beneficiario)  {
			em.persist(beneficiario);
			LOG.info("Beneficiario persisted");
			return beneficiario.getId();
	}
	            
	public void actualizarBeneficiario(Beneficiario beneficiario)  {
		em.merge(beneficiario);
		LOG.info("Beneficiario merged");
}
	
	
}
