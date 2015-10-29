package py.edu.uca.intercajas.client.requestfactory;

import java.util.Date;

import py.edu.uca.intercajas.server.entity.EntityLocator;
import py.edu.uca.intercajas.server.entity.Proyecto;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value=Proyecto.class,locator=EntityLocator.class)
public interface ProyectoProxy extends EntityProxy{
	public Long getIdProyecto();
	
	public void setIdProyecto(Long idProyecto);
	
	public String getNombre();
	
	public void setNombre(String nombre);
	
	public String getDescripcion();
	
	public void setDescripcion(String descripcion);
	
	public Date getFecha();
	
	public void setFecha(Date fecha);
	
	public Double getPresupuesto();
	
	public void setPresupuesto(Double presupuesto);
	
	public Double getGasto();
	
	public void setGasto(Double gasto);
	
	public Double getValorResidual();
	
	public void setValorResidual(Double valorResidual);
	
	public String getCorreo();
	
	public void setCorreo(String correo);
	
	public Long getVersion();
	
	public void setVersion(Long version);
	
	public String getOperacion();
	
	public void setOperacion(String operacion);
	
}
