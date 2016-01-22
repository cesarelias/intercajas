package py.edu.uca.intercajas.client.solicitud.events;

import py.edu.uca.intercajas.shared.entity.TiempoServicioDeclarado;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class PeriodoAporteDeclaradoChangedEvent extends GwtEvent<PeriodoAporteDeclaradoChangedEvent.Handler> {

	public static Type<Handler> TYPE = new Type<Handler>();

	TiempoServicioDeclarado periodoAporteDeclarado;
	
	public interface Handler extends EventHandler {
		void selected(TiempoServicioDeclarado periodoAporteDeclarado);
	}
	
	public  PeriodoAporteDeclaradoChangedEvent(TiempoServicioDeclarado periodoAporteDeclarado) {
		this.periodoAporteDeclarado = periodoAporteDeclarado;
	}
	
	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.selected(periodoAporteDeclarado);
	}

}
