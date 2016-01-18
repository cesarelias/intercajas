package py.edu.uca.intercajas.client.solicitud;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.server.entity.SolicitudTitular;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.UIDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
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
		
		Window.alert("entro aqui");
		try { 
			
		solicitudTitular.setNumero(solicitudTitularEditor.numero.getValue());
		solicitudTitular.setFecha(solicitudTitularEditor.fecha.getValue());
		solicitudTitular.setBeneficiario(solicitudTitularEditor.beneficiario.getBeneficiario());
		solicitudTitular.setListaPeriodoAporteDeclarados(tablaPeriodoAporteDeclarado.listaPeriodoAporteDeclarado);

		} catch (Exception e) {
			Window.alert(e.getMessage());
		}
		BeneficiarioService.Util.get().nuevoSolicitudTitular(solicitudTitular, new MethodCallback<Void>() {
			
			@Override
			public void onSuccess(Method method, Void response) {
				close();
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				Window.alert(exception.getMessage());
				new UIDialog("Error",new HTML(method.getResponse().getText()));
			}
		});

	}

	public void create() {
		try {
			solicitudTitular = new SolicitudTitular();
		} catch (Exception e) {
			Window.alert("este es el error?: " + e.getMessage());
		}

	}	
}
