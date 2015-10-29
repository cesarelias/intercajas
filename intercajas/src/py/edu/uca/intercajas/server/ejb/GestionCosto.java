package py.edu.uca.intercajas.server.ejb;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import py.edu.uca.intercajas.server.entity.Costo;
import py.edu.uca.intercajas.shared.UnknownException;

@Stateless
public class GestionCosto {
	private static final Logger LOG = Logger
			.getLogger(GestionCosto.class.getName());
	
	

	@PersistenceContext
	EntityManager em;
	

	public Costo find(Long id) {
		return em.find(Costo.class, id);
	}
	
	public Boolean insertarCosto(Costo costo)
			throws UnknownException {
		LOG.info("insertarCosto Bean llamado");
		try {
			em.persist(costo);
			LOG.info("Costo persisted");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info(e.getMessage());
			throw new UnknownException(e.getMessage());
		}
		return true;
	}

	public static Boolean insertarCosto(String nombre,String correo)
			throws UnknownException {
		LOG.info("insertarCosto String llamado");
//		Costo bean = new Costo();
//		bean.setNombre(nombre);
//		bean.setCorreo(correo);		
//		bean.setOperacion("I");
//		bean.setVersion((new Date()).getTime());
//		BeanParametro parametro = new BeanParametro();
//		parametro.setBean(bean);
//		parametro.setTipoOperacion(bean.getOperacion());
//		try {
//			tx.begin();
//			LogicCosto logic = new LogicCosto(pm);
//			Boolean resultado = logic.mantenimiento(parametro);
//			if (resultado) {
//				tx.commit();
//				return true;
//			} else {
//				tx.rollback();
//				return false;
//			}
//		} catch (Exception ex) {
//			LOG.warning(ex.getMessage());
//			LOG.info(ex.getLocalizedMessage());
//			throw new UnknownException(ex.getMessage());
//		} finally {
////			if (!pm.isClosed()) {
////				if (tx.isActive()) {
////					tx.rollback();
////				}
////				pm.close();
//			}
//		}
		return true;
	}
	
	public Boolean actualizarCosto(Costo costo)
			throws UnknownException {

		LOG.info("actualizarCosto Bean llamado");
//		if (costo.getId() == null) {
//			LOG.info("id es nulo");
//		} else {
//			LOG.info("id ya no es nulo");	
//		}
//		if (costo.getOperacion().equalsIgnoreCase("A")
//				&& costo.getId() != null) {

//			Log.info("entro aki");
//			LOG.info("no hacemos nada, solo mensaje "+ costo.getNombre());
			em.merge(costo);

//		}
			
//			try {
//				tx.begin();
//				LogicCosto logic = new LogicCosto(pm);
//				Boolean resultado = logic.mantenimiento(parametro);
//				if (resultado) {
//					tx.commit();
//					return true;
//				} else {
//					tx.rollback();
//					return false;
//				}
//			} catch (Exception ex) {
//				LOG.warning(ex.getMessage());
//				LOG.info(ex.getLocalizedMessage());
//				throw new UnknownException(ex.getMessage());
//			} finally {
////				if (!pm.isClosed()) {
////					if (tx.isActive()) {
////						tx.rollback();
////					}
////					pm.close();
////				}
//			}
//		} else {
//			throw new UnknownException("Verifique Catalogo de Servicio");
//		}
		return true;
	}

