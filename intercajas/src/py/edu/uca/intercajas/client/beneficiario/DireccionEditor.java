package py.edu.uca.intercajas.client.beneficiario;

import py.edu.uca.intercajas.client.requestfactory.DireccionProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.ui.client.ValueBoxEditorDecorator;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class DireccionEditor extends Composite implements Editor<DireccionProxy> {

	private static DireccionEditorUiBinder uiBinder = GWT
			.create(DireccionEditorUiBinder.class);

	interface DireccionEditorUiBinder extends UiBinder<Widget, DireccionEditor> {
	}

	
	@UiField
	TextBox callePrincipal;
	@UiField
	TextBox numeroCasa;
	@UiField
	TextBox barrio;
	@UiField
	TextBox ciudad;
	@UiField
	TextBox departamento;
	
	public DireccionEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
