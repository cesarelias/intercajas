package py.edu.uca.intercajas.client.beneficiario;

import java.util.Set;

import javax.validation.ConstraintViolation;

import py.edu.uca.intercajas.client.requestfactory.BeneficiarioProxy;
import py.edu.uca.intercajas.client.requestfactory.ContextGestionBeneficiario;
import py.edu.uca.intercajas.client.requestfactory.DireccionProxy;
import py.edu.uca.intercajas.client.requestfactory.FactoryGestion;
import py.edu.uca.intercajas.server.entity.Direccion;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class BeneficiarioEditorWorkFlow {

	interface Binder extends UiBinder<DialogBox, BeneficiarioEditorWorkFlow> {
		Binder BINDER = GWT.create(Binder.class);
	}

	interface Driver extends
			RequestFactoryEditorDriver<BeneficiarioProxy, BeneficiarioEditor> {
	}

	@UiField(provided = true)
	BeneficiarioEditor beneficiarioEditor;

	@UiField
	DialogBox dialog;

	private Driver editorDriver;

	public BeneficiarioEditorWorkFlow() {
		beneficiarioEditor = new BeneficiarioEditor();
		Binder.BINDER.createAndBindUi(this);
	}

	@UiHandler("guardar")
	void onGuardar(ClickEvent event) {

		// Flush the contents of the UI
		RequestContext context = editorDriver.flush();

		// Check for errors
		if (editorDriver.hasErrors()) {
			dialog.setText("Se encontraron errores");
			return;
		}

		// Send the request
		context.fire();
			

	}

	@UiHandler("cancelar")
	void onCancel(ClickEvent event) {
		dialog.hide();
	}	

	public void create(BeneficiarioProxy beneficiario, ContextGestionBeneficiario requestContext, final FactoryGestion factoryGestion ) {
	
		try {
		dialog.setText("Nuevo Beneficiario");
		
		
		requestContext.insertarBeneficiario(beneficiario).to(new Receiver<Void>() {
			@Override
			public void onConstraintViolation(Set<ConstraintViolation<?>> errors) {
				// Otherwise, show ConstraintViolations in the UI
				dialog.setText("Se encontraron errores");
				for (ConstraintViolation<?>  e : errors) {
					Window.alert(e.getMessage());
				}
				editorDriver.setConstraintViolations(errors);
			}
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert("Error al grabar" + error.getMessage());
			}
			@Override
			public void onSuccess(Void response) {
				// If everything went as planned, just dismiss the dialog box
				dialog.hide();
			}
		});
		
		
		editorDriver = GWT.create(Driver.class);
		editorDriver.initialize(factoryGestion, beneficiarioEditor);
		editorDriver.edit(beneficiario, requestContext);
		dialog.center();
		} catch (Exception e) {
			Window.alert(e.getMessage());
		}

	}

	public void edit(BeneficiarioProxy beneficiario, RequestContext requestContext, final FactoryGestion factoryGestion ) {

		dialog.setText("Editando Beneficiario");

		editorDriver = GWT.create(Driver.class);
		editorDriver.initialize(factoryGestion, beneficiarioEditor);

		Request<BeneficiarioProxy> fetchRequest = factoryGestion.contextGestionBeneficiario().find(beneficiario.getId());
		fetchRequest.with(editorDriver.getPaths());
			
		fetchRequest.to(new Receiver<BeneficiarioProxy>() {

			@Override
			public void onSuccess(BeneficiarioProxy response) {

				ContextGestionBeneficiario contextX = factoryGestion.contextGestionBeneficiario();
				
				editorDriver = GWT.create(Driver.class);
				editorDriver.initialize(factoryGestion, beneficiarioEditor);
				editorDriver.edit(response, contextX);
				dialog.center();

				contextX.actualizarBeneficiario(response).to(new Receiver<Void>() {
					@Override
					public void onConstraintViolation(Set<ConstraintViolation<?>> errors) {
						// Otherwise, show ConstraintViolations in the UI
						dialog.setText("Se encontraron errores");
						for (ConstraintViolation<?>  e : errors) {
							Window.alert(e.getMessage());
						}
						editorDriver.setConstraintViolations(errors);
					}
					@Override
					public void onFailure(ServerFailure error) {
						Window.alert("Error al grabar" + error.getMessage());
					}
					@Override
					public void onSuccess(Void response) {
						// If everything went as planned, just dismiss the dialog box
						dialog.hide();
					};
				});

			}
		}).fire();

	}
	
}
