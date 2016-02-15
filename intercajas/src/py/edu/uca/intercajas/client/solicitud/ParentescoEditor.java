package py.edu.uca.intercajas.client.solicitud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import py.edu.uca.intercajas.shared.entity.SolicitudBeneficiario;
import py.edu.uca.intercajas.shared.entity.SolicitudBeneficiario.Parentesco;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ValueListBox;

public class ParentescoEditor extends ValueListBox<SolicitudBeneficiario.Parentesco> {

	private SolicitudBeneficiario.Parentesco selected;
	
//    private Map<RadioButton, SolicitudBeneficiario.Parentesco> map;
	private List<SolicitudBeneficiario.Parentesco> valores = new ArrayList<SolicitudBeneficiario.Parentesco>();

    @UiConstructor
    public ParentescoEditor() {
        for (SolicitudBeneficiario.Parentesco e: SolicitudBeneficiario.Parentesco.class.getEnumConstants()){
        	valores.add(e);
        }
        selected = SolicitudBeneficiario.Parentesco.Conyuge;
        this.setValue(SolicitudBeneficiario.Parentesco.Conyuge, true);
        this.setAcceptableValues(valores);
        
        this.addValueChangeHandler(new ValueChangeHandler<SolicitudBeneficiario.Parentesco>() {
			@Override
			public void onValueChange(ValueChangeEvent<Parentesco> event) {
				selected = event.getValue();
			}
		});
        
    }

    public SolicitudBeneficiario.Parentesco getValue() {
        return selected;
    }

}