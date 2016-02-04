package py.edu.uca.intercajas.client.menumail;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class RefreshMailEvent extends GwtEvent<RefreshMailEvent.Handler> {

	public static Type<Handler> TYPE = new Type<Handler>();

	
	public interface Handler extends EventHandler {
		void refresh();
	}
	
	public  RefreshMailEvent() {
	}
	
	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.refresh();
	}

}
