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

	    GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
	        public void onUncaughtException(Throwable e) {
	          //log.log(Level.SEVERE, e.getMessage(), e);
	        }
	      });
	    
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
//		new DynaTableRf().mostrar(new Mail());
//		new UIBeneficiario().mostrar(new Mail());
		
		
//		final ContextGestionPeriodoAporte con = FACTORY.contextGestionPeriodoAporte();
		
//		Window.alert(ppp.getCaja().toString());
		
//		new UIBeneficiario().mostrar(MenuMail.getMain().getWidget(0));
//		ListaBeneficiarios l = new ListaBeneficiarios(EVENTBUS, FACTORY,2);
//		l.mostrar(MenuMail.getMain().getWidget(0));
		
//		new ListaBeneficiarios(eventBus, FACTORY, 10).mostrarDialog(eventBus);
//		new ListaBeneficiarios(eventBus, FACTORY,10 ).mostrar(null, eventBus, "Beneficiarios");
		
//		new SolicitudTitularEditorWorkFlow().mostrarDialog(eventBus);
		
//		RootLayoutPanel.get().add(new ListaBeneficiarios(EVENTBU
		
		
//		Defaults.setServiceRoot(com.google.gwt.core.client.GWT.getHostPageBaseURL());
		
		/*
		 * 
		 * 
		 * 
		 * 
		 *RestFull TEST with RestyGWT :-) 
		 * 
		 * 
		 * 
		 * 
		 */
		
		try { 
			
			Defaults.setDateFormat(null);

//			BeneficiarioService.Util.get().getAll(new MethodCallback<List<BeneficiarioClient>>() {
//				@Override
//				public void onSuccess(Method method, List<BeneficiarioClient> response) {
//					for (BeneficiarioClient bb : response) {
//						Window.alert(bb.getNombre());
//					}
//				}
//				@Override
//				public void onFailure(Method method, Throwable exception) {
//					Window.alert(exception.getMessage());
//				}
//			});
//			
//			BeneficiarioClient b = new BeneficiarioClient();
//			b.setId("10");
//			b.setNombre("Elias");
//
//			BeneficiarioService.Util.get().insert(b, new MethodCallback<Void>() {
//				@Override
//				public void onSuccess(Method method, Void response) {
//					Window.alert("OK!");
//				}
//				
//				@Override
//				public void onFailure(Method method, Throwable exception) {
//					Window.alert(method.getResponse().getText());
//				}
//			});
//			
			
			// // Get - y Update ANDA!
//			BeneficiarioService.Util.get().getById(2L, new MethodCallback<Beneficiario>() {
//				@Override
//				public void onFailure(Method method, Throwable exception) {
//					Window.alert(exception.getMessage());
//					new UIDialog("titulo",new HTML(method.getResponse().getText()));
//				}
//				@Override
//				public void onSuccess(Method method, Beneficiario response) {
//					Window.alert("Respuesta: " + response.getNombres() + " DocumentoNro: " + response.getDocumento().getNumeroDocumento() + " TipoDoc: " + response.getDocumento().getTipoDocumento());
//					
//					response.setNombres(response.getNombres() + " pesada!");
//					BeneficiarioService.Util.get().actualizarBeneficiario(response, new MethodCallback<Void>() {
//
//						@Override
//						public void onFailure(Method method, Throwable exception) {
//							new UIDialog("titulo",new HTML(method.getResponse().getText()));
//						}
//
//						@Override
//						public void onSuccess(Method method, Void response) {
//							Window.alert("Actualizado");
//						}
//					});
//					
//				}
//			});
			
			
			/// Query ResultTEST FUNCIONA!
//			BeneficiarioService.Util.get().findByNombresDocs("4153109",0,10, new MethodCallback<List<Beneficiario>>() {
//				@Override
//				public void onFailure(Method method, Throwable exception) {
//					new UIDialog("titulo",new HTML(method.getResponse().getText()));
//				}
//
//				@Override
//				public void onSuccess(Method method, List<Beneficiario> response) {
//					for (Beneficiario bb : response) {
//						Window.alert(bb.getNombres());
//					}
//				}
//			});
			
			
			
			// // // Insert - Funciona OK!
//			Beneficiario bbb = new Beneficiario();
//			DocumentoIdentidad doc = new DocumentoIdentidad();
//			doc.setNumeroDocumento("12321");
//			doc.setTipoDocumento(TipoDocumentoIdentidad.CEDULA);
//			bbb.setDocumento(doc);
//			bbb.setNombres("cesar");
//			bbb.setFechaNacimiento(new Date());
//			bbb.setApellidos("sanabria");
//			bbb.setSexo(Sexo.MASCULINO);
//			BeneficiarioService.Util.get().nuevo(bbb, new MethodCallback<Long>() {
//				@Override
//				public void onSuccess(Method method, Long response) {
//					Window.alert("El id nuexo es: " + response);
//				}
//				@Override
//				public void onFailure(Method method, Throwable exception) {
//					new UIDialog("titulo",new HTML(method.getResponse().getText()));
//				}
//			});
//			
			
//			// // Get Beneficiario e Insert de solicidut ANDA
//			BeneficiarioService.Util.get().getById(2L, new MethodCallback<Beneficiario>() {
//				@Override
//				public void onFailure(Method method, Throwable exception) {
//					Window.alert(exception.getMessage());
//					new UIDialog("titulo",new HTML(method.getResponse().getText()));
//				}
//				@Override
//				public void onSuccess(Method method, Beneficiario response) {
//					
//					SolicitudTitular sol = new SolicitudTitular();
//					List<PeriodoAporteDeclarado> listaPeriodos = new ArrayList<PeriodoAporteDeclarado>();
//					
//					PeriodoAporteDeclarado pa = new PeriodoAporteDeclarado();
//					pa.setLugar("Financiera A");
//					listaPeriodos.add(pa);
//					
//					pa = new PeriodoAporteDeclarado();
//					pa.setLugar("Financiera B");
//					listaPeriodos.add(pa);
//					sol.setListaPeriodoAporteDeclarados(listaPeriodos);
//					sol.setBeneficiario(response);
//					
//					BeneficiarioService.Util.get().nuevoSolicitudTitular(sol, new MethodCallback<Void>() {
//						@Override
//						public void onSuccess(Method method, Void response) {
//							Window.alert("Solicitud creada!!!");
//						}
//						@Override
//						public void onFailure(Method method, Throwable exception) {
//							new UIDialog("titulo",new HTML(method.getResponse().getText()));
//						}
//					});
//					
//										
//				}
//			});

			
			
			
			
		
	    } catch (Exception e) {
	    	Window.alert(e.getMessage());
	    }
		
		//probamos Solicitud Titular
		try {
		
		  SolicitudTitularEditorWorkFlow b = new SolicitudTitularEditorWorkFlow(eventBus);
		  b.title = "Nueva solicitud titular";
		  b.mostrarDialog();
		  b.create();
		  
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