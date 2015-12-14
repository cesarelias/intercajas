package py.edu.uca.intercajas.client.requestfactory;

import py.edu.uca.intercajas.shared.UnknownException;

import com.google.web.bindery.requestfactory.shared.LoggingRequest;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

public interface FactoryGestion extends RequestFactory{
	
	LoggingRequest loggingRequest();
	ContextGestionUsuario contextGestionUsuario();
	ContextGestionCosto contextGestionCosto();
	ContextGestionProyecto contextGestionProyecto();
	ContextGestionBeneficiario contextGestionBeneficiario();
}
