package py.edu.uca.intercajas.client.report;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.TextCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.UIValidarFormulario;
import py.edu.uca.intercajas.shared.UIBase;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;

public class UIAuditoria extends UIBase  {


	TextBox usuario;
	DateBox desde;
	DateBox hasta;
	Button obtener;
	Button cerrar;
	CheckBox todos;
	DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd/MM/yyyy");

	public UIAuditoria() {
		
		
		
		initWidget(init());
//		desde.setFormat(new DateBox.DefaultFormat(dateFormat));
//		inicio.getDatePicker().setYearAndMonthDropdownVisible(true);
//		inicio.getDatePicker().setVisibleYearCount(99);
		
//		hasta.setFormat(new DateBox.DefaultFormat(dateFormat));
//		fin.getDatePicker().setYearAndMonthDropdownVisible(true);
//		fin.getDatePicker().setVisibleYearCount(99);
		
	}
	
	@UiHandler("Obtener")
	void onObjtener(ClickEvent event){

		
	}

	VerticalPanel init() {
		
		titulo = "Reporte auditoria";
		usuario = new TextBox();
		desde = new DateBox();
		desde.setFormat(new DateBox.DefaultFormat(dateFormat));
		hasta = new DateBox();
		hasta.setFormat(new DateBox.DefaultFormat(dateFormat));
		todos = new CheckBox("Todos los usuarios");
		obtener = new Button("Descargar Informe");
		cerrar = new Button("Cerrar");
		
		HorizontalPanel p = new HorizontalPanel();
		p.setWidth("100%");
		p.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		p.add(cerrar);
		p.add(obtener);

		FlexTable table = new FlexTable();
		
		table.getElement().getStyle().setMargin(10, Unit.PX);
		table.setCellPadding(5);
		table.getColumnFormatter().setWidth(0, "60px");
		table.getColumnFormatter().setWidth(1, "60px");
		
		table.setText(0, 0, "Desde");
		table.setWidget(0, 1, desde);
		table.setText(1, 0, "Hasta");
		table.setWidget(1, 1, hasta);
		table.setText(2, 0, "Usuario");
		table.setWidget(2, 1, usuario);
		table.setWidget(3, 1, todos);
		
		VerticalPanel v = new VerticalPanel();
		
		v.add(table);
		v.add(p);
		
		obtener.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				if (!formularioValido()) return;
				
				String usuarioParam = todos.getValue() ? "%" : usuario.getValue();
				BeneficiarioService.Util.get().reporteAuditoriaPorUsuario(desde.getTextBox().getValue(), hasta.getTextBox().getValue(), usuarioParam, new TextCallback() {
					@Override
					public void onSuccess(Method method, String response) {
						Window.open( "servlet.gupld?show=" + "reports/" + response + "&nombreDescarga=informeAuditoria.pdf", "_parent", "");
					}
					
					@Override
					public void onFailure(Method method, Throwable exception) {
						Window.alert(exception.getMessage());
					}
				});
				
			}
		});
		
		cerrar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				close();
			}
		});
		
	
		todos.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (((CheckBox) event.getSource()).getValue()) {
					usuario.setText("");
					usuario.setEnabled(false);
				} else {
					usuario.setEnabled(true);
				}
			}
		});
		
		
		return v;
		
	}
	
	public boolean formularioValido() {
			
		UIValidarFormulario vf = new UIValidarFormulario("Favor complete las siguientes informaciones solicitadas para crear el informe de auditoria");

		
		if (!todos.getValue() && usuario.getValue().length() == 0) {
			vf.addError("Ingrese un usuario o selecciones \"todos\"");
		}
		
		if (!AppUtils.esFecha(desde)) {
			vf.addError("Fecha desde no valida");
		}

		if (!AppUtils.esFecha(hasta)) {
			vf.addError("Fecha hasta no valida");
		}
		
		return vf.esValido();
	}
	
	
}
