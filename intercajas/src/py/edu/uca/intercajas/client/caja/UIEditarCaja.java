package py.edu.uca.intercajas.client.caja;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.UIErrorRestDialog;
import py.edu.uca.intercajas.client.UIValidarFormulario;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.Caja;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UIEditarCaja extends UIBase {

	Caja caja;

	TextBox nombre = new TextBox();
	TextBox siglas = new TextBox();
	TextBox tmin = new TextBox();
	
	Integer tminInt;
	
	Button cancel = new Button("Cancelar");
	Button guardar = new Button("Guardar");
	
	public UIEditarCaja(Caja caja) {
		
		initComponents();

		if (caja == null) {
			titulo = "Nueva caja de jubilacion";
			this.caja = new Caja();
		} else {
			this.caja = caja;
			titulo = "Editando caja de jubilacion";
			nombre.setText(caja.getNombre());
//			nombre.setEnabled(false);
			
			siglas.setText(caja.getSiglas());
//			siglas.setEnabled(false);
			
			tmin.setText(String.valueOf(caja.getT_min()));
			tminInt = caja.getT_min();
			
		}
		
	}
	
	public void initComponents() {

		HorizontalPanel p = new HorizontalPanel();
		p.setWidth("100%");
		p.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		p.add(cancel);
		p.add(guardar);

		FlexTable table = new FlexTable();
		
		nombre.setMaxLength(70);
		nombre.setWidth("300px");
		nombre.setMaxLength(15);
		siglas.setWidth("120px");
		tmin.setWidth("120px");
		
		table.getElement().getStyle().setMargin(10, Unit.PX);
		table.setCellPadding(5);
		table.getColumnFormatter().setWidth(0, "60px");
//		table.getColumnFormatter().setWidth(1, "30px");
		
		table.setText(0, 0, "Nombre");
		table.setWidget(0, 1, nombre);

		table.setText(1, 0, "Siglas");
		table.setWidget(1, 1, siglas);
		
		table.setText(2, 0, "T MIN");
		table.setWidget(2, 1, tmin);
		
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

	public void onSave() {
		

		if (!formularioValido()) return;
		
		caja.setNombre(nombre.getValue());
		caja.setSiglas(siglas.getValue());
		
		try {
			tminInt = Integer.valueOf(tmin.getValue());
		} catch (Exception e) {
			Window.alert("Favor ingrese el numeros de meses de tmin");
			return;
		}
		
		caja.setT_min(tminInt);
		
		BeneficiarioService.Util.get().actualizarCaja(caja, new MethodCallback<Void>() {
			@Override
			public void onSuccess(Method method, Void response) {
				close();
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				new UIErrorRestDialog(method, exception);
			}
		});
		
//
//		BeneficiarioService.Util.get().actualizarUsuario(usuario, new MethodCallback<Void>() {
//
//			@Override
//			public void onFailure(Method method, Throwable exception) {
//				new UIErrorRestDialog(method, exception);
//			}
//
//			@Override
//			public void onSuccess(Method method, Void response) {
//				Window.alert("guardado");
//				close();
//			}
//		});
	}

	public boolean formularioValido() {
		
		UIValidarFormulario vf = new UIValidarFormulario("Favor complete las siguientes informaciones solicitadas para crear la concesion de beneficio");

		
		try {
			tminInt = Integer.valueOf(tmin.getValue());
		} catch (Exception e) {
			vf.addError("Favor ingrese el numeros de meses de tmin");
		}
		
		if (tminInt == null  || tminInt < 1 || tminInt > 1200) {
			vf.addError("Favor ingrese el numeros de meses de tmin entre los valores 1 y 1200");
		}
		
		return vf.esValido();
		
	}
}
