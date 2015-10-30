package py.edu.uca.intercajas.client.requestfactory;

import py.edu.uca.intercajas.server.ejb.BeanLocator;
import py.edu.uca.intercajas.server.ejb.GestionBeneficiario;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value=GestionBeneficiario.class, locator=BeanLocator.class)

public interface ContextGestionBeneficiario extends RequestContext{
	Request<Boolean> insertarBeneficiario(BeneficiarioProxy beneficiario);
	Request<BeneficiarioProxy> find (Long id);
}
