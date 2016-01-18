package py.edu.uca.intercajas.client.solicitud;

import py.edu.uca.intercajas.client.beneficiario.BeneficiarioSelector;
import py.edu.uca.intercajas.shared.UIBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.ui.client.ValueBoxEditorDecorator;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class SolicitudTitularEditor extends UIBase  {

	private static SolicitudTitularEditorUiBinder uiBinder = GWT
			.create(SolicitudTitularEditorUiBinder.class);

	interface SolicitudTitularEditorUiBinder extends
			UiBinder<Widget, SolicitudTitularEditor> {
	}
	
	SimpleEventBus eventBus;

	@UiField TextBox numero;
	@UiField DateBox fecha;
	@UiField (provided = true) BeneficiarioSelector beneficiario;

	public SolicitudTitularEditor(SimpleEventBus eventBus) {

		this.eventBus = eventBus;

		beneficiario = new BeneficiarioSelector(eventBus);

		title = "Solicitud Titular";
		initWidget(uiBinder.createAndBindUi(this));

//		eventBus.addHandler(BeneficiarioChangedEvent.TYPE, new BeneficiarioChangedEvent.Handler() {
//			@Override
//			public void selected(BeneficiarioProxy beneficiarioSelected) {
////				labelBeneficiario.setText(beneficiarioSelected.getNombres() + ", " + beneficiarioSelected.getNombres());
//				//TODO falta sacar el evento una vez disparado. O sino queda por siempre aqui.
//			}
//		});

		DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd/MM/yyyy");
		fecha.setFormat(new DateBox.DefaultFormat(dateFormat));
		fecha.getDatePicker().setYearAndMonthDropdownVisible(true);
		fecha.getDatePicker().setVisibleYearCount(99);

	}
	
//	@UiHandler("buscarBeneficiario")
//	void onBuscarBeneficiario(ClickEvent event) {
//		new ListaBeneficiarios(eventBus, factoryGestion, 10).mostrarDialog();
//	}

}