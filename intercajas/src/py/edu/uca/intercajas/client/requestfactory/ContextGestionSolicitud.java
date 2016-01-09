package py.edu.uca.intercajas.client.requestfactory;

import java.util.List;

import py.edu.uca.intercajas.server.ejb.BeanLocator;
import py.edu.uca.intercajas.server.ejb.GestionSolicitud;
import py.edu.uca.intercajas.server.entity.Caja;
import py.edu.uca.intercajas.server.entity.Empleador;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value=GestionSolicitud.class, locator=BeanLocator.class)
public interface ContextGestionSolicitud extends RequestContext{
	Request<PeriodoAporteDeclaradoProxy> findPeriodoAporteDeclarado(Long id);
	Request<SolicitudProxy> find (Long id);
	Request<Void> nuevoSolicitudTitular (SolicitudTitularProxy  solicitudTitular);
	Request<List<CajaProxy>> getCajas();
	Request<List<EmpleadorProxy>> getEmpleador(Long caja_id);
	
	
	Request<List<EmpleadorProxy>> findEmpleadorByNombre(String nombre);
	Request<Long> insertarEmpleador(EmpleadorProxy empleador);
	Request<EmpleadorProxy> findEmpleadorById(Long id);
	
	Request<CajaProxy> findCajaById(Long id);
	
}
