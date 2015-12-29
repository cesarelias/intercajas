package py.edu.uca.intercajas.client.beneficiario.events;

import py.edu.uca.intercajas.client.requestfactory.BeneficiarioProxy;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class BeneficiarioChangedEvent extends GwtEvent<BeneficiarioChangedEvent.Handler> {

	public static Type<Handler> TYPE = new Type<Handler>();

	BeneficiarioProxy beneficiarioSelected;
	
	public interface Handler extends EventHandler {
		void selected(BeneficiarioProxy beneficiarioSelected);
	}
	
	public  BeneficiarioChangedEvent(BeneficiarioProxy beneficiarioSelected) {
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
