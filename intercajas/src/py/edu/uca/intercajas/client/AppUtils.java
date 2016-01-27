package py.edu.uca.intercajas.client;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class AppUtils {
	    public static EventBus EVENT_BUS = GWT.create(SimpleEventBus.class);
}
