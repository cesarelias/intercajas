package py.edu.uca.intercajas.client;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Response;

import py.edu.uca.intercajas.client.beneficiario.BeneficiarioEditorWorkFlow;
import py.edu.uca.intercajas.client.beneficiario.ListaBeneficiarios;
import py.edu.uca.intercajas.client.beneficiario.ListaBeneficiarios2;
import py.edu.uca.intercajas.client.beneficiario.TipoDocumentoEditor;
import py.edu.uca.intercajas.client.menumail.MenuMail;
import py.edu.uca.intercajas.client.requestfactory.BeneficiarioProxy;
import py.edu.uca.intercajas.client.requestfactory.ContextGestionBeneficiario;
import py.edu.uca.intercajas.client.requestfactory.ContextGestionCosto;
import py.edu.uca.intercajas.client.requestfactory.DireccionProxy;
import py.edu.uca.intercajas.client.requestfactory.DocumentoIdentidadProxy;
import py.edu.uca.intercajas.client.requestfactory.FactoryGestion;
import py.edu.uca.intercajas.server.ejb.GestionBeneficiario;
import py.edu.uca.intercajas.server.entity.enums.TipoDocumentoIdentidad;
import py.edu.uca.intercajas.shared.UnknownException;

import com.google.gwt.dom.client.Style.Unit;
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
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryLogHandler;
import com.google.web.bindery.requestfactory.shared.LoggingRequest;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.Violation;

public class Intercajas implements EntryPoint {

	private static final EventBus EVENTBUS = new SimpleEventBus();
	private static final Logger log = Logger.getLogger(Intercajas.class.getName());
	private static final FactoryGestion FACTORY  = GWT.create(FactoryGestion.class);

	private BeneficiarioProxy beneficiario;
	
	public void onModuleLoad() {

	    GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
	        public void onUncaughtException(Throwable e) {
	          //log.log(Level.SEVERE, e.getMessage(), e);
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

//		DecoratorPanel panel = new DecoratorPanel();
//		UILoginImpl login = new UILoginImpl();
//		panel.add(login);
//		RootPanel.get().add(panel);

//	    log.log(Level.SEVERE, "aqui esta mi mensaje de mierda");
		
//		Para iniciar en esta ventana - desarrollo
//		RootPanel.get().remove(0);
//		new MenuMail();
//		new DynaTableRf().mostrar(new Mail());
//		new UIBeneficiario().mostrar(new Mail());
		
		final ContextGestionBeneficiario context = FACTORY.contextGestionBeneficiario();
		FACTORY.initialize(EVENTBUS);

		
		RootLayoutPanel.get().add(new ListaBeneficiarios(EVENTBUS, FACTORY,10 ));
	    //Este es el INSERT
//		BeneficiarioProxy beneficiario = context.create(BeneficiarioProxy.class);
//		DocumentoIdentidadProxy docProxy = context.create(DocumentoIdentidadProxy.class);
//		DireccionProxy dirProxy = context.create(DireccionProxy.class);
//
//		docProxy.setTipoDocumento(TipoDocumentoIdentidad.CEDULA);
//		beneficiario.setDocumento(docProxy);
//		beneficiario.setDireccion(dirProxy);
//	    
//		new BeneficiarioEditorWorkFlow().create(beneficiario, context, FACTORY);
		
	    //Este es el update
//	    context.find(2L).fire(new Receiver<BeneficiarioProxy>() {
//			@Override
//			public void onSuccess(BeneficiarioProxy response) {
//				new BeneficiarioEditorWorkFlow().edit(response, context, FACTORY);
//			}
//		});
		
	}

}