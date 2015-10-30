package py.edu.uca.intercajas.client.requestfactory;

import py.edu.uca.intercajas.server.entity.EntityLocator;
import py.edu.uca.intercajas.server.entity.SolicitudTitular;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;


@ProxyFor(value=SolicitudTitular.class,locator=EntityLocator.class)
public interface SolicitudTitularProxy extends EntityProxy {

	public BeneficiarioProxy getBeneficiario();

	public void setBeneficiario(BeneficiarioProxy beneficiario);

}
