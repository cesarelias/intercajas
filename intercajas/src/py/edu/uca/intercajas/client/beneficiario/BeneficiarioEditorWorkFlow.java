package py.edu.uca.intercajas.client.beneficiario;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.server.entity.Beneficiario;
import py.edu.uca.intercajas.server.entity.Direccion;
import py.edu.uca.intercajas.server.entity.DocumentoIdentidad;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.UIDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class BeneficiarioEditorWorkFlow extends UIBase {

	interface Binder extends UiBinder<Widget, BeneficiarioEditorWorkFlow> {
//		Binder BINDER = GWT.create(Binder.class);
	}

	@UiField(provided = true)
	BeneficiarioEditor beneficiarioEditor;
	
	Beneficiario beneficiario;

	Boolean nuevo = false;
	
//	@UiField
//	DialogBox dialog;

	public BeneficiarioEditorWorkFlow() {
		beneficiarioEditor = new BeneficiarioEditor();
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
	}

	@UiHandler("guardar")
	void onSave(ClickEvent event) {

		
		beneficiario.setApellidos(beneficiarioEditor.apellidos.getValue());
		beneficiario.setNombres(beneficiarioEditor.nombres.getValue());
		beneficiario.getDocumento().setNumeroDocumento(beneficiarioEditor.documento.numeroDocumento.getValue());
		beneficiario.getDocumento().setTipoDocumento(beneficiarioEditor.documento.tipoDocumento.getValue());
		beneficiario.getDireccion().setCallePrincipal(beneficiarioEditor.direccion.callePrincipal.getValue());
		beneficiario.getDireccion().setBarrio(beneficiarioEditor.direccion.barrio.getValue());
		beneficiario.getDireccion().setCiudad(beneficiarioEditor.direccion.ciudad.getValue());
		beneficiario.getDireccion().setDepartamento(beneficiarioEditor.direccion.departamento.getValue());
		beneficiario.getDireccion().setNumeroCasa(beneficiarioEditor.direccion.numeroCasa.getValue());
		beneficiario.setFechaNacimiento(beneficiarioEditor.fechaNacimiento.getValue());
		beneficiario.setSexo(beneficiarioEditor.sexo.getValue());

		if (nuevo) { //insert
			
			BeneficiarioService.Util.get().nuevo(beneficiario, new MethodCallback<Long>() {
				
				@Override
				public void onSuccess(Method method, Long response) {
					close();
				}
				
				@Override
				public void onFailure(Method method, Throwable exception) {
					new UIDialog("Error",new HTML(method.getResponse().getText()));
				}
			});
			
		} else { //update
			
			BeneficiarioService.Util.get().actualizarBeneficiario(beneficiario, new MethodCallback<Void>() {
				@Override
				public void onSuccess(Method method, Void response) {
					close();
				}
				@Override
				public void onFailure(Method method, Throwable exception) {
					new UIDialog("Error",new HTML(method.getResponse().getText()));
				}
			});
		
		
		}
		
		
//		// Flush the contents of the UI
//		RequestContext context = editorDriver.flush();
//
//		// Check for errors
//		if (editorDriver.hasErrors()) {
//			setTitle("Se encontraron errores");
//			return;
//		}
//
//		// Send the request
//		context.fire();


	}

	@UiHandler("cancelar")
	void onCancel(ClickEvent event) {
		close();
	}	

	public void create() {
		try {
			
			nuevo = true;
			this.beneficiario = new Beneficiario();
			this.beneficiario.setDireccion(new Direccion());
			this.beneficiario.setDocumento(new DocumentoIdentidad());

			setTitle("Nuevo Beneficiario");
		
			
		} catch (Exception e) {
			Window.alert(e.getMessage());
		}

	}

	public void edit(Beneficiario beneficiario) {
		
		nuevo = false;
		
		this.beneficiario = beneficiario;

		setTitle("Editando Beneficiario");
		
		
		beneficiarioEditor.apellidos.setValue(beneficiario.getApellidos());
		beneficiarioEditor.nombres.setValue(beneficiario.getNombres());
		beneficiarioEditor.documento.numeroDocumento.setValue(beneficiario.getDocumento().getNumeroDocumento());
		beneficiarioEditor.documento.tipoDocumento.setValue(beneficiario.getDocumento().getTipoDocumento());
		beneficiarioEditor.direccion.callePrincipal.setValue(beneficiario.getDireccion().getCallePrincipal());
		beneficiarioEditor.direccion.barrio.setValue(beneficiario.getDireccion().getBarrio());
		beneficiarioEditor.direccion.ciudad.setValue(beneficiario.getDireccion().getCiudad());
		beneficiarioEditor.direccion.departamento.setValue(beneficiario.getDireccion().getDepartamento());
		beneficiarioEditor.direccion.numeroCasa.setValue(beneficiario.getDireccion().getNumeroCasa());
		beneficiarioEditor.fechaNacimiento.setValue(beneficiario.getFechaNacimiento());
		beneficiarioEditor.sexo.setValue(beneficiario.getSexo());
		
		
//		Request<BeneficiarioProxy> fetchRequest = factoryGestion.contextGestionBeneficiario().find(beneficiario.getId());
//		fetchRequest.with(editorDriver.getPaths());	
//
//		fetchRequest.to(new Receiver<BeneficiarioProxy>() {
//
//			@Override
//			public void onSuccess(BeneficiarioProxy response) {
//				
//				ContextGestionBeneficiario contextX = factoryGestion.contextGestionBeneficiario();
//				editorDriver = GWT.create(Driver.class);
//				editorDriver.initialize(factoryGestion, beneficiarioEditor);
//				editorDriver.edit(response, contextX);
//				d.center();
//
//				contextX.actualizarBeneficiario(response).to(new Receiver<Void>() {
//					@Override
//					public void onConstraintViolation(Set<ConstraintViolation<?>> errors) {
//						// Otherwise, show ConstraintViolations in the UI
//						setTitle("Se encontraron errores");
//						for (ConstraintViolation<?>  e : errors) {
//							Window.alert(e.getMessage());
//						}
//						editorDriver.setConstraintViolations(errors);
//					}
//					@Override
//					public void onFailure(ServerFailure error) {
//						Window.alert("Error al grabar" + error.getMessage());
//					}
//					@Override
//					public void onSuccess(Void response) {
//						// If everything went as planned, just dismiss the dialog box
//						//eventBus.fireEvent(new FinishDialogEvent(d));
//						eventBus.fireEvent(new BeneficiarioChangedEvent(getBeneficiario()));
//						close();
//					};
//				});
//
//			}
//		}).fire();

	}
	
	Beneficiario getBeneficiario() {
		return this.beneficiario;
	}
}
