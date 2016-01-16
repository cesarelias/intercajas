package py.edu.uca.intercajas.client.solicitud;

import py.edu.uca.intercajas.shared.UIBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class UISolicitudTitular extends UIBase {

	private static UISolicitudTitularUiBinder uiBinder = GWT
			.create(UISolicitudTitularUiBinder.class);

	interface UISolicitudTitularUiBinder extends
			UiBinder<Widget, UISolicitudTitular> {
	}
	
	@UiField Button guardar;
	@UiField Button volver;
	@UiField TextBox numeroDocumento;

	public UISolicitudTitular() {
		initWidget(uiBinder.createAndBindUi(this));
		title = "Solicitud Titular";
	}

	
	@UiHandler("volver")
	void volver(ClickEvent e) {
		volver();
	}
	
	@UiHandler("guardar")
	void guardar(ClickEvent e) {
	}
}
