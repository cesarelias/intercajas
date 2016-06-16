package py.edu.uca.intercajas.server.ejb;


import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebParam;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import py.edu.uca.intercajas.server.GwMessage;
import py.edu.uca.intercajas.shared.UserDTO;
import py.edu.uca.intercajas.shared.entity.Auditoria;
import py.edu.uca.intercajas.shared.entity.Beneficiario;
import py.edu.uca.intercajas.shared.entity.Solicitud;
import py.edu.uca.intercajas.shared.entity.SolicitudBeneficiario;
import py.edu.uca.intercajas.shared.entity.DocumentoIdentidad.TipoDocumentoIdentidad;
import py.edu.uca.intercajas.shared.entity.Usuario;


@Path("/usuario")
@Stateless
public class UsuarioRest   {


	@PersistenceContext
	EntityManager em;
	
	@EJB
	UserLogin userLogin;
	
	@EJB
	GwMessage gwMessage;

	@Path("/test")
	@GET
	public String test() {
		System.out.println("rest working");
		return "rest working";
	}
	
	@Path("/{id}")
	@GET
	@Produces("application/json")
	public Usuario find(@PathParam("id") Long id) {
		return em.find(Usuario.class, id);
	}
	
	@Path("/findByDescripcionNombre")
	@GET
	@Produces("application/json")
	public List<Usuario> findByDescripcionNombre(@QueryParam("descripcionNombre") String descripcionNombre,
												@QueryParam("startRow") int startRow,
												@QueryParam("maxResults") int maxResults) {
		
		if (descripcionNombre == null || descripcionNombre.length() == 0) {
			descripcionNombre = "%";
		} else {
			descripcionNombre = '%' + descripcionNombre.toUpperCase() + '%';
		}
		
		if (maxResults > 500) {
			maxResults = 500;
		}
		
		return em.createQuery("select u "
				+ "              from Usuario u "
				+ "             where UPPER(u.descripcion) like :descripcionLogin "
				+ "                or UPPER(u.nombre) like :descripcionLogin"
				+ " order by u.descripcion asc"
				,Usuario.class)
				.setParameter("descripcionLogin", descripcionNombre)
				.setFirstResult(startRow)
				.setMaxResults(maxResults)
				.getResultList();
	}

	@Path("/actualizar")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public void actualizar(Usuario usuario, @Context HttpServletRequest req) { 
	
		UserDTO user = userLogin.getValidUser(req.getSession().getId());
        if (user == null) {
        	throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("Usuario no valido").build());
        }

		if (usuario.getId() == null) { //nuevo usuario
			
			//verificamos que no exista el nombreUsuario
			if (em.createQuery("select u from Usuario u where u.nombre = :nombre", Usuario.class)
					.setParameter("nombre", usuario.getNombre())
					.setMaxResults(1)
					.getResultList().size() > 0) {
				throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("No puede agregar usuario, existe el nombre de usuario").build());
			}
			
			//creamos una clave aleatoria
			int randomNum = 1000 + (int)(Math.random() * 9999); 
			//TODO falta enviar el correo!
			usuario.setClave(userLogin.MD5(String.valueOf(randomNum)));
			
			gwMessage.sendEmail(usuario.getCorreo(), "no-reply", "Nuevo Usuario", "Bienvenido al sistema Intercajas " + usuario.getDescripcion() + " su nombre de usuario es: " + usuario.getNombre() + " y su contraseña asignada es " + String.valueOf(randomNum));
	

		} else {
			
					
			
			Usuario usuarioAnterior = em.find(Usuario.class, usuario.getId());
			if (usuarioAnterior == null) {
				throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("No existe el usuario").build());
			}
			
			if (em.createQuery("select u from Usuario u where u.nombre = :nombre and id <> :id", Usuario.class)
					.setParameter("nombre", usuario.getNombre())
					.setParameter("id", usuario.getId())
					.setMaxResults(1)
					.getResultList().size() > 0) {
				throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("No puede modificar usuario, existe el nombre de usuario").build());
			}

			
			if (!usuarioAnterior.getNombre().equals(usuario.getNombre()) ||
					!usuarioAnterior.getDescripcion().equals(usuario.getDescripcion())) {
				if (em.createQuery("select s from Auditoria s where upper(s.nombreUsuario) = upper(:nombreUsuario)", Auditoria.class)
						.setParameter("nombreUsuario", usuarioAnterior.getNombre())
						.setMaxResults(1)
						.getResultList().size() > 0) {
					throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("No puede cambiar el nombre de usuario, ya registra movimientos en auditoria").build());
				}
			}
			
			
			
			gwMessage.sendEmail(usuario.getCorreo(), "no-reply", "Cambio en cuenta", "Su cuenta de usuario sufrio un cambio Descripcion: " + usuario.getDescripcion() + " nombre de usuario : " + usuario.getNombre() + " Activo? " + usuario.isActivo());
			
			userLogin.registrarAuditoria(user, "Cambio datos usuario (anterior/nuevo) " +
					" descripcion: " + usuarioAnterior.getDescripcion() + "/" + usuario.getDescripcion() +
					" activo? " + usuarioAnterior.isActivo() + "/" + usuario.isActivo());
		}
		
		em.merge(usuario);
			
		
		
	}

	@Path("/restablecerContrasena")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public void restablecerContrasena(@QueryParam("nombre") String nombre, @QueryParam("correo") String correo)  {
		
		Usuario usuario = null;
		try {
			usuario = 
				em.createQuery("select u from Usuario u where nombre = :nombre and correo = :correo", Usuario.class)
						.setParameter("correo", correo)
						.setParameter("nombre", nombre)
						.getSingleResult();
		} catch (NoResultException e) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("Nombre de usuario / correo no valido").build());
		}
		
		//creamos una clave aleatoria
		int randomNum = 1000 + (int)(Math.random() * 9999); 
		
		usuario.setClave(userLogin.MD5(String.valueOf(randomNum)));
		
		//TODO falta enviar el correo!
		gwMessage.sendEmail(usuario.getCorreo(), "no-reply", "Cambio de contraseña", " Su contraseña asignada es " + String.valueOf(randomNum));
		
				
		em.persist(usuario);
		
	}
	
	@Path("/eliminar")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public void eliminar(@WebParam(name="usuario_id") Long usuario_id)  {
		
		Usuario u = em.find(Usuario.class, usuario_id);
		
		if (u == null) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("No existe el usuario").build());
		}
		
		if (em.createQuery("select s from Auditoria s where nombreUsuario = :nombreUsuario", Auditoria.class)
				.setParameter("nombreUsuario", u.getNombre())
				.setMaxResults(1)
				.getResultList().size() > 0) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("No puede eliminar usuario, existe en auditoria").build());
		}
		
		em.remove(u);
		
	}

	
}