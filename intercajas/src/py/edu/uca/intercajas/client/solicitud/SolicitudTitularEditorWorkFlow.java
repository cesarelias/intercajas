package py.edu.uca.intercajas.client.solicitud;

import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.menumail.Mailboxes.Images;
import py.edu.uca.intercajas.client.menumail.RefreshMailEvent;
import py.edu.uca.intercajas.shared.NuevaSolicitud;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.UIDialog;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.Solicitud;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class SolicitudTitularEditorWorkFlow extends UIBase {

	interface Binder extends UiBinder<Widget, SolicitudTitularEditorWorkFlow> {
	}
	
	@UiField(provided = true) SolicitudTitularEditor solicitudTitularEditor;
	@UiField(provided = true) TablaTiempoServicioDeclarado tablaTiempoServicioDeclarado;
	@UiField UploadAdjuntos upload;
	@UiField TextArea cuerpoMensaje;
	
	Solicitud solicitudTitular;
	
	Images images = GWT.create(Images.class);
	

	public SolicitudTitularEditorWorkFlow() {
		
//		title = "Solicitud Titular";
		tablaTiempoServicioDeclarado = new TablaTiempoServicioDeclarado();
		solicitudTitularEditor = new SolicitudTitularEditor();
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		
	}

	@UiHandler("cancelar")
	void onCancel(ClickEvent event) {
		
		//close();
	}
	
	@UiHandler("enviar")
	void onSave(ClickEvent event) {
		
		
		if (upload.adjuntos.length == 0) {
			Window.alert("Es obligatorio enviar al menos un adjunto");
			return;
		}
				
		//TODO falta agregar la validacion del formulario
		/* 1. Beneficiario seleccionado (no nulo)
		 * 2. Fecha de solicitud no puede ser futura, ni menor a la fecha de la ley de intercajas
		 * 3. Periodos declarados no puede estar vacio
		 * 4. Periodos declarados debe contener al menos dos cajas en cuestion
		 * 5. El numero de solicitud debe ser autogenerado? o tenes ambos (agregar numero expediente interno)
		 * 6. Agregar insertarEnvio -- con los adjutos.!
		 */
		
		solicitudTitular.setEstado(Solicitud.Estado.Nuevo);	
		solicitudTitular.setNumero(solicitudTitularEditor.numero.getValue());
		solicitudTitular.setCotizante(solicitudTitularEditor.beneficiario.getBeneficiario());
		
//		solicitudTitular.setListaTiempoServicioDeclarado(tablaTiempoServicioDeclarado.listaTiempoServicioDeclarado);

		//Creamos el mensaje
//		List<Mensaje> mensajes = new ArrayList<Mensaje>();
		Mensaje mensaje = new Mensaje();
		mensaje.setAsunto(Mensaje.Asunto.NuevaSolicitud);
		mensaje.setCuerpo(cuerpoMensaje.getValue());
		mensaje.setReferencia(solicitudTitular.getNumero() + " - " + solicitudTitularEditor.beneficiario.getBeneficiario().toString());
//		mensaje.setAdjuntos(adjuntos);
		mensaje.setSolicitud(solicitudTitular);
//		mensajes.add(mensaje);
//		solicitudTitular.setMensajes(mensajes);

		NuevaSolicitud nuevaSolicitudTitular = new NuevaSolicitud(solicitudTitular, tablaTiempoServicioDeclarado.listaTiempoServicioDeclarado, mensaje, upload.adjuntos);
		
		BeneficiarioService.Util.get().nuevoSolicitud(nuevaSolicitudTitular, new MethodCallback<Void>() {

			@Override
			public void onSuccess(Method method, Void response) {
				AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent());
				Window.alert("Solicuitud GENERADA!");
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
		solicitudTitular = new Solicitud();
	}
	
}
