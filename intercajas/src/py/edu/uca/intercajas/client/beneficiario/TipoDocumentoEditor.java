package py.edu.uca.intercajas.client.beneficiario;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import py.edu.uca.intercajas.server.entity.enums.TipoDocumentoIdentidad;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;

public class TipoDocumentoEditor extends FlowPanel implements LeafValueEditor<TipoDocumentoIdentidad> {

    private Map<RadioButton, TipoDocumentoIdentidad> map;

    @UiConstructor
    public TipoDocumentoEditor() {
        map = new HashMap<RadioButton, TipoDocumentoIdentidad>();
        for (TipoDocumentoIdentidad e: TipoDocumentoIdentidad.class.getEnumConstants()){
            RadioButton rb = new RadioButton("tipoDocumento", e.name());
            map.put(rb, e);
            super.add(rb);
        }
    }

    @Override
    public void setValue(TipoDocumentoIdentidad value) {
        if (value==null)
            return;
        RadioButton rb = (RadioButton) super.getWidget(value.ordinal());
        rb.setValue(true);
    }

    @Override
    public TipoDocumentoIdentidad getValue() {
        for (Entry<RadioButton, TipoDocumentoIdentidad> e: map.entrySet()) {
            if (e.getKey().getValue())
                return e.getValue();
        }
        return null;
    }
}