package py.edu.uca.intercajas.client.view.login;

import java.util.ArrayList;
import java.util.List;

import py.edu.uca.intercajas.shared.entity.Usuario;
import py.edu.uca.intercajas.shared.entity.Usuario.Tipo;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.ValueListBox;

public class TipoUsuarioEditor extends ValueListBox<Usuario.Tipo> {

	private Usuario.Tipo selected;
	
	private List<Usuario.Tipo> valores = new ArrayList<Usuario.Tipo>();

    @UiConstructor
    public TipoUsuarioEditor() {
        for (Usuario.Tipo e: Usuario.Tipo.class.getEnumConstants()){
        	valores.add(e);
        }
        selected = Usuario.Tipo.Gestor;
        this.setValue(Usuario.Tipo.Gestor, true);
        this.setAcceptableValues(valores);
        
        this.addValueChangeHandler(new ValueChangeHandler<Usuario.Tipo>() {
			@Override
			public void onValueChange(ValueChangeEvent<Tipo> event) {
				selected = event.getValue();
			}
		});
        
    }

    public Usuario.Tipo getValue() {
        return selected;
    }

}