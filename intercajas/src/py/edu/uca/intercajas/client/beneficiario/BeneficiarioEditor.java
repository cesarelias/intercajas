package py.edu.uca.intercajas.client.beneficiario;

import py.edu.uca.intercajas.client.requestfactory.BeneficiarioProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.ui.client.ValueBoxEditorDecorator;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;

public class BeneficiarioEditor extends Composite implements
		Editor<BeneficiarioProxy> {

	private static BeneficiarioEditorUiBinder uiBinder = GWT
			.create(BeneficiarioEditorUiBinder.class);

	interface BeneficiarioEditorUiBinder extends
			UiBinder<Widget, BeneficiarioEditor> {
	}

	@UiField
	ValueBoxEditorDecorator<String> nombres;

	@UiField
	ValueBoxEditorDecorator<String> apellidos;

	@UiField
	DatePicker fechaNacimiento;

	@UiField(provided = false)
	DocumentoIdentidadEditor documento;

	
	public BeneficiarioEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}


}
