package py.edu.uca.intercajas.client.beneficiario.events;

import py.edu.uca.intercajas.server.entity.Beneficiario;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class BeneficiarioChangedEvent extends GwtEvent<BeneficiarioChangedEvent.Handler> {

	public static Type<Handler> TYPE = new Type<Handler>();

	Beneficiario beneficiarioSelected;
	
	public interface Handler extends EventHandler {
		void selected(Beneficiario beneficiarioSelected);
	}
	
	public  BeneficiarioChangedEvent(Beneficiario beneficiarioSelected) {
		this.beneficiarioSelected = beneficiarioSelected;
	}
	
	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.selected(beneficiarioSelected);
	}

}
