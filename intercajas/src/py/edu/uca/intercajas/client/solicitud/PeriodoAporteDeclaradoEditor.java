package py.edu.uca.intercajas.client.solicitud;

import java.util.List;

import py.edu.uca.intercajas.client.requestfactory.CajaProxy;
import py.edu.uca.intercajas.client.requestfactory.ContextGestionSolicitud;
import py.edu.uca.intercajas.client.requestfactory.EmpleadorProxy;
import py.edu.uca.intercajas.client.requestfactory.FactoryGestion;
import py.edu.uca.intercajas.client.requestfactory.PeriodoAporteDeclaradoProxy;
import py.edu.uca.intercajas.client.solicitud.events.PeriodoAporteDeclaradoChangedEvent;
import py.edu.uca.intercajas.shared.UIBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.SimpleKeyProvider;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class PeriodoAporteDeclaradoEditor extends UIBase  {

	interface Binder extends UiBinder<Widget, PeriodoAporteDeclaradoEditor> {
	}
	
	@UiField DateBox inicio;
	@UiField DateBox fin;
	@UiField(provided = true) ValueListBox<CajaProxy> caja;

	@UiField(provided = true) SuggestBox lugar;

	@Editor.Ignore	MultiWordSuggestOracle oracle;
	
	FactoryGestion factoryGestion2;
	ContextGestionSolicitud ctx;
	PeriodoAporteDeclaradoProxy periodoAporteDeclaradoProxy;

	public PeriodoAporteDeclaradoEditor(SimpleEventBus eventBus, FactoryGestion factoryGestion, ContextGestionSolicitud contextEdit, PeriodoAporteDeclaradoProxy periodoAporteDeclaradoProxyForEdit) {
		
		this.factoryGestion2 = factoryGestion;
		this.periodoAporteDeclaradoProxy = periodoAporteDeclaradoProxyForEdit;
		this.eventBus = eventBus;
		
		ctx = contextEdit;
		oracle = new MultiWordSuggestOracle();
		lugar = new SuggestBox(oracle);	

		caja = new ValueListBox<CajaProxy>(new ProxyRenderer<CajaProxy>(null) {
			@Override
			public String render(CajaProxy object) {
				return object.getSiglas();
			}
			},
			new SimpleKeyProvider<CajaProxy>() {
				@Override
				public Object getKey(CajaProxy item) {
					return item == null ? null : item.getId();
				}
			}
		);

		caja.addValueChangeHandler(new ValueChangeHandler<CajaProxy>() {
		@Override
		public void onValueChange(ValueChangeEvent<CajaProxy> event) {
			setSuggest(event.getValue().getId());
		}
		});
		
//		ctx = factoryGestion.contextGestionSolicitud();
		
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

		if (periodoAporteDeclaradoProxy == null) {
			periodoAporteDeclaradoProxy = ctx.create(PeriodoAporteDeclaradoProxy.class);
		} else {
			inicio.setValue(periodoAporteDeclaradoProxy.getInicio());
			fin.setValue(periodoAporteDeclaradoProxy.getFin());
			lugar.setValue(periodoAporteDeclaradoProxy.getLugar());
			caja.setValue(periodoAporteDeclaradoProxy.getCaja(), true);
		}

//		caja = new ValueListBox<CajaProxy>();
		
		factoryGestion.contextGestionSolicitud().getCajas().fire(new Receiver<List<CajaProxy>>() {
			@Override
			public void onSuccess(List<CajaProxy> response) {
				if (periodoAporteDeclaradoProxy.getCaja() == null) {
					caja.setValue(response.get(0), true);
				} ;
					
				caja.setAcceptableValues(response);
			}
		});
		
		DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd/MM/yyyy");
		inicio.setFormat(new DateBox.DefaultFormat(dateFormat));
		inicio.getDatePicker().setYearAndMonthDropdownVisible(true);
		inicio.getDatePicker().setVisibleYearCount(99);
		
		fin.setFormat(new DateBox.DefaultFormat(dateFormat));
		fin.getDatePicker().setYearAndMonthDropdownVisible(true);
		fin.getDatePicker().setVisibleYearCount(99);
		
	}
	
	@UiHandler("save")
	void onSave(ClickEvent event){

		periodoAporteDeclaradoProxy.setCaja(caja.getValue());
		periodoAporteDeclaradoProxy.setInicio(inicio.getValue());
		periodoAporteDeclaradoProxy.setFin(fin.getValue());
		periodoAporteDeclaradoProxy.setLugar(lugar.getValue());
		
		
		eventBus.fireEvent(new PeriodoAporteDeclaradoChangedEvent(periodoAporteDeclaradoProxy));
		close();
		
	}

	@UiHandler("cancel")
	void onCancel(ClickEvent event){
		close();
	}

	void setSuggest(Long caja_id) {
		factoryGestion2.contextGestionSolicitud().getEmpleador(caja_id).fire(new Receiver<List<EmpleadorProxy>>() {
			@Override
			public void onSuccess(List<EmpleadorProxy> response) {
				oracle.clear();
				for (EmpleadorProxy e : response) {
					oracle.add(e.getNombre());
				}
			}
		});
	}
	
}
