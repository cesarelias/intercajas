package py.edu.uca.intercajas.client.finiquito;

import java.util.ArrayList;
import java.util.List;

import py.edu.uca.intercajas.shared.entity.Denegado;
import py.edu.uca.intercajas.shared.entity.Denegado.Motivo;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.ValueListBox;

public class DenegadoMotivoEditor extends ValueListBox<Denegado.Motivo> {

	private Denegado.Motivo selected;
	
//    private Map<RadioButton, SolicitudBeneficiario.Parentesco> map;
	private List<Denegado.Motivo> valores = new ArrayList<Denegado.Motivo>();

    @UiConstructor
    public DenegadoMotivoEditor() {
        for (Denegado.Motivo e: Denegado.Motivo.class.getEnumConstants()){
        	valores.add(e);
        }
        selected = Denegado.Motivo.NoReuneEdadAntiguedad;
        this.setValue(Denegado.Motivo.NoReuneEdadAntiguedad, true);
        this.setAcceptableValues(valores);
        
        this.addValueChangeHandler(new ValueChangeHandler<Denegado.Motivo>() {
			@Override
			public void onValueChange(ValueChangeEvent<Motivo> event) {
				selected = event.getValue();
			}
		});
        
    }

    public Denegado.Motivo getValue() {
        return selected;
    }

}