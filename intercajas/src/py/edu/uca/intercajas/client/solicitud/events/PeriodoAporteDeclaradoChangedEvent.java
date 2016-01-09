package py.edu.uca.intercajas.client.solicitud.events;

import py.edu.uca.intercajas.client.requestfactory.PeriodoAporteDeclaradoProxy;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class PeriodoAporteDeclaradoChangedEvent extends GwtEvent<PeriodoAporteDeclaradoChangedEvent.Handler> {

	public static Type<Handler> TYPE = new Type<Handler>();

	PeriodoAporteDeclaradoProxy periodoAporteDeclaradoProxy;
	
	public interface Handler extends EventHandler {
		void selected(PeriodoAporteDeclaradoProxy periodoAporteDeclaradoProxy);
	}
	
	public  PeriodoAporteDeclaradoChangedEvent(PeriodoAporteDeclaradoProxy periodoAporteDeclaradoProxy) {
		this.periodoAporteDeclaradoProxy = periodoAporteDeclaradoProxy;
	}
	
	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.selected(periodoAporteDeclaradoProxy);
	}

}
