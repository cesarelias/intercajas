package py.edu.uca.intercajas.client.mensaje;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.menumail.RefreshMailEvent;
import py.edu.uca.intercajas.shared.NuevaAutorizacion;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Mensaje;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class UIAnular extends UIBase {

	private static UIDenegarUiBinder uiBinder = GWT
			.create(UIDenegarUiBinder.class);

	interface UIDenegarUiBinder extends UiBinder<Widget, UIAnular> {
	}

	
	@UiField TextArea observacion;
	@UiField Button anular;
	
	Mensaje mensaje;
	
	public UIAnular(Mensaje mensaje) {
		
		initWidget(uiBinder.createAndBindUi(this));
		
		this.mensaje = mensaje;
		
		titulo = "Anular envio de mensaje";
		
		
	}
	
	@UiHandler("cancelar")
	void onCancel(ClickEvent event){
		close();
	}

	
	@UiHandler("anular")
	void onSave(ClickEvent event) {

		mensaje.setObservacion(observacion.getValue());
		BeneficiarioService.Util.get().anular(mensaje, new MethodCallback<Void>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				//TODO mejorar esto
				Window.alert(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, Void response) {
				AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent());
				Window.alert("Mensaje anulado");
				close();
			}
		});

		
	}

}
