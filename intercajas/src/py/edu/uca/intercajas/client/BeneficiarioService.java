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

import com.google.gwt.core.client.GWT;

import py.edu.uca.intercajas.server.entity.Beneficiario;
import py.edu.uca.intercajas.server.entity.SolicitudTitular;
import py.edu.uca.intercajas.shared.domain.BeneficiarioClient;

//@Path("/rest/beneficiarios")
public interface BeneficiarioService extends RestService {

	@GET
	public void getAll(MethodCallback<List<Beneficiario>> lista);
	
	@POST
	@Path("nuevo")
	public void nuevo(Beneficiario beneficiario,  MethodCallback<Long> callback);
	
	@POST
	@Path("actualizar")
	public void actualizarBeneficiario(Beneficiario beneficiario,  MethodCallback<Void> callback);
	
	@POST
	@Path("beneficiario")
	public void getById(Long id, MethodCallback<Beneficiario> callback);
	
	@GET
	@Path("findByNombresDocs")
	public void findByNombresDocs(
			@QueryParam("nombresDocs") String nombresDocs,
			@QueryParam("startRow") int startRow,
			@QueryParam("maxResults") int maxResults,
			MethodCallback<List<Beneficiario>> callback);
	
	@Path("solicitud")
	@POST
	@Consumes("application/json")
	public void nuevoSolicitudTitular(SolicitudTitular solicitudTitular, MethodCallback<Void> callback);
	
	/**
     * Utility class to get the instance of the Rest Service
     */
    public static final class Util {

        private static BeneficiarioService instance;

        public static final BeneficiarioService get() {
            if (instance == null) {
                instance = GWT.create(BeneficiarioService.class);
                ((RestServiceProxy) instance).setResource(new Resource("rest/beneficiarios"));
            }
            return instance;
        }

        private Util() {
            // Utility class should not be instantiated
        }
        
    }
	
}
