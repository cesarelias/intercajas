package py.edu.uca.intercajas.client.beneficiario;

import java.util.Set;

import javax.validation.ConstraintViolation;

import py.edu.uca.intercajas.client.beneficiario.events.BeneficiarioChangedEvent;
import py.edu.uca.intercajas.client.requestfactory.BeneficiarioProxy;
import py.edu.uca.intercajas.client.requestfactory.ContextGestionBeneficiario;
import py.edu.uca.intercajas.client.requestfactory.FactoryGestion;
import py.edu.uca.intercajas.shared.UIBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class BeneficiarioEditorWorkFlow extends UIBase {

	interface Binder extends UiBinder<Widget, BeneficiarioEditorWorkFlow> {
//		Binder BINDER = GWT.create(Binder.class);
	}

	interface Driver extends
			RequestFactoryEditorDriver<BeneficiarioProxy, BeneficiarioEditor> {
	}

	@UiField(provided = true)
	BeneficiarioEditor beneficiarioEditor;
	
	BeneficiarioProxy beneficiario;

//	@UiField
//	DialogBox dialog;

	private Driver editorDriver;

	public BeneficiarioEditorWorkFlow() {
		beneficiarioEditor = new BeneficiarioEditor();
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
	}

	@UiHandler("guardar")
	void onSave(ClickEvent event) {

		// Flush the contents of the UI
		RequestContext context = editorDriver.flush();

		// Check for errors
		if (editorDriver.hasErrors()) {
			setTitle("Se encontraron errores");
			return;
		}

		// Send the request
		context.fire();


	}

	@UiHandler("cancelar")
	void onCancel(ClickEvent event) {
		close();
	}	

	public void create(BeneficiarioProxy beneficiario, ContextGestionBeneficiario requestContext, final FactoryGestion factoryGestion ) {
		try {
			this.beneficiario = beneficiario;

			setTitle("Nuevo Beneficiario");
		
			
		requestContext.insertarBeneficiario(beneficiario).to(new Receiver<Long>() {
			@Override
			public void onConstraintViolation(Set<ConstraintViolation<?>> errors) {
				// Otherwise, show ConstraintViolations in the UI
				setTitle("Se encontraron errores");
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
			public void onSuccess(Long response) {
				// If everything went as planned, just dismiss the dialog box
//				Window.alert("el nuevo id de beneficiario es: " + response.toString());
				eventBus.fireEvent(new BeneficiarioChangedEvent(getBeneficiario()));
				close();
			}
		});
		
		
		editorDriver = GWT.create(Driver.class);
		editorDriver.initialize(factoryGestion, beneficiarioEditor);
		editorDriver.edit(beneficiario, requestContext);
		
		
		} catch (Exception e) {
			Window.alert(e.getMessage());
		}

	}

	public void edit(BeneficiarioProxy beneficiario, final RequestContext requestContext, final FactoryGestion factoryGestion ) {
		
		this.beneficiario = beneficiario;
		
		setTitle("Editando Beneficiario");

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
				d.center();

				contextX.actualizarBeneficiario(response).to(new Receiver<Void>() {
					@Override
					public void onConstraintViolation(Set<ConstraintViolation<?>> errors) {
						// Otherwise, show ConstraintViolations in the UI
						setTitle("Se encontraron errores");
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
						//eventBus.fireEvent(new FinishDialogEvent(d));
						eventBus.fireEvent(new BeneficiarioChangedEvent(getBeneficiario()));
						close();
					};
				});

			}
		}).fire();

	}
	
	BeneficiarioProxy getBeneficiario() {
		return this.beneficiario;
	}
}
