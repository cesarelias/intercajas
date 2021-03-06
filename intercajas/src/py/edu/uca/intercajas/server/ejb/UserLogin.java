package py.edu.uca.intercajas.server.ejb;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.eclipse.jetty.server.Authentication.User;

import py.edu.uca.intercajas.shared.UserDTO;
import py.edu.uca.intercajas.shared.entity.Auditoria;
import py.edu.uca.intercajas.shared.entity.Caja;
import py.edu.uca.intercajas.shared.entity.Usuario;

@Singleton
public class UserLogin {

	@PersistenceContext
	private EntityManager em;
	
	private Collection<UserDTO> usuarios = new ArrayList<UserDTO>();

	//TODO debemos hacer que expire al cambiar de fecha!, es decir, en un nuevo dia, es obligatorio el re-login
	final long DURATION = 1000 * 60 * 60 * 24 * 1; //24 hora dura una sesion
	
	public UserDTO login(String name, String password, String sessionId) { 

    	try { 
	    	Usuario u = em.createQuery(
	    			  "select u "
	    			+ "  from Usuario u"
	    			+ " where u.nombre = :nombre "
	    			+ "   and u.clave = :clave "
	    			+ "   and u.activo = true",
	    			 Usuario.class)
	    			.setParameter("nombre", name)
	    			.setParameter("clave", MD5(password))
	    			.getSingleResult();

	    	UserDTO user = new UserDTO();
	    	user.setId(u.getId());
	    	user.setLoggedIn(true);
	    	user.setDescription(u.getDescripcion());
    		user.setName(name);
    		user.setSessionId(sessionId);
    		user.setExpire(new Date(System.currentTimeMillis() + DURATION));
    		user.setCaja(u.getCaja());
    		user.setTipo(u.getTipo());
    		user.setFechaLogin(new Date());
    		usuarios.add(user);
    		return user;

    	} catch (NoResultException e) {
    		return null;
    	}
    	
	}

	public UserDTO getValidUser(String sessionId) {
		
//		UserDTO user = new UserDTO();
//		Caja c = em.find(Caja.class, 1L);
//		user.setCaja(c);
//		user.setLoggedIn(true);
//		user.setName("cesar");
//		user.setDescription("Cesar Sanabria");
//		user.setExpire(new Date());
//
//		return user;
		
		Date ahora = new Date();
		Iterator<UserDTO> i = usuarios.iterator();
		while (i.hasNext()) {
		    UserDTO u = i.next(); 
			if (u.getSessionId().equals(sessionId)) {
					if(u.getExpire().after(ahora)) {
						System.out.println("usuario VALIDO!");
						return u;
					} else {
						i.remove();
					}
			}
		}
		System.out.println("usuario no VALIDO");
		return null;
	}
	
	public void logout(String sessionId) {
		System.out.println("logout!");
		Iterator<UserDTO> i = usuarios.iterator();
		UserDTO u;
		while (i.hasNext()) {
			u = i.next();
			if (u.getSessionId().equals(sessionId)) {
				i.remove();
				System.out.println("logout de : " + u.getName());
				return;
			}
		}
	}

	//TODO, falta recibir el sessionID para controlar quien esta haciendo el llamado, y que la sesion este activa 
	// y solo los Usuarios administradores pueden cambiar contraseñas de otros usuarios 
	public boolean changePassword(String name, String oldPassword, String newPassword) {
		
		try {
			Usuario u = em.createQuery(
					  " select u"
					+ "   from Usuario u "
					+ "  where u.nombre = :nombre",Usuario.class)
					.setParameter("nombre", name)
					.getSingleResult();
			
			if (MD5(oldPassword).equals(u.getClave())) {
				u.setClave(MD5(newPassword));
				em.persist(u);
				return true;
			}
			
			return false;
			
		} catch (NoResultException e) {
			return false;
		}
		
	}
	
	public String MD5(String md5) {
		
		MessageDigest m;
		try {
			m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(md5.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			String hashtext = bigInt.toString(16);
			// Now we need to zero pad it if you actually want the full 32 chars.
			while(hashtext.length() < 32 ){
				hashtext = "0"+hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
		
	}
	
	@Schedule(hour="23", persistent=false) //limpiarmos de memoria la lista de usuarios expirados una vez al dia.
	public void clearExpiredUsers() {
		Iterator<UserDTO> i = usuarios.iterator();
		Date ahora = new Date();
		UserDTO u;
		while (i.hasNext()) {
			u = i.next();
			if(u.getExpire().before(ahora)) {
				i.remove();
			}
		}
	}
	
	public void registrarAuditoria(UserDTO user, String operacion) {
		
		Auditoria a = new Auditoria();
		a.setCajaSiglas(user.getCaja().getSiglas());
		a.setFecha(new Date());
		a.setNombreUsuario(user.getName());
		a.setOperacion(operacion);
		
		em.persist(a);
		
	}
	
}
