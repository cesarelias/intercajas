package py.edu.uca.intercajas.server.ejb;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import py.edu.uca.intercajas.server.entity.Usuario;
import py.edu.uca.intercajas.shared.UnknownException;


@Stateless
public class GestionUsuario {
	private static final Logger LOG = Logger.getLogger(GestionUsuario.class
			.getName());

	@PersistenceContext
	private EntityManager em;
	
	
	public Usuario find(Long id)  {
		return em.find(Usuario.class, id);
	}
	
	public Usuario login(String correo, String clave) throws UnknownException {
		
		
		List<Usuario> usuarios = em.createQuery("from Usuario where correo = :correo and clave = :clave",Usuario.class)
				.setParameter("correo", correo)
				.setParameter("clave", clave)
				.getResultList();
		if (usuarios.size() > 0) {
			LOG.info("usuario logueado");
			return usuarios.get(0);
		} else {
			LOG.info("Usuario o clave incorrectos");
			throw new UnknownException("Usuario o clave incorrectos");
		}
	}
	public static Boolean insertarUsuario(Usuario bean) throws UnknownException {

		// if (bean.getOperacion().equalsIgnoreCase("I")
		// && bean.getCorreo() != null) {
		// BeanParametro parametro = new BeanParametro();
		// parametro.setBean(bean);
		// parametro.setTipoOperacion(bean.getOperacion());
		// PersistenceManager pm = null;
		// Transaction tx = null;
		// try {
		// pm = PMF.getPMF().getPersistenceManager();
		// tx = pm.currentTransaction();
		// tx.begin();
		// LogicUsuario logic = new LogicUsuario(pm);
		// Boolean resultado = logic.mantenimiento(parametro);
		// if (resultado) {
		// tx.commit();
		// pm.close();
		// return true;
		// } else {
		// tx.rollback();
		// pm.close();
		// return false;
		// }
		// } catch (Exception ex) {
		// LOG.warning(ex.getMessage());
		// LOG.info(ex.getLocalizedMessage());
		// throw new UnknownException(ex.getMessage());
		// } finally {
		// if (!pm.isClosed()) {
		// if (tx.isActive()) {
		// tx.rollback();
		// }
		// pm.close();
		// }
		// }
		// } else {
		// throw new UnknownException("Verifique Catalogo de Servicio");
		// }

		return true;
	}

	public static Boolean insertarUsuario(String correo, String clave,
			String nombres, String apellidos) throws UnknownException {
		// Usuario bean = new Usuario();
		// bean.setCorreo(correo);
		// bean.setClave(clave);
		// bean.setNombre(nombres);
		// bean.setApellidos(apellidos);
		// bean.setOperacion("I");
		// bean.setVersion((new Date()).getTime());
		// BeanParametro parametro = new BeanParametro();
		// parametro.setBean(bean);
		// parametro.setTipoOperacion(bean.getOperacion());
		// PersistenceManager pm = null;
		// Transaction tx = null;
		// try {
		// pm = PMF.getPMF().getPersistenceManager();
		// tx = pm.currentTransaction();
		// tx.begin();
		// LogicUsuario logic = new LogicUsuario(pm);
		// Boolean resultado = logic.mantenimiento(parametro);
		// if (resultado) {
		// tx.commit();
		// pm.close();
		// return true;
		// } else {
		// tx.rollback();
		// pm.close();
		// return false;
		// }
		// } catch (Exception ex) {
		// LOG.warning(ex.getMessage());
		// LOG.info(ex.getLocalizedMessage());
		// throw new UnknownException(ex.getMessage());
		// } finally {
		// if (!pm.isClosed()) {
		// if (tx.isActive()) {
		// tx.rollback();
		// }
		// pm.close();
		// }
		// }
		return true;
	}

}
