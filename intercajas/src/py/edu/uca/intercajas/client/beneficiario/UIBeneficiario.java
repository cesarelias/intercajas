package py.edu.uca.intercajas.client.beneficiario;

import py.edu.uca.intercajas.shared.UIBase;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class UIBeneficiario extends UIBase  {

	private static UIBeneficiarioUiBinder uiBinder = GWT
			.create(UIBeneficiarioUiBinder.class);

	interface UIBeneficiarioUiBinder extends UiBinder<Widget, UIBeneficiario> {
	}
	
	private static  EventBus EVENTBUS = new SimpleEventBus();


	@UiField Button guardar;
	@UiField Button volver;
	@UiField RadioButton tipoDocumento0;
	@UiField RadioButton tipoDocumento1;
	@UiField TextBox numeroDocumento;
	@UiField TextBox nombres;
	@UiField DateBox fechaNacimiento;
	@UiField TextBox apellidos;
	@UiField RadioButton sexo0;
	@UiField RadioButton sexo1;
	@UiField TextBox callePrincipal;
	@UiField TextBox numeroCasa;
	@UiField TextBox barrio;
	@UiField TextBox ciudad;
	@UiField TextBox departamento;
	

	public UIBeneficiario() {

		initWidget(uiBinder.createAndBindUi(this));
		//TODO la fecha trae con hora, falta sacar la hora!
	    DateTimeFormat dateFormat=DateTimeFormat.getFormat("dd/MM/yyyy");
		fechaNacimiento.setFormat(new DateBox.DefaultFormat(dateFormat));
	}
	
	public UIBeneficiario(EventBus eventBus) {

		this.EVENTBUS = eventBus;
		initWidget(uiBinder.createAndBindUi(this));
		//TODO la fecha trae con hora, falta sacar la hora!
	    DateTimeFormat dateFormat=DateTimeFormat.getFormat("dd/MM/yyyy");
		fechaNacimiento.setFormat(new DateBox.DefaultFormat(dateFormat));
	}

	@UiHandler("volver")
	void volver(ClickEvent e) {
		volver();
	}
	
	@UiHandler("guardar")
	void guardar(ClickEvent e) {
		
	}


//	public static final native String getLanguage() /*-{
//    	return navigator.language;
//	}-*/;
	
	
}
