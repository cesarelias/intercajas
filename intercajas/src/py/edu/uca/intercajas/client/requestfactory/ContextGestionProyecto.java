package py.edu.uca.intercajas.client.requestfactory;

import py.edu.uca.intercajas.server.ejb.GestionProyecto;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value=GestionProyecto.class)
public interface ContextGestionProyecto extends RequestContext{
	Request<Boolean> insertarProyecto(ProyectoProxy bean);
}
