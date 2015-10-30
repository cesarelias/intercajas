package py.edu.uca.intercajas.client.requestfactory;

import py.edu.uca.intercajas.server.ejb.BeanLocator;
import py.edu.uca.intercajas.server.ejb.GestionSolicitud;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value=GestionSolicitud.class, locator=BeanLocator.class)
public interface ContextGestionSolicitud extends RequestContext{
	Request<SolicitudProxy> find (Long id);
}
