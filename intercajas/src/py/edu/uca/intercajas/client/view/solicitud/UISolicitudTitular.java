package py.edu.uca.intercajas.client.view.solicitud;

import py.edu.uca.intercajas.client.requestfactory.BeneficiarioProxy;
import py.edu.uca.intercajas.client.requestfactory.ContextGestionBeneficiario;
import py.edu.uca.intercajas.client.requestfactory.FactoryGestion;
import py.edu.uca.intercajas.server.entity.enums.TipoDocumentoIdentidad;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.UIDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class UISolicitudTitular extends UIBase {

	private static UISolicitudTitularUiBinder uiBinder = GWT
			.create(UISolicitudTitularUiBinder.class);

	interface UISolicitudTitularUiBinder extends
			UiBinder<Widget, UISolicitudTitular> {
	}
	
	private static  EventBus EVENTBUS = new SimpleEventBus();
	private static  FactoryGestion FACTORY  = GWT.create(FactoryGestion.class);
	
	@UiField Button guardar;
	@UiField Button volver;
	@UiField TextBox numeroDocumento;

	public UISolicitudTitular() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	
	@UiHandler("volver")
	void volver(ClickEvent e) {
		volver();
	}
	
	@UiHandler("guardar")
	void guardar(ClickEvent e) {
		FACTORY.initialize(EVENTBUS);
		
		ContextGestionBeneficiario context = FACTORY.contextGestionBeneficiario();
		
		context.findByDocumento(numeroDocumento.getValue(), TipoDocumentoIdentidad.CEDULA).with("direccion","documento").fire(new Receiver<BeneficiarioProxy>() {
			
			@Override
			public void onSuccess(BeneficiarioProxy response) {
			    // Create a dialog box and set the caption text
				HTML details = new HTML(response.getNombres() + " vive sobre la calle " + response.getDireccion().getCallePrincipal() + " numero: " + response.getDireccion().getNumeroCasa() + " de sexo:" + response.getSexo());
				new UIDialog("Aviso!", details);
			}
		});
	}
}
