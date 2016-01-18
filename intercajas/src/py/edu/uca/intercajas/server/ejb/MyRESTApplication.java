package py.edu.uca.intercajas.server.ejb;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/*
 * Esto habilita el servicio rest en WildFly
 */
@ApplicationPath("/rest")
public class MyRESTApplication extends Application {

}
