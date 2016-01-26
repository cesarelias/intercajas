package py.edu.uca.intercajas.client;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestService;
import org.fusesource.restygwt.client.RestServiceProxy;

import py.edu.uca.intercajas.shared.NuevaSolicitudTitular;
import py.edu.uca.intercajas.shared.entity.Beneficiario;
import py.edu.uca.intercajas.shared.entity.Caja;
import py.edu.uca.intercajas.shared.entity.Empleador;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.SolicitudTitular;

import com.google.gwt.core.client.GWT;

//@Path("/rest/beneficiarios")
public interface BeneficiarioService extends RestService {

	@GET
	public void getAll(MethodCallback<List<Beneficiario>> lista);
	
	@POST
	@Path("beneficiario/nuevo")
	public void nuevoBeneficiario(Beneficiario beneficiario,  MethodCallback<Long> callback);
	
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
	
	@Path("solicitudTitular/nuevo")
	@POST
	@Consumes("application/json")
	public void nuevoSolicitudTitular(NuevaSolicitudTitular nuevaSolicitudTitular, MethodCallback<Void> callback);
	
	
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
	public void findAllPending(@QueryParam("startRow") int startRow, @QueryParam("maxResults") int maxResults, MethodCallback<List<Mensaje>> mensajes);
	
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
