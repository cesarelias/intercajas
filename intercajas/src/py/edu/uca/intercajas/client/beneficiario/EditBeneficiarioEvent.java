package py.edu.uca.intercajas.client.beneficiario;

import py.edu.uca.intercajas.client.requestfactory.BeneficiarioProxy;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.requestfactory.shared.RequestContext;

public class EditBeneficiarioEvent extends GwtEvent<EditBeneficiarioEvent.Handler> {

	public static final Type<Handler> TYPE = new Type<Handler>();
	
	public interface Handler extends EventHandler {
		void startEdit(BeneficiarioProxy beneficiario, RequestContext requestContext);
	}

	private final BeneficiarioProxy beneficiario;
	private final RequestContext request;
	
	public EditBeneficiarioEvent(BeneficiarioProxy beneficiario){
		this(beneficiario, null);
	}
	
	public EditBeneficiarioEvent(BeneficiarioProxy beneficiario, RequestContext request) {
		this.beneficiario = beneficiario;
		this.request = request;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.startEdit(beneficiario, request);
	}
	

}
