package py.edu.uca.intercajas.client.requestfactory;

import java.util.List;

import py.edu.uca.intercajas.server.ejb.BeanLocator;
import py.edu.uca.intercajas.server.ejb.GestionCosto;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value=GestionCosto.class, locator=BeanLocator.class)
public interface ContextGestionCosto extends RequestContext{
	Request<Boolean> insertarCosto(CostoProxy bean);
	Request<Boolean> actualizarCosto(CostoProxy bean);
	Request<Boolean> eliminarCosto(CostoProxy bean);
	Request<List<CostoProxy>> listarCosto();
	Request<List<CostoProxy>> listarCosto(String correo);
}
