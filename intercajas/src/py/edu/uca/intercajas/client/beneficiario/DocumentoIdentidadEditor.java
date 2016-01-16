package py.edu.uca.intercajas.client.beneficiario;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class DocumentoIdentidadEditor extends Composite  {

	private static DocumentoIdentidadEditorUiBinder uiBinder = GWT
			.create(DocumentoIdentidadEditorUiBinder.class);

	interface DocumentoIdentidadEditorUiBinder extends
			UiBinder<Widget, DocumentoIdentidadEditor> {
	}
	
	@UiField
	TextBox numeroDocumento;

	@UiField
	TipoDocumentoEditor tipoDocumento;
	
	public DocumentoIdentidadEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
