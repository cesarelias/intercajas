package py.edu.uca.intercajas.client;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.jws.WebParam;
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

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestService;
import org.fusesource.restygwt.client.RestServiceProxy;
import org.fusesource.restygwt.client.TextCallback;

import py.edu.uca.intercajas.shared.BandejaParam;
import py.edu.uca.intercajas.shared.ConsultaEstadoMensaje;
import py.edu.uca.intercajas.shared.ConsultaEstadoSolicitudBeneficiario;
import py.edu.uca.intercajas.shared.NuevaAnulacion;
import py.edu.uca.intercajas.shared.NuevaAutorizacion;
import py.edu.uca.intercajas.shared.NuevaSolicitud;
import py.edu.uca.intercajas.shared.NuevoConcedido;
import py.edu.uca.intercajas.shared.NuevoDenegado;
import py.edu.uca.intercajas.shared.NuevoReconocimientoTiempoServicio;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Beneficiario;
import py.edu.uca.intercajas.shared.entity.Caja;
import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
import py.edu.uca.intercajas.shared.entity.Destino;
import py.edu.uca.intercajas.shared.entity.Empleador;
import py.edu.uca.intercajas.shared.entity.Finiquito;
import py.edu.uca.intercajas.shared.entity.Concedido;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.Solicitud;
import py.edu.uca.intercajas.shared.entity.SolicitudBeneficiario;
import py.edu.uca.intercajas.shared.entity.Usuario;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dev.generator.ast.MethodCall;
import com.google.gwt.dev.json.JsonString;
import com.google.gwt.json.client.JSONString;

//@Path("/rest/beneficiarios")
public interface BeneficiarioService extends RestService {

	@GET
	public void getAll(MethodCallback<List<Beneficiario>> lista);
	
	@POST
	@Path("beneficiario/nuevo")
	public void nuevoBeneficiario(Beneficiario beneficiario,  MethodCallback<Void> callback);
	
	@POST
	@Path("beneficiario/actualizar")
	public void actualizarBeneficiario(Beneficiario beneficiario,  MethodCallback<Void> callback);
	
	@GET
	@Path("beneficiario/{id}")
	public void findBenediciarioById(@PathParam("id") Long id, MethodCallback<Beneficiario> callback);
	
	@GET
	@Path("beneficiario/findByNombresDocs")
	public void findBeneficiarioByNombresDocs(
			@QueryParam("nombresDocs") String nombresDocs,
			@QueryParam("startRow") int startRow,
			@QueryParam("maxResults") int maxResults,
			MethodCallback<List<Beneficiario>> callback);
	
	@Path("solicitud/nuevo")
	@POST
	@Consumes("application/json")
	public void nuevoSolicitud(NuevaSolicitud nuevaSolicitud, MethodCallback<Void> callback);
	
	
	//esto es para otro lado
	@Path("empleador/findBycaja_id")
	@GET
	@Produces("application/json")
	public void findEmpleadorByCajaId(@QueryParam("caja_id") Long caja_id, MethodCallback<List<Empleador>> listaEmpleadores);
	
	
	@Path("caja/findAll")
	@GET
	@Produces("application/json")
	public void findCajaAll(MethodCallback<List<Caja>> listaCajas);
	
	
	@Path("mensaje/findAllPending")
	@GET
	@Produces("application/json")
	public void mensajeFindAllPending(@QueryParam("startRow") int startRow, @QueryParam("maxResults") int maxResults, MethodCallback<List<Mensaje>> mensajes);

	
	////BANDEJA
	@Path("destino/findMisPendientes")
	@POST
	@Produces("application/json")
	//public void findMisPendientes(@QueryParam("startRow") int startRow, @QueryParam("maxResults") int maxResults, @QueryParam("beneficiario_id") Long beneficiario_id, @QueryParam("remitente_id") Long remitente_id, MethodCallback<List<Destino>> destinos);
	public void findMisPendientes(BandejaParam parametros, MethodCallback<List<Destino>> destinos);
	
	@Path("destino/findMisFiniquitados")
	@GET
	@Produces("application/json")
	public void findMisFiniquitados(@QueryParam("startRow") int startRow, @QueryParam("maxResults") int maxResults, MethodCallback<List<Destino>> destinos);
	
	@Path("destino/findPendientes")
	@GET
	@Produces("application/json")
	public void findPendientes(@QueryParam("startRow") int startRow, @QueryParam("maxResults") int maxResults, MethodCallback<List<Destino>> destinos);
	
	@Path("destino/findFiniquitados")
	@GET
	@Produces("application/json")
	public void findFiniquitados(@QueryParam("startRow") int startRow, @QueryParam("maxResults") int maxResults, MethodCallback<List<Destino>> destinos);
	
	@Path("destino/findAnulados")
	@GET
	@Produces("application/json")
	public void findAnulados(@QueryParam("startRow") int startRow, @QueryParam("maxResults") int maxResults, MethodCallback<List<Destino>> destinos);
	
	//FIN BANDEJA

	@Path("adjunto/findByMensajeId")
	@GET
	@Produces("application/json")
	public void adjuntoFindByMensajeId(@QueryParam("mensaje_id") Long mensaje_id, MethodCallback<List<Adjunto>> adjuntos);

	@Path("solicitud/")
	@GET
	@Produces("application/json")
	public void findSolicitudById(@QueryParam("id") Long id, MethodCallback<Solicitud > solicitud);

	
	@Path("cajaDeclarada/findBySolicitudId")
	@GET
	@Produces("application/json")
	public void findCajaDeclaradaBySolicitudId(@QueryParam("solicitud_id") Long solicitud_id, MethodCallback<List<CajaDeclarada>> cajasDeclaradas);
	
	
	@Path("solicitud/nuevoReconocimientoTiempoServicio")
	@POST
	@Consumes("application/json")
	public void nuevoReconocimientoTiempoServicio(NuevoReconocimientoTiempoServicio nuevoReconocimientoTiempoServicio, MethodCallback<Void> callback);
		
	@Path("cajaDeclarada/findCajaDeclaraadBySolicitudIdAndCurrentUser")
	@GET
	@Produces("application/json")
	public void findCajaDeclaraadBySolicitudIdAndCurrentUser(@QueryParam("solicitud_id") Long solicitud_id, MethodCallback<CajaDeclarada> cajaDeclarada);
	
	@Path("finiquito/denegar")
	@POST
	@Consumes("application/json")
	public void denegar(NuevoDenegado nuevoDenegado, MethodCallback<Void> callback);


	@Path("solicitud/findSolicitudBeneficioBySolicitudId")
	@GET
	@Produces("application/json")
	public void findSolicitudBeneficioBySolicitudId(@QueryParam("id") Long id, MethodCallback<List<SolicitudBeneficiario>> ballback);

	@Path("finiquito/conceder")
	@POST
	@Consumes("application/json")
	public void conceder(NuevoConcedido nuevoConcedido, MethodCallback<Void> callback);	

	
	@Path("mensaje/autorizar")
	@POST
	@Consumes("application/json")
	public void autorizar(NuevaAutorizacion nuevaAutorizacion, MethodCallback<Void> callback);
	
	@Path("mensaje/anular")
	@POST
	@Consumes("application/json")
	public void anular(NuevaAnulacion nuevaAnulacion, MethodCallback<Void> callback);	
	
	
	@Path("usuario/findByDescripcionNombre")
	@GET
	@Produces("application/json")
	public void findUsuarioByDescripcionNombre(@QueryParam("descripcionNombre") String descripcionNombre,
   	                              @QueryParam("startRow") int startRow,
								  @QueryParam("maxResults") int maxResults, MethodCallback<List<Usuario> > usuarios);	

	@Path("usuario/actualizar")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public void actualizarUsuario(Usuario usuario, MethodCallback<Void> callback); 
	
	@Path("usuario/restablecerContrasena")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public void restablecerContrasena(@QueryParam("nombre") String nombre, @QueryParam("correo") String correo, MethodCallback<Void> callback);


	@Path("solicitud/consultEstadoMensaje")
	@GET
	@Produces("application/json")
	public void consultEstadoMensaje(@QueryParam("mensaje_id") Long mensaje_id, MethodCallback<ConsultaEstadoMensaje> consultaEstadoMensaje);

	@Path("mensaje/detalleAutorizarMensaje")
	@GET
	public void detalleAutorizarMensaje(@QueryParam("mensaje_id") Long mensaje_id, TextCallback detalleHTML);
	
	@Path("empleador/findEmpleadorByNombre")
	@GET
	@Produces("application/json")
	public void findEmpleadorByNombre(@QueryParam("nombre") String nombre, @QueryParam("startRow") int startRow, @QueryParam("maxResults") int maxResults, MethodCallback<List<Empleador>> listaEmpleador);
	

	@Path("empleador/actualizar")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public void actualizarEmpleador(Empleador empleador, MethodCallback<Void> callback); 
	
	@Path("caja/findByNombreSigla")
	@GET
	@Produces("application/json")
	public void findCajaByNombreSigla(@QueryParam("nombreSigla") String nombreSigla,
			                            @QueryParam("startRow") int startRow,
										@QueryParam("maxResults") int maxResults,
										MethodCallback<List<Caja>> cajas);
	
	@Path("caja/eliminar")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public void eliminarCaja(@WebParam(name="caja_id") Long caja_id, MethodCallback<Void> callback);
	
	@Path("caja/actualizar")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public void actualizarCaja(Caja caja, MethodCallback<Void> callback);	
	
	@Path("beneficiario/eliminar")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public void eliminarBeneficiario(@WebParam(name="beneficiario_id") Long beneficiario_id, MethodCallback<Void> callback);	
	
	@Path("empleador/eliminar")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public void eliminarEmpleador(@WebParam(name="empleador_id") Long empleador_id, MethodCallback<Void> callback); 
	
	
	@Path("usuario/eliminar")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public void eliminarUsuario(@WebParam(name="usuario_id") Long usuario_id, MethodCallback<Void> callback);

	
	////REPORTES
	@Path("report/auditoriaPorUsuario")
	@GET
	@Produces("text/plain")
	public void reporteAuditoriaPorUsuario(@QueryParam(value = "desde") String desde,
			           @QueryParam(value = "hasta") String hasta,
			           @QueryParam(value = "usuario") String usuario, TextCallback archivo);
	
	@Path("report/totalizacion")
	@GET
	@Produces("text/plain")
	public void reporteTotalizacion(@QueryParam(value = "param") Long soliciutd_id, TextCallback archivo); 
	
	
	
	@Path("report/solicitudDetalle")
	@GET
	@Produces("text/plain")
	public void reporteSolicitudDetalle(@QueryParam(value = "param") Long solicitud_id, TextCallback archivo);

	@Path("report/mensaje")
	@GET
	@Produces("text/plain")
	public void reporteMensaje(@QueryParam(value = "param") Long solicitud_id, TextCallback archivo);
	
	
	//FIN REPORTES
	
	/**
     * Utility class to get the instance of the Rest Service
     */
    public static final class Util {

        private static BeneficiarioService instance;

        public static final BeneficiarioService get() {
            if (instance == null) {
                instance = GWT.create(BeneficiarioService.class);
                ((RestServiceProxy) instance).setResource(new Resource("rest"));
            }
            return instance;
        }

        private Util() {
            // Utility class should not be instantiated
        }
        
    }

	
}
