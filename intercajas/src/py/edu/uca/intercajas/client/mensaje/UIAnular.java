package py.edu.uca.intercajas.client.mensaje;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.UIErrorRestDialog;
import py.edu.uca.intercajas.client.UIValidarFormulario;
import py.edu.uca.intercajas.client.menumail.RefreshMailEvent;
import py.edu.uca.intercajas.shared.NuevaAnulacion;
import py.edu.uca.intercajas.shared.NuevaAutorizacion;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Destino;
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
	Destino destino;
	
	public UIAnular(Mensaje mensaje, Destino destino) {
		
		initWidget(uiBinder.createAndBindUi(this));
		
		this.mensaje = mensaje;
		this.destino = destino;
		
		titulo = "Anular envio de mensaje";
		
		
	}
	
	@UiHandler("cancelar")
	void onCancel(ClickEvent event){
		close();
	}

	
	@UiHandler("anular")
	void onSave(ClickEvent event) {

		if (!formularioValido()) return;
		
		NuevaAnulacion nuevaAnulacion = new NuevaAnulacion();
		nuevaAnulacion.setMensaje_id(mensaje.getId());
		nuevaAnulacion.setObvervacion(observacion.getValue());
		
		BeneficiarioService.Util.get().anular(nuevaAnulacion, new MethodCallback<Void>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				new UIErrorRestDialog(method, exception);
			}

			@Override
			public void onSuccess(Method method, Void response) {
				AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent());
				close();
			}
		});
		
	}
	
	public boolean formularioValido() {
		
		UIValidarFormulario vf = new UIValidarFormulario("Favor complete las siguientes informaciones solicitadas para denegar beneficio");
		
		if (observacion.getValue().length() == 0) {
			vf.addError("Es obligatorio ingresar la observacion");
		}
		
		return vf.esValido();
		
	}

}
