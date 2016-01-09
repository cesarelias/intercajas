package py.edu.uca.intercajas.client.requestfactory;

import java.util.List;

import py.edu.uca.intercajas.server.ejb.BeanLocator;
import py.edu.uca.intercajas.server.ejb.GestionBeneficiario;
import py.edu.uca.intercajas.server.entity.enums.TipoDocumentoIdentidad;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value=GestionBeneficiario.class, locator=BeanLocator.class)

public interface ContextGestionBeneficiario extends RequestContext{
	Request<Long> insertarBeneficiario(BeneficiarioProxy beneficiario);
	Request<Void> actualizarBeneficiario(BeneficiarioProxy beneficiario);
	Request<BeneficiarioProxy> find (Long id);
	Request<List<BeneficiarioProxy>> findAll ();
	Request<List<BeneficiarioProxy>> findByNombresDocs(String nombresDocs, int startRow, int maxResults);
	Request<BeneficiarioProxy> findByDocumento(String numeroDocumento, TipoDocumentoIdentidad tipoDocumento);
}
