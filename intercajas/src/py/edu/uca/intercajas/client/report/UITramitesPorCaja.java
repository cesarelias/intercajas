package py.edu.uca.intercajas.client.report;


import java.io.IOException;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.TextCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.UIErrorRestDialog;
import py.edu.uca.intercajas.client.UIValidarFormulario;
import py.edu.uca.intercajas.client.menumail.Mailboxes;
import py.edu.uca.intercajas.client.menumail.RefreshMailEvent;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.Caja;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.view.client.SimpleKeyProvider;

public class UITramitesPorCaja extends UIBase  {


	DateBox desde;
	DateBox hasta;
	Button obtener;
	Button cerrar;
	CheckBox estado0;
	CheckBox estado1;
	CheckBox estado2;
	DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd/MM/yyyy");
	ValueListBox<Caja> caja;
	Long caja_id;
	Integer estado0i, estado1i, estado2i;

	
	public UITramitesPorCaja() {
		
		
		
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
		
		titulo = "Reporte de tramites por Caja de Jubilación";
		desde = new DateBox();
		desde.setFormat(new DateBox.DefaultFormat(dateFormat));
		hasta = new DateBox();
		hasta.setFormat(new DateBox.DefaultFormat(dateFormat));
		estado0 = new CheckBox("Nuevo");
		estado0.setValue(true);
		estado1 = new CheckBox("Con Antiguedad");
		estado1.setValue(true);
		estado2 = new CheckBox("Finiquitado");
		estado2.setValue(true);
		
		obtener = new Button("Descargar Informe");
		cerrar = new Button("Cerrar");

		
		initCaja();	
		
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
		table.setText(2, 0, "Caja");
		table.setWidget(2, 1, caja);
		table.setWidget(3, 0, new Label("Estados"));
		table.setWidget(4, 1, estado0);
		table.setWidget(5, 1, estado1);
		table.setWidget(6, 1, estado2);
		
		VerticalPanel v = new VerticalPanel();
		
		v.add(table);
		v.add(p);
		
		obtener.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				try {
				if (caja.getValue() == null || caja.getValue().getId() == null) {
					caja_id = null;
				} else {
					caja_id = caja.getValue().getId();
				}
				} catch (Exception e) {
					Window.alert(e.getMessage());
				}
				
				estado0i = estado0.getValue() ? 0 : null;
				estado1i = estado1.getValue() ? 1 : null;
				estado2i = estado2.getValue() ? 2 : null;
				
				if (!formularioValido()) return;
				
				BeneficiarioService.Util.get().reporteTramitesPorCaja(caja_id, estado0i, estado1i, estado2i, desde.getTextBox().getValue(), hasta.getTextBox().getValue(), new TextCallback() {
					@Override
					public void onSuccess(Method method, String response) {
						Window.open( "servlet.gupld?show=" + "reports/" + response + "&nombreDescarga=tramitesPorCaja.pdf", "_parent", "");
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
		
	
		return v;
		
	}
	
	private void initCaja() {
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
			
	  try {
	  BeneficiarioService.Util.get().findCajaAll(new MethodCallback<List<Caja>>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				new UIErrorRestDialog(method, exception);
			}

			@Override
			public void onSuccess(Method method, List<Caja> response) {
				Caja cajaTodos = new Caja();
				cajaTodos.setSiglas("TODOS");
				response.add(0,cajaTodos);
				caja.setAcceptableValues(response);
			}
		});
	  } catch (Exception e) {
		  Window.alert(e.getMessage());
	  }

	  
	  caja.addValueChangeHandler(new ValueChangeHandler<Caja>() {
			@Override
			public void onValueChange(ValueChangeEvent<Caja> event) {
		//		setCajaIdFilter(event.getValue().getId());
		//		AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent(Mailboxes.this.beneficiarioIdFilter,event.getValue().getId(), fechaDesde, fechaHasta));
			}
	  });
		
	}

	public boolean formularioValido() {
			
		UIValidarFormulario vf = new UIValidarFormulario("Favor complete las siguientes informaciones solicitadas para crear el informe de auditoria");

		
		if (!AppUtils.esFecha(desde)) {
			vf.addError("Fecha desde no valida");
		}

		if (!AppUtils.esFecha(hasta)) {
			vf.addError("Fecha hasta no valida");
		}
		
		if (AppUtils.esFecha(desde) && AppUtils.esFecha(hasta)) {
			if (hasta.getValue().before(desde.getValue())) {
				vf.addError("Rango de fecha del desde - hasta no valido");
			}
		}

		
		if (!estado0.getValue() && !estado1.getValue() && !estado2.getValue()) {
			vf.addError("Seleccione al menos un estado (Nuevo/Con Antiguedad/Finiquitado)");
		}
		
		return vf.esValido();
	}
	
	
}
