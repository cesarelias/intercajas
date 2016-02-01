package py.edu.uca.intercajas.client.tiemposervicio;

import java.io.IOException;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.solicitud.events.PeriodoAporteDeclaradoChangedEvent;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.Caja;
import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
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
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.SimpleKeyProvider;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class TiempoServicioReconocidoEditor extends UIBase  {

	interface Binder extends UiBinder<Widget, TiempoServicioReconocidoEditor> {
	}

	public interface Listener {
		void onChanged(TiempoServicioReconocido tiempoServicioReconocido);
	}
	
	@UiField DateBox inicio;
	@UiField DateBox fin;

	@UiField(provided = true) SuggestBox lugar;

	@Editor.Ignore	MultiWordSuggestOracle oracle;
	
	TiempoServicioReconocido tiempoServicioReconocido;
	Listener listener;

	public TiempoServicioReconocidoEditor(TiempoServicioReconocido periodoAporteReconocidoEdit) {
		
		this.tiempoServicioReconocido = periodoAporteReconocidoEdit;
		
		oracle = new MultiWordSuggestOracle();
		lugar = new SuggestBox(oracle);	

		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

		if (tiempoServicioReconocido == null) {
			tiempoServicioReconocido = new TiempoServicioReconocido();
		} else {
			inicio.setValue(tiempoServicioReconocido.getInicio());
			fin.setValue(tiempoServicioReconocido.getFin());
			lugar.setValue("esto falta");
		}

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

		CajaDeclarada cd = new CajaDeclarada();
		Caja c = new Caja();
		c.setSiglas("CAJADEC");
		cd.setCaja(c);
		
		Empleador e = new Empleador();
		e.setDescripcion("FALTA EL EMPLEADOR");
		
		tiempoServicioReconocido.setCajaDeclarada(cd);
		tiempoServicioReconocido.setInicio(inicio.getValue());
		tiempoServicioReconocido.setFin(fin.getValue());
		tiempoServicioReconocido.setEmpleador(e);
		
		
		if (listener!=null) {
			listener.onChanged(tiempoServicioReconocido);
		}
//		AppUtils.EVENT_BUS.fireEvent(new PeriodoAporteDeclaradoChangedEvent(tiempoServicioDeclarado));
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

	public void setListener(Listener listener) {
		this.listener = listener;
	}
	
}