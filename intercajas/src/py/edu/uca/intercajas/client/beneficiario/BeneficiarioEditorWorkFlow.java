package py.edu.uca.intercajas.client.beneficiario;

import java.util.Set;

import javax.validation.ConstraintViolation;

import py.edu.uca.intercajas.client.requestfactory.BeneficiarioProxy;
import py.edu.uca.intercajas.client.requestfactory.FactoryGestion;
import py.edu.uca.intercajas.shared.UIDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
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
	private BeneficiarioProxy beneficiario;
	private final FactoryGestion requestFactory;

	public BeneficiarioEditorWorkFlow(FactoryGestion requestFactory,
			BeneficiarioProxy beneficiario) {
		this.requestFactory = requestFactory;
		this.beneficiario = beneficiario;
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
			
		context.fire(new Receiver<Void>() {
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
			public void onSuccess(Void response) {
				// If everything went as planned, just dismiss the dialog box
				dialog.hide();
			}

		});

	}

	public static void register(EventBus eventBus, final FactoryGestion requestFactory) {
		eventBus.addHandler(EditBeneficiarioEvent.TYPE,
				new EditBeneficiarioEvent.Handler() {
					public void startEdit(BeneficiarioProxy beneficiario,
							RequestContext requestContext) {
						new BeneficiarioEditorWorkFlow(requestFactory,
								beneficiario).edit(requestContext);
					}
				});

	}

	public void edit(RequestContext requestContext) {
		editorDriver = GWT.create(Driver.class);
		editorDriver.initialize(requestFactory, beneficiarioEditor);
		if (requestContext == null) {
			fetchAndEdit();
			return;
		}

		editorDriver.edit(beneficiario, requestContext);
		dialog.center();
	}

	private void fetchAndEdit() {
		Window.alert("requestContext nulo, que diablos se hace aqui?");
		// TODO Auto-generated method stub
	}
	
	@UiHandler("cancelar")
	void onCancel(ClickEvent event) {
		dialog.hide();
	}	

}
