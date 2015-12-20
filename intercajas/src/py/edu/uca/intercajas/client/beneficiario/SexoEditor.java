package py.edu.uca.intercajas.client.beneficiario;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import py.edu.uca.intercajas.server.entity.enums.Sexo;
import py.edu.uca.intercajas.server.entity.enums.TipoDocumentoIdentidad;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;

public class SexoEditor extends FlowPanel implements LeafValueEditor<Sexo> {

    private Map<RadioButton, Sexo> map;

    @UiConstructor
    public SexoEditor() {
        map = new HashMap<RadioButton, Sexo>();
        for (Sexo e: Sexo.class.getEnumConstants()){
            RadioButton rb = new RadioButton("sexo", e.name());
            map.put(rb, e);
            super.add(rb);
        }
    }

    @Override
    public void setValue(Sexo value) {
        if (value==null)
            return;
        RadioButton rb = (RadioButton) super.getWidget(value.ordinal());
        rb.setValue(true);
    }

    @Override
    public Sexo getValue() {
        for (Entry<RadioButton, Sexo> e: map.entrySet()) {
            if (e.getKey().getValue())
                return e.getValue();
        }
        return null;
    }
}