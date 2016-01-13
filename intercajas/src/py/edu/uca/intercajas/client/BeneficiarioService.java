package py.edu.uca.intercajas.client;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import py.edu.uca.intercajas.shared.domain.BeneficiarioClient;

@Path("/rest/beneficiarios")
public interface BeneficiarioService extends RestService {

	@GET
	public void getAll(MethodCallback<List<BeneficiarioClient>> lista);
	
	@POST
	@Path("beneficiario")
	public void insert(BeneficiarioClient beneficiario,  MethodCallback<Void> callback);
	
}
