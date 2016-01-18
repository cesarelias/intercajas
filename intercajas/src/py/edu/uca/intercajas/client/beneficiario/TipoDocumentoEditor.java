package py.edu.uca.intercajas.client.beneficiario;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import py.edu.uca.intercajas.shared.entity.DocumentoIdentidad;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;

public class TipoDocumentoEditor extends FlowPanel implements LeafValueEditor<DocumentoIdentidad.TipoDocumentoIdentidad> {

    private Map<RadioButton, DocumentoIdentidad.TipoDocumentoIdentidad> map;

    @UiConstructor
    public TipoDocumentoEditor() {
        map = new HashMap<RadioButton, DocumentoIdentidad.TipoDocumentoIdentidad>();
        for (DocumentoIdentidad.TipoDocumentoIdentidad e: DocumentoIdentidad.TipoDocumentoIdentidad.class.getEnumConstants()){
            RadioButton rb = new RadioButton("tipoDocumento", e.name());
            map.put(rb, e);
            super.add(rb);
        }
    }

    @Override
    public void setValue(DocumentoIdentidad.TipoDocumentoIdentidad value) {
        if (value==null)
            return;
        RadioButton rb = (RadioButton) super.getWidget(value.ordinal());
        rb.setValue(true);
    }

    @Override
    public DocumentoIdentidad.TipoDocumentoIdentidad getValue() {
        for (Entry<RadioButton, DocumentoIdentidad.TipoDocumentoIdentidad> e: map.entrySet()) {
            if (e.getKey().getValue())
                return e.getValue();
        }
        return null;
    }
}