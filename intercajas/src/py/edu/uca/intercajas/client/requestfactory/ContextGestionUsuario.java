package py.edu.uca.intercajas.client.requestfactory;

import py.edu.uca.intercajas.server.ejb.BeanLocator;
import py.edu.uca.intercajas.server.ejb.GestionUsuario;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value=GestionUsuario.class, locator=BeanLocator.class)
public interface ContextGestionUsuario extends RequestContext{
	Request<Boolean> insertarUsuario(UsuarioProxy bean);
	Request<Boolean> insertarUsuario(String correo, String clave,String nombres,String apellidos);
	Request<UsuarioProxy> login(String correo, String clave);
}
