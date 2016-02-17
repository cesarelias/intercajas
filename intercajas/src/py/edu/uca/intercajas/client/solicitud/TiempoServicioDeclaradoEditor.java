package py.edu.uca.intercajas.client.solicitud;

import java.io.IOException;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.UIErrorRestDialog;
import py.edu.uca.intercajas.client.solicitud.events.PeriodoAporteDeclaradoChangedEvent;
import py.edu.uca.intercajas.client.tiemposervicio.TiempoServicioReconocidoEditor.Listener;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.Caja;
import py.edu.uca.intercajas.shared.entity.Empleador;
import py.edu.uca.intercajas.shared.entity.TiempoServicioDeclarado;
import py.edu.uca.intercajas.shared.entity.TiempoServicioReconocido;

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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.SimpleKeyProvider;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class TiempoServicioDeclaradoEditor extends UIBase  {

	interface Binder extends UiBinder<Widget, TiempoServicioDeclaradoEditor> {
	}
	
	public interface Listener {
		void onChanged(TiempoServicioDeclarado tiempoServicioDeclarado);
	}
	
	@UiField DateBox inicio;
	@UiField DateBox fin;
	@UiField(provided = true) ValueListBox<Caja> caja;

	@UiField(provided = true) SuggestBox lugar;

	@Editor.Ignore	MultiWordSuggestOracle oracle;
	
	TiempoServicioDeclarado tiempoServicioDeclarado;
	
	Listener listener;

	public TiempoServicioDeclaradoEditor(final TiempoServicioDeclarado tiempoServicioDeclarado) {
		
	
		this.tiempoServicioDeclarado = tiempoServicioDeclarado;
		
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

		if (tiempoServicioDeclarado == null) {
			this.tiempoServicioDeclarado = new TiempoServicioDeclarado();
		} else {
			inicio.setValue(tiempoServicioDeclarado.getInicio());
			fin.setValue(tiempoServicioDeclarado.getFin());
			lugar.setValue(tiempoServicioDeclarado.getLugar());
			caja.setValue(tiempoServicioDeclarado.getCaja(), true);
		}

		addCajasListBox();
		
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

		tiempoServicioDeclarado.setCaja(caja.getValue());
		tiempoServicioDeclarado.setInicio(inicio.getValue());
		tiempoServicioDeclarado.setFin(fin.getValue());
		tiempoServicioDeclarado.setLugar(lugar.getValue());
		
		
		//AppUtils.EVENT_BUS.fireEvent(new PeriodoAporteDeclaradoChangedEvent(tiempoServicioDeclarado));
		if (listener!=null) {
			listener.onChanged(tiempoServicioDeclarado);
		}
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
				new UIErrorRestDialog(method, exception);
			}
		});
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}
	
	public void addCajasListBox() {
		BeneficiarioService.Util.get().findCajaAll(new MethodCallback<List<Caja>>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				new UIErrorRestDialog(method, exception);
			}

			 
			@Override
			public void onSuccess(Method method, List<Caja> response) {
				if (tiempoServicioDeclarado.getCaja() == null) {
					caja.setValue(response.get(0), true);
				} ;
				caja.setAcceptableValues(response);
			}
		});

	}
	
}
