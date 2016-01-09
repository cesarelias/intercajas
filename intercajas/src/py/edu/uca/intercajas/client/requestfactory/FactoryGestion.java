package py.edu.uca.intercajas.client.requestfactory;

import com.google.web.bindery.requestfactory.shared.LoggingRequest;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

public interface FactoryGestion extends RequestFactory{
	
	LoggingRequest loggingRequest();
	ContextGestionUsuario contextGestionUsuario();
	ContextGestionSolicitud contextGestionSolicitud();
	ContextGestionCosto contextGestionCosto();
	ContextGestionBeneficiario contextGestionBeneficiario();
}
