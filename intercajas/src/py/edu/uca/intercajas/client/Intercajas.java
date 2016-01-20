package py.edu.uca.intercajas.client;

import java.util.logging.Logger;

import org.fusesource.restygwt.client.Defaults;

import py.edu.uca.intercajas.client.menumail.MenuMail;
import py.edu.uca.intercajas.client.solicitud.SolicitudTitularEditorWorkFlow;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class Intercajas implements EntryPoint {

	private final SimpleEventBus eventBus = new SimpleEventBus();
	private static final Logger log = Logger.getLogger(Intercajas.class.getName());

	private HandlerRegistration r;
	
	public void onModuleLoad() {

		Defaults.setDateFormat(null);
		
		 // Add remote logging handler
	    Logger.getLogger("").addHandler(new ErrorDialog().getHandler());

	    
//		DecoratorPanel panel = new DecoratorPanel();
//		UILoginImpl login = new UILoginImpl();
//		panel.add(login);
//		RootPanel.get().add(panel);

//	    log.log(Level.SEVERE, "aqui esta mi mensaje de mierda");
		
//		Para iniciar en esta ventana - desarrollo
//		RootPanel.get().remove(0);
		new MenuMail();
		
	}
		
		
	public void removeH() {
		r.removeHandler();
	}

	
}