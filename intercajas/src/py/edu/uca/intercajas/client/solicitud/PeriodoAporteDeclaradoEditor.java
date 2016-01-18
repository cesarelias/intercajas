package py.edu.uca.intercajas.client.solicitud;

import java.io.IOException;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.solicitud.events.PeriodoAporteDeclaradoChangedEvent;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.Caja;
import py.edu.uca.intercajas.shared.entity.Empleador;
import py.edu.uca.intercajas.shared.entity.PeriodoAporteDeclarado;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.SimpleKeyProvider;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class PeriodoAporteDeclaradoEditor extends UIBase  {

	interface Binder extends UiBinder<Widget, PeriodoAporteDeclaradoEditor> {
	}
	
	@UiField DateBox inicio;
	@UiField DateBox fin;
	@UiField(provided = true) ValueListBox<Caja> caja;

	@UiField(provided = true) SuggestBox lugar;

	@Editor.Ignore	MultiWordSuggestOracle oracle;
	
	PeriodoAporteDeclarado periodoAporteDeclarado;

	public PeriodoAporteDeclaradoEditor(SimpleEventBus eventBus, PeriodoAporteDeclarado periodoAporteDeclaradoEdit) {
		
		this.periodoAporteDeclarado = periodoAporteDeclaradoEdit;
		this.eventBus = eventBus;
		
		oracle = new MultiWordSuggestOracle();
		lugar = new SuggestBox(oracle);	

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
		
//		caja = new ValueListBox<Caja>(new ProxyRenderer<Caja>(null) {
//			@Override
//			public String render(Caja object) {
//				return object.getSiglas();
//			}
//			},
//			new SimpleKeyProvider<Caja>() {
//				@Override
//				public Object getKey(Caja item) {
//					return item == null ? null : item.getId();
//				}
//			}
//		);

		
		caja.addValueChangeHandler(new ValueChangeHandler<Caja>() {
		@Override
		public void onValueChange(ValueChangeEvent<Caja> event) {
			setSuggest(event.getValue().getId());
		}
		});
		
		
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

		if (periodoAporteDeclarado == null) {
			periodoAporteDeclarado = new PeriodoAporteDeclarado();
		} else {
			inicio.setValue(periodoAporteDeclarado.getInicio());
			fin.setValue(periodoAporteDeclarado.getFin());
			lugar.setValue(periodoAporteDeclarado.getLugar());
			caja.setValue(periodoAporteDeclarado.getCaja(), true);
		}

		BeneficiarioService.Util.get().findCajaAll(new MethodCallback<List<Caja>>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Method method, List<Caja> response) {
				if (periodoAporteDeclarado.getCaja() == null) {
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

		periodoAporteDeclarado.setCaja(caja.getValue());
		periodoAporteDeclarado.setInicio(inicio.getValue());
		periodoAporteDeclarado.setFin(fin.getValue());
		periodoAporteDeclarado.setLugar(lugar.getValue());
		
		
		eventBus.fireEvent(new PeriodoAporteDeclaradoChangedEvent(periodoAporteDeclarado));
		close();
		
	}

	@UiHandler("cancel")
	void onCancel(ClickEvent event){
		close();
	}

	void setSuggest(Long caja_id) {
		BeneficiarioService.Util.get().findEmpleadorByCajaId(caja_id, new MethodCallback<List<Empleador>>() {
			@Override
			public void onSuccess(Method method, List<Empleador> response) {
				oracle.clear();
				for (Empleador e : response) {
					oracle.add(e.getNombre());
				}
			}
			@Override
			public void onFailure(Method method, Throwable exception) {
			}
		});
	}
	
}
