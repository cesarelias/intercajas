package py.edu.uca.intercajas.client;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import py.edu.uca.intercajas.client.menumail.MenuMail;
import py.edu.uca.intercajas.client.requestfactory.BeneficiarioProxy;
import py.edu.uca.intercajas.client.requestfactory.ContextGestionSolicitud;
import py.edu.uca.intercajas.client.requestfactory.FactoryGestion;
import py.edu.uca.intercajas.client.requestfactory.PeriodoAporteDeclaradoProxy;
import py.edu.uca.intercajas.client.requestfactory.SolicitudTitularProxy;
import py.edu.uca.intercajas.client.solicitud.PeriodoAporteDeclaradoEditor;
import py.edu.uca.intercajas.client.solicitud.SolicitudTitularEditorWorkFlow;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryLogHandler;
import com.google.web.bindery.requestfactory.shared.LoggingRequest;

public class Intercajas implements EntryPoint {

	private final SimpleEventBus eventBus = new SimpleEventBus();
	private static final Logger log = Logger.getLogger(Intercajas.class.getName());
	private static final FactoryGestion FACTORY  = GWT.create(FactoryGestion.class);

	private BeneficiarioProxy beneficiario;
	private HandlerRegistration r;
	
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
		new MenuMail();
//		new DynaTableRf().mostrar(new Mail());
//		new UIBeneficiario().mostrar(new Mail());
		
		final ContextGestionSolicitud context = FACTORY.contextGestionSolicitud();
		FACTORY.initialize(eventBus);

		
//		final ContextGestionPeriodoAporte con = FACTORY.contextGestionPeriodoAporte();
		
//		Window.alert(ppp.getCaja().toString());
		
//		new UIBeneficiario().mostrar(MenuMail.getMain().getWidget(0));
//		ListaBeneficiarios l = new ListaBeneficiarios(EVENTBUS, FACTORY,2);
//		l.mostrar(MenuMail.getMain().getWidget(0));
		
//		new ListaBeneficiarios(eventBus, FACTORY, 10).mostrarDialog(eventBus);
//		new ListaBeneficiarios(eventBus, FACTORY,10 ).mostrar(null, eventBus, "Beneficiarios");
		
//		new SolicitudTitularEditorWorkFlow().mostrarDialog(eventBus);
		
//		RootLayoutPanel.get().add(new ListaBeneficiarios(EVENTBU

		
		//probamos Solicitud Titular
		try {
		
		  ContextGestionSolicitud ctx = FACTORY.contextGestionSolicitud();
		  SolicitudTitularProxy solicitudTitular = ctx.create(SolicitudTitularProxy.class);
		  SolicitudTitularEditorWorkFlow b = new SolicitudTitularEditorWorkFlow(eventBus, FACTORY, ctx);
		  b.title = "Nueva solicitud titular";
		  b.mostrarDialog();
		  b.create(solicitudTitular);
		  
	    } catch (Exception e) {
	    	Window.alert(e.getMessage());
	    }
		
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
		
		
//		//Este es el test del eventBus.fire
//		Button test = new Button("test");
//		RootLayoutPanel.get().add(test);
////		
//		test.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				EVENTBUS.fireEvent(new BeneficiarioChangedEvent());
//			}
//		});
		
		//Ejemplo de registro de evento!
//	    r = eventBus.addHandler(BeneficiarioChangedEvent.TYPE, new BeneficiarioChangedEvent.Handler() {
//			@Override
//			public void selected(BeneficiarioProxy beneficiarioSelected) {
//				Window.alert("desde Intercajas: beneficiario selected: " + beneficiarioSelected.getNombres());
//				removeH();
//			}
//		});
	    
	}

	public void removeH() {
		r.removeHandler();
	}

	
}