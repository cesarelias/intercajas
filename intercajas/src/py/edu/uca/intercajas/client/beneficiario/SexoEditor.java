package py.edu.uca.intercajas.client.beneficiario;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import py.edu.uca.intercajas.shared.entity.Beneficiario;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;

public class SexoEditor extends FlowPanel implements LeafValueEditor<Beneficiario.Sexo> {

    private Map<RadioButton, Beneficiario.Sexo> map;

    @UiConstructor
    public SexoEditor() {
        map = new HashMap<RadioButton, Beneficiario.Sexo>();
        for (Beneficiario.Sexo e: Beneficiario.Sexo.class.getEnumConstants()){
            RadioButton rb = new RadioButton("sexo", e.name());
            map.put(rb, e);
            super.add(rb);
        }
    }

    @Override
    public void setValue(Beneficiario.Sexo value) {
        if (value==null)
            return;
        RadioButton rb = (RadioButton) super.getWidget(value.ordinal());
        rb.setValue(true);
    }

    @Override
    public Beneficiario.Sexo getValue() {
        for (Entry<RadioButton, Beneficiario.Sexo> e: map.entrySet()) {
            if (e.getKey().getValue())
                return e.getValue();
        }
        return null;
    }
}