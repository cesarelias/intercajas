package py.edu.uca.intercajas.client.requestfactory;

import com.google.web.bindery.requestfactory.shared.RequestFactory;

public interface FactoryGestion extends RequestFactory{
	ContextGestionUsuario contextGestionUsuario();
	ContextGestionCosto contextGestionCosto();
	ContextGestionProyecto contextGestionProyecto();
}
