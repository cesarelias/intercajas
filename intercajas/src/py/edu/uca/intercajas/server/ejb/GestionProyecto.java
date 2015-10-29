package py.edu.uca.intercajas.server.ejb;

import java.util.Date;
import java.util.logging.Logger;

import py.edu.uca.intercajas.server.entity.Proyecto;
import py.edu.uca.intercajas.shared.UnknownException;


public class GestionProyecto {
	private static final Logger LOG = Logger
			.getLogger(GestionProyecto.class.getName());
	
	public static Boolean insertarProyecto(Proyecto bean)
			throws UnknownException {
//		if (bean.getOperacion().equalsIgnoreCase("I")
//				&& bean.getIdProyecto() == null) {
//			BeanParametro parametro = new BeanParametro();			
//			parametro.setBean(bean);
//			parametro.setTipoOperacion(bean.getOperacion());
//			PersistenceManager pm = null;
//			Transaction tx = null;
//			try {
//				pm = PMF.getPMF().getPersistenceManager();
//				tx = pm.currentTransaction();
//				tx.begin();
//				LogicProyecto logic = new LogicProyecto(pm);
//				Boolean resultado = logic.mantenimiento(parametro);
//				if (resultado) {
//					tx.commit();
//					pm.close();
//					return true;
//				} else {
//					tx.rollback();
//					pm.close();
//					return false;
//				}
//			} catch (Exception ex) {
//				LOG.warning(ex.getMessage());
//				LOG.info(ex.getLocalizedMessage());
//				throw new UnknownException(ex.getMessage());
//			} finally {
//				if (!pm.isClosed()) {
//					if (tx.isActive()) {
//						tx.rollback();
//					}
//					pm.close();
//				}
//			}
//		} else {
//			throw new UnknownException("Verifique Catalogo de Servicio");
//		}
		return true;
	}
	
	public static Boolean insertarProyecto(
			String nombre,
			String descripcion,
			Date fecha,
			Double presupuesto,
			String correo)
			throws UnknownException {
//		Proyecto bean = new Proyecto();
//		bean.setNombre(nombre);
//		bean.setDescripcion(descripcion);
//		bean.setFecha(fecha);
//		bean.setPresupuesto(presupuesto);
//		bean.setGasto(0.0);
//		bean.setValorResidual(presupuesto);
//		bean.setCorreo(correo);		
//		bean.setOperacion("I");
//		bean.setVersion((new Date()).getTime());
//		BeanParametro parametro = new BeanParametro();
//		parametro.setBean(bean);
//		parametro.setTipoOperacion(bean.getOperacion());
//		PersistenceManager pm = null;
//		Transaction tx = null;
//		try {
//			pm = PMF.getPMF().getPersistenceManager();
//			tx = pm.currentTransaction();
//			tx.begin();
//			LogicProyecto logic = new LogicProyecto(pm);
//			Boolean resultado = logic.mantenimiento(parametro);
//			if (resultado) {
//				tx.commit();
//				pm.close();
//				return true;
//			} else {
//				tx.rollback();
//				pm.close();
//				return false;
//			}
//		} catch (Exception ex) {
//			LOG.warning(ex.getMessage());
//			LOG.info(ex.getLocalizedMessage());
//			throw new UnknownException(ex.getMessage());
//		} finally {
//			if (!pm.isClosed()) {
//				if (tx.isActive()) {
//					tx.rollback();
//				}
//				pm.close();
//			}
//		}
		return true;
	}
	
}
