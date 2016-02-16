package py.edu.uca.intercajas.client.view.login;

import java.io.IOException;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.Caja;
import py.edu.uca.intercajas.shared.entity.Usuario;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SimpleKeyProvider;

public class UIEditarUsuario extends UIBase {

	Usuario usuario;

	TextBox nombre = new TextBox();
	TextBox descripcion = new TextBox();
	TipoUsuarioEditor tipo = new TipoUsuarioEditor();
	Button cancel = new Button("Cancelar");
	Button guardar = new Button("Guardar");
	ValueListBox<Caja> caja;
	
	public UIEditarUsuario(Usuario usuario) {
		
		initCajaList();
		initComponents();

		if (usuario == null) {
			titulo = "Nuevo usuario";
			this.usuario = new Usuario();
		} else {
			this.usuario = usuario;
			titulo = "Editando usuario";
			nombre.setText(usuario.getNombre());
			nombre.setEnabled(false);
			descripcion.setText(usuario.getDescripcion());
			caja.setValue(usuario.getCaja());
			tipo.setValue(usuario.getTipo());
		}
		
		
	}
	
	public void initComponents() {

		HorizontalPanel p = new HorizontalPanel();
		p.setWidth("100%");
		p.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		p.add(cancel);
		p.add(guardar);

		FlexTable table = new FlexTable();
		
		table.getElement().getStyle().setMargin(10, Unit.PX);
		table.setCellPadding(5);
		table.getColumnFormatter().setWidth(0, "60px");
		table.getColumnFormatter().setWidth(1, "60px");
		
		table.setText(0, 0, "Descripcion");
		table.setWidget(0, 1, descripcion);
		table.setText(1, 0, "Nombre");
		table.setWidget(1, 1, nombre);
		table.setText(2, 0, "Caja");
		table.setWidget(2, 1, caja);
		table.setText(3, 0, "Tipo");
		table.setWidget(3, 1, tipo);
		
		VerticalPanel v = new VerticalPanel();
		
		v.add(table);
		v.add(p);
		
		initWidget(v);
	
		cancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				close();
			}
		});
		
		guardar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onSave();
			}
		});
		
	}

	//TODO, no hace falta peroooo, seria mejor tener el ListBox caja en un editor, para no repertir codigo!
	public void initCajaList() {
		caja = new ValueListBox<Caja>(
				
				new Renderer<Caja>() {
					@Override
					public String render(Caja object) {
						return object.getSiglas();
					}
					@Override
					public void render(Caja object, Appendable appendable)
							throws IOException {
						// TODO Auto-generated method stub
					}
				},
				new SimpleKeyProvider<Caja>() {
					@Override
					public Object getKey(Caja item) {
						return item == null ? null : item.getId();
					}
			});
		
		BeneficiarioService.Util.get().findCajaAll(new MethodCallback<List<Caja>>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Method method, List<Caja> response) {
				if (response != null && response.size() > 0) {
					caja.setValue(response.get(0), true);
				}
				caja.setAcceptableValues(response);
			}
		});

	}

	
	public void onSave() {
		
		usuario.setNombre(nombre.getValue());
		usuario.setDescripcion(descripcion.getValue());
		usuario.setCaja(caja.getValue());
		usuario.setTipo(tipo.getValue());

		BeneficiarioService.Util.get().actualizarUsuario(usuario, new MethodCallback<Void>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				// TODO Auto-generated method stub
				Window.alert(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, Void response) {
				Window.alert("guardado");
				close();
			}
		});
	}
	
}