	public static Boolean actualizarCosto(Long idCosto,
			String nombre,String correo) throws UnknownException {
		LOG.info("actualizarCosto por id");
//		Costo bean = new Costo();
//		bean.setIdCosto(idCosto);
//		bean.setNombre(nombre);
//		bean.setCorreo(correo);
//		bean.setOperacion("A");
//		bean.setVersion((new Date()).getTime());
//		BeanParametro parametro = new BeanParametro();
//		parametro.setBean(bean);
//		parametro.setTipoOperacion(bean.getOperacion());
//		try {
//			tx.begin();
//			LogicCosto logic = new LogicCosto(pm);
//			Boolean resultado = logic.mantenimiento(parametro);
//			if (resultado) {
//				tx.commit();
//				return true;
//			} else {
//				tx.rollback();
//				return false;
//			}
//		} catch (Exception ex) {
//			LOG.warning(ex.getMessage());
//			LOG.info(ex.getLocalizedMessage());
//			throw new UnknownException(ex.getMessage());
//		} finally {
////			if (!pm.isClosed()) {
////				if (tx.isActive()) {
////					tx.rollback();
////				}
////				pm.close();
////			}
//		}
		return true;
	}

	public static Boolean eliminarCosto(Costo bean)
			throws UnknownException {
//		if (bean.getOperacion().equalsIgnoreCase("E")
//				&& bean.getIdCosto() != null) {
//			BeanParametro parametro = new BeanParametro();
//			parametro.setId(bean.getIdCosto());
//			parametro.setBean(bean);
//			parametro.setTipoOperacion(bean.getOperacion());
//			try {
//				tx.begin();
//				LogicCosto logic = new LogicCosto(pm);
//				Boolean resultado = logic.mantenimiento(parametro);
//				if (resultado) {
//					tx.commit();
//					return true;
//				} else {
//					tx.rollback();
//					return false;
//				}
//			} catch (Exception ex) {
//				LOG.warning(ex.getMessage());
//				LOG.info(ex.getLocalizedMessage());
//				throw new UnknownException(ex.getMessage());
//			} finally {
//				if (!pm.isClosed()) {
////					if (tx.isActive()) {
////						tx.rollback();
////					}
////					pm.close();
//				}
//			}
//		} else {
//			throw new UnknownException("Verifique Catalogo de Servicio");
//		}
		return true;
	}

	public static Boolean eliminarCosto(Long idCosto)
			throws UnknownException {
//		Costo bean = new Costo();
//		bean.setIdCosto(idCosto);
//		bean.setOperacion("E");
//		BeanParametro parametro = new BeanParametro();
//		parametro.setId(bean.getIdCosto());
//		parametro.setBean(bean);
//		parametro.setTipoOperacion(bean.getOperacion());
//		try {
//			tx.begin();
//			LogicCosto logic = new LogicCosto(pm);
//			Boolean resultado = logic.mantenimiento(parametro);
//			if (resultado) {
//				tx.commit();
//				return true;
//			} else {
//				tx.rollback();
//				return false;
//			}
//		} catch (Exception ex) {
//			LOG.warning(ex.getMessage());
//			LOG.info(ex.getLocalizedMessage());
//			throw new UnknownException(ex.getMessage());
//		} finally {
////			if (!pm.isClosed()) {
////				if (tx.isActive()) {
////					tx.rollback();
////				}
////				pm.close();
//			}
//		}
		return true;
	}

	public List<Costo> listarCosto()
			throws UnknownException {

		LOG.info("listarCosto llamado");
		return em.createQuery("from Costo", Costo.class).getResultList();
		
	}
	
	public List<Costo> listarCosto(String correo)
			throws UnknownException {

		LOG.info("listarCosto STRINg correo llamado");
		return em.createQuery("from Costo where correo = :correo",Costo.class).setParameter("correo", correo).getResultList();
				
	}
	

	public static Costo getCosto(Long idCosto)
			throws UnknownException {

//		try {
//			LogicCosto logic = new LogicCosto(pm);
//			Costo resultado = (Costo) logic
//					.getBean(idCosto);
//			return resultado;
//		} catch (Exception ex) {
//			LOG.warning(ex.getMessage());
//			LOG.info(ex.getLocalizedMessage());
//			throw new UnknownException(ex.getMessage());
//		} finally {
////			if (!pm.isClosed()) {
////				pm.close();
////			}
//		}

		
		return null;
	}

}
