package py.edu.uca.intercajas.client.beneficiario;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class BeneficiarioEditor extends Composite {

	private static BeneficiarioEditorUiBinder uiBinder = GWT
			.create(BeneficiarioEditorUiBinder.class);

	interface BeneficiarioEditorUiBinder extends
			UiBinder<Widget, BeneficiarioEditor> {
	}
	
	@UiField
	TextBox nombres;

	@UiField
	TextBox apellidos;

	@UiField
	DateBox fechaNacimiento;

	@UiField(provided = false)
	DocumentoIdentidadEditor documento;

	@UiField(provided = false)
	DireccionEditor direccion;

	@UiField(provided = false)
	SexoEditor sexo;

	public BeneficiarioEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd/MM/yyyy");
		fechaNacimiento.setFormat(new DateBox.DefaultFormat(dateFormat));
		fechaNacimiento.getDatePicker().setYearAndMonthDropdownVisible(true);
		fechaNacimiento.getDatePicker().setVisibleYearCount(99);
	}

}
