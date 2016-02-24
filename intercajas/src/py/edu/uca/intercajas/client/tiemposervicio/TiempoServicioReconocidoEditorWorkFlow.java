package py.edu.uca.intercajas.client.tiemposervicio;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.UIDialog;
import py.edu.uca.intercajas.client.UIErrorRestDialog;
import py.edu.uca.intercajas.client.UIValidarFormulario;
import py.edu.uca.intercajas.client.menumail.Mailboxes.Images;
import py.edu.uca.intercajas.client.menumail.RefreshMailEvent;
import py.edu.uca.intercajas.shared.NuevoReconocimientoTiempoServicio;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Destino;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.Solicitud;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class TiempoServicioReconocidoEditorWorkFlow extends UIBase {

	interface Binder extends UiBinder<Widget, TiempoServicioReconocidoEditorWorkFlow> {
	}
	
//	@UiField SolicitudTitularEditor solicitudTitularEditor;
	@UiField(provided = true) TablaTiempoServicioReconocido tablaTiempoServicioReconocido;
	@UiField UploadReconocimientoTiempoServicio upload;
	@UiField TextArea cuerpoMensaje;
	
	Solicitud solicitud;
	Destino destino;
	
	Images images = GWT.create(Images.class);
	
	public TiempoServicioReconocidoEditorWorkFlow(Destino destino) {
		
		titulo = "Nuevo Reconocimiento de Tiempo de Servicio";
		
		this.solicitud = destino.getMensaje().getSolicitud();
		this.destino   = destino; //usamos la fila del maillist (destino) para pasar al estado Atendido
		
		tablaTiempoServicioReconocido = new TablaTiempoServicioReconocido();
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		
	}

	@UiHandler("cancelar")
	void onCancel(ClickEvent event) {
		close();
	}
	
	@UiHandler("enviar")
	void onSave(ClickEvent event) {

		
		if (!formularioValido()) return;
		
		Mensaje mensaje = new Mensaje();
		mensaje.setAsunto(Mensaje.Asunto.ReconocimientoTiempoServicio);
		mensaje.setCuerpo(cuerpoMensaje.getValue());
		mensaje.setReferencia(solicitud.getExpedienteNumero() + " " + " falta el titular del beneficio");
		mensaje.setSolicitud(solicitud);

		NuevoReconocimientoTiempoServicio n = new NuevoReconocimientoTiempoServicio(solicitud, tablaTiempoServicioReconocido.listaTiempoServicioReconocido, mensaje, upload.adjuntos);

		BeneficiarioService.Util.get().nuevoReconocimientoTiempoServicio(n, new MethodCallback<Void>() {
			@Override
			public void onSuccess(Method method, Void response) {
				close();
				AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent());
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				new UIErrorRestDialog(method, exception);
			}
		});
		
		
//		BeneficiarioService.Util.get().findCajaDeclaradaBySolicitudId(1L, new MethodCallback<List<CajaDeclarada>>() {
//			@Override
//			public void onFailure(Method method, Throwable exception) {
//				Window.alert(exception.getMessage());
//			}
//			@Override
//			public void onSuccess(Method method, List<CajaDeclarada> response) {
//				for (CajaDeclarada e : response) {
//					Window.alert(e.getCaja().getSiglas());
//				}
//			}
//		});
	}

	public boolean formularioValido() {
		
		UIValidarFormulario vf = new UIValidarFormulario("Favor complete las siguientes informaciones solicitadas para reconocer tiempo de servicio");

		
		if (upload.adjuntos[0] == null) {
			vf.addError("Es obligatorio enviar adjunto del tiempo de reconocimento de servicios");
		}

		
//		//TODO y si no tiene aportes, va tiempo de servicios 0 ?
//		if (tablaTiempoServicioReconocido.listaTiempoServicioReconocido.size() == 0) {
//			vf.addError("Debe declarar el tiempo de servicio reconocido");
//		}
		
		if (cuerpoMensaje.getValue().length() == 0){
			vf.addError("Ingrese texto en el mensaje");
		}	
			
		return vf.esValido();
		
	}

	
}
