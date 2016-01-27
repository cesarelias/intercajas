package py.edu.uca.intercajas.client.solicitud.events;

import py.edu.uca.intercajas.shared.entity.Solicitud;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class SolicitudCreatedEvent extends GwtEvent<SolicitudCreatedEvent.Handler> {

	public static Type<Handler> TYPE = new Type<Handler>();

	Solicitud solicitud;
	
	public interface Handler extends EventHandler {
		void created(Solicitud solicitud);
	}
	
	public  SolicitudCreatedEvent(Solicitud solicitud) {
		this.solicitud = solicitud;
	}
	
	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.created(solicitud);
	}

}
