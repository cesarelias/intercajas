package py.edu.uca.intercajas.client.requestfactory;

import py.edu.uca.intercajas.server.ejb.GestionCosto;
import py.edu.uca.intercajas.shared.UnknownException;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value=GestionCosto.class)
public interface ContextGestionCosto extends RequestContext{
	Request<Void> test(String test);
}
