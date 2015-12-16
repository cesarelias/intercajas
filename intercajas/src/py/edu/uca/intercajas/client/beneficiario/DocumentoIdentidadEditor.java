package py.edu.uca.intercajas.client.beneficiario;

import py.edu.uca.intercajas.client.requestfactory.DocumentoIdentidadProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.ui.client.ValueBoxEditorDecorator;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class DocumentoIdentidadEditor extends Composite implements Editor<DocumentoIdentidadProxy> {

	private static DocumentoIdentidadEditorUiBinder uiBinder = GWT
			.create(DocumentoIdentidadEditorUiBinder.class);

	interface DocumentoIdentidadEditorUiBinder extends
			UiBinder<Widget, DocumentoIdentidadEditor> {
	}
	
	@UiField
	ValueBoxEditorDecorator<String> numeroDocumento;

	@UiField
	TipoDocumentoEditor tipoDocumento;
	
	public DocumentoIdentidadEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
