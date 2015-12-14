package py.edu.uca.intercajas.client.view.solicitud;

import py.edu.uca.intercajas.client.requestfactory.BeneficiarioProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.ui.client.ValueBoxEditorDecorator;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class BeneficiarioEditor extends Composite implements Editor<BeneficiarioProxy>{

	private static BeneficiarioEditorUiBinder uiBinder = GWT
			.create(BeneficiarioEditorUiBinder.class);

	interface BeneficiarioEditorUiBinder extends
			UiBinder<Widget, BeneficiarioEditor> {
	}

	@UiField
	ValueBoxEditorDecorator<String> nombres;
	
	public BeneficiarioEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
