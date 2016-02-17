package py.edu.uca.intercajas.client.mensaje;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.TextCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.UIErrorRestDialog;
import py.edu.uca.intercajas.client.menumail.RefreshMailEvent;
import py.edu.uca.intercajas.shared.NuevaAutorizacion;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Destino;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.SolicitudBeneficiario;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class UIAutorizar extends UIBase {

	private static UIDenegarUiBinder uiBinder = GWT
			.create(UIDenegarUiBinder.class);

	interface UIDenegarUiBinder extends UiBinder<Widget, UIAutorizar> {
	}

	
	@UiField UploadAutorizar upload;
	
	@UiField TextArea observacion;
	@UiField Button autorizar;
	@UiField HTML detalle;
	
	Mensaje mensaje;
	Destino destino;
	
	public UIAutorizar(Destino destino) {
		
		initWidget(uiBinder.createAndBindUi(this));
		
		this.mensaje = destino.getMensaje();
		this.destino = destino;
		
		titulo = "Autorizar y enviar mensaje";
		
		agregarDetalle();
		
		
	}
	

	@UiHandler("cancelar")
	void onCancel(ClickEvent event){
		close();
	}

	
	@UiHandler("autorizar")
	void onSave(ClickEvent event) {

		for(Adjunto a : upload.adjuntos) {
			if (a == null) {
				Window.alert("Es obligatorio enviar adjunto");
				return;
			}
		}

		NuevaAutorizacion nuevaAutorizacion = new NuevaAutorizacion();
		nuevaAutorizacion.setAdjuntos(upload.adjuntos);
		nuevaAutorizacion.setMensaje_id(mensaje.getId());
		nuevaAutorizacion.setObservacion(observacion.getValue());
		
		BeneficiarioService.Util.get().autorizar(nuevaAutorizacion, new MethodCallback<Void>() {
			
			@Override
			public void onSuccess(Method method, Void response) {
				AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent());
				Window.alert("El mensaje fue autorizado y enviado!");
				close();
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				new UIErrorRestDialog(method, exception);
			}
		});
		
		
	}

	private void agregarDetalle() {
		
		BeneficiarioService.Util.get().detalleAutorizarMensaje(destino.getMensaje().getId(), new TextCallback() {
			
			@Override
			public void onSuccess(Method method, String response) {
				// TODO Auto-generated method stub
				detalle.setHTML(response);
				
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				new UIErrorRestDialog(method, exception);
			}
		});
		
	}
}
