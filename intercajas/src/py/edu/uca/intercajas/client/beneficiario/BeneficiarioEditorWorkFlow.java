package py.edu.uca.intercajas.client.beneficiario;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.LoginService;
import py.edu.uca.intercajas.client.UIDialog;
import py.edu.uca.intercajas.client.UIErrorRestDialog;
import py.edu.uca.intercajas.client.UIValidarFormulario;
import py.edu.uca.intercajas.client.beneficiario.events.BeneficiarioChangedEvent;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.Beneficiario;
import py.edu.uca.intercajas.shared.entity.Beneficiario.Sexo;
import py.edu.uca.intercajas.shared.entity.Direccion;
import py.edu.uca.intercajas.shared.entity.DocumentoIdentidad;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.SimpleEventBus;

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

		if (!formularioValido()) return;
		try {
			
		
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
			
			BeneficiarioService.Util.get().nuevoBeneficiario(beneficiario, new MethodCallback<Void>() {
				
				@Override
				public void onSuccess(Method method, Void response) {
					close();
				}
				
				@Override
				public void onFailure(Method method, Throwable exception) {
					new UIErrorRestDialog(method, exception);
				}
			});
			
		} else { //update
			
			BeneficiarioService.Util.get().actualizarBeneficiario(beneficiario, new MethodCallback<Void>() {
				@Override
				public void onSuccess(Method method, Void response) {
					try { 
						AppUtils.EVENT_BUS.fireEvent(new BeneficiarioChangedEvent(beneficiario));
					} catch (Exception e) {
						Window.alert(e.getMessage());
					}

					close();
				}
				@Override
				public void onFailure(Method method, Throwable exception) {
					new UIErrorRestDialog(method, exception);
				}
			});
		
		
		}
		
		} catch (Exception e) {
			Window.alert(e.getMessage());
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

			
			beneficiarioEditor.documento.tipoDocumento.setValue(DocumentoIdentidad.TipoDocumentoIdentidad.CEDULA);
			beneficiarioEditor.sexo.setValue(Beneficiario.Sexo.MASCULINO);
			
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
	
	public boolean formularioValido() {
			
		UIValidarFormulario vf = new UIValidarFormulario("Favor complete las siguientes informaciones solicitadas para crear el beneficiario");

		if (beneficiarioEditor.nombres.getValue().length() < 2 ){
			vf.addError("Ingrese nombre del beneficiario");
		}
		
		if (beneficiarioEditor.apellidos.getValue().length() < 2) {
			vf.addError("Ingrese apellido del beneficiario");
		}
		
		if (!AppUtils.esFecha(beneficiarioEditor.fechaNacimiento)) {
			vf.addError("Ingrese una fecha de nacimiento valida");
		} else {
			if (beneficiarioEditor.fechaNacimiento.getValue().after(LoginService.Util.currentUser.getFechaLogin())) {
				vf.addError("La fecha de nacimiento no puede ser posterior a hoy");
			}
		}
		
		if (beneficiarioEditor.documento.numeroDocumento.getValue().length() == 0 ){
			vf.addError("Ingrese numero de documento");
		}
		
		
		return vf.esValido();
			
	}
}
