package py.edu.uca.intercajas.client;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Response;

import py.edu.uca.intercajas.client.menumail.MenuMail;
import py.edu.uca.intercajas.client.requestfactory.BeneficiarioProxy;
import py.edu.uca.intercajas.client.requestfactory.ContextGestionBeneficiario;
import py.edu.uca.intercajas.client.requestfactory.ContextGestionCosto;
import py.edu.uca.intercajas.client.requestfactory.DocumentoIdentidadProxy;
import py.edu.uca.intercajas.client.requestfactory.FactoryGestion;
import py.edu.uca.intercajas.client.view.solicitud.events.EditBeneficiarioEvent;
import py.edu.uca.intercajas.server.ejb.GestionBeneficiario;
import py.edu.uca.intercajas.server.entity.enums.TipoDocumentoIdentidad;
import py.edu.uca.intercajas.shared.UnknownException;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryLogHandler;
import com.google.web.bindery.requestfactory.shared.LoggingRequest;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class Intercajas implements EntryPoint {

	private static final EventBus EVENTBUS = new SimpleEventBus();
	private static final Logger log = Logger.getLogger(Intercajas.class.getName());
	private static final FactoryGestion FACTORY  = GWT.create(FactoryGestion.class);
	
	public void onModuleLoad() {
		
	    GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
	        public void onUncaughtException(Throwable e) {
	          log.log(Level.SEVERE, e.getMessage(), e);
	        }
	      });
	    
		 // Add remote logging handler
	    RequestFactoryLogHandler.LoggingRequestProvider provider = new RequestFactoryLogHandler.LoggingRequestProvider() {
	      public LoggingRequest getLoggingRequest() {
	        return FACTORY.loggingRequest();
	      }
	    };
	    Logger.getLogger("").addHandler(new ErrorDialog().getHandler());
	    Logger.getLogger("").addHandler(
	        new RequestFactoryLogHandler(provider, Level.WARNING,
	            new ArrayList<String>()));
			
	    
	    
	    FACTORY.initialize(EVENTBUS);
	    BeneficiarioEditorWorkFlow.register(EVENTBUS, FACTORY);
	    
//		DecoratorPanel panel = new DecoratorPanel();
//		UILoginImpl login = new UILoginImpl();
//		panel.add(login);
//		RootPanel.get().add(panel);

//	    log.log(Level.SEVERE, "aqui esta mi mensaje de mierda");
		
//		Para iniciar en esta ventana - desarrollo
//		RootPanel.get().remove(0);
		new MenuMail();
//		new DynaTableRf().mostrar(new Mail());
//		new UIBeneficiario().mostrar(new Mail());
		
		ContextGestionBeneficiario context = FACTORY.contextGestionBeneficiario();
		BeneficiarioProxy beneficiario = context.create(BeneficiarioProxy.class);
		DocumentoIdentidadProxy docProxy = context.create(DocumentoIdentidadProxy.class);

		docProxy.setNumeroDocumento("123322");
		docProxy.setTipoDocumento(TipoDocumentoIdentidad.CEDULA);
		beneficiario.setDocumento(docProxy);
		beneficiario.setNombres("cesarito");
		beneficiario.setApellidos("sanabrita");

	    context.insertarBeneficiario(beneficiario).to(new Receiver<Void>() {
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert("Error al grabar" + error.getMessage());
			}
			@Override
			public void onSuccess(Void response) {
			}
		});
	    
	    EVENTBUS.fireEvent(new EditBeneficiarioEvent(beneficiario, context));
//	    new UIDialog("titulo", new HTML("detalle del mensaje"));
	    
	}
	
}