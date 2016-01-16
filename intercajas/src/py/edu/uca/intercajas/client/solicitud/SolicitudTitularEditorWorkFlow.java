package py.edu.uca.intercajas.client.solicitud;

import py.edu.uca.intercajas.server.entity.SolicitudTitular;
import py.edu.uca.intercajas.shared.UIBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class SolicitudTitularEditorWorkFlow extends UIBase {

	interface Binder extends UiBinder<Widget, SolicitudTitularEditorWorkFlow> {
	}
	
	@UiField(provided = true) SolicitudTitularEditor solicitudTitularEditor;
	@UiField(provided = true) TablaPeriodoAporteDeclarado tablaPeriodoAporteDeclarado;
	
	
	SolicitudTitular solicitudTitular;
	
	public SolicitudTitularEditorWorkFlow(SimpleEventBus eventBus) {
//		title = "Solicitud Titular";
		this.eventBus = eventBus;
	    tablaPeriodoAporteDeclarado = new TablaPeriodoAporteDeclarado(eventBus);
		solicitudTitularEditor = new SolicitudTitularEditor(eventBus);
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
	}

	@UiHandler("cancelar")
	void onCancel(ClickEvent event) {
		close();
	}
	
	@UiHandler("guardar")
	void onSave(ClickEvent event) {
		
//		solicitudTitular.setListaPeriodoAporteDeclarados(tablaPeriodoAporteDeclarado.listaPeriodoAporteDeclarado);
		

	}

	public void create() {
		try {
			
		} catch (Exception e) {
			Window.alert("este es el error?: " + e.getMessage());
		}

	}	
}
