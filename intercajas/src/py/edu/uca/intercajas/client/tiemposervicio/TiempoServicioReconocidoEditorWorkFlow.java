package py.edu.uca.intercajas.client.tiemposervicio;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.LoginService;
import py.edu.uca.intercajas.client.menumail.Mailboxes.Images;
import py.edu.uca.intercajas.client.solicitud.events.SolicitudCreatedEvent;
import py.edu.uca.intercajas.shared.NuevoReconocimientoTiempoServicio;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.UIDialog;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.Solicitud;
import py.edu.uca.intercajas.shared.entity.SolicitudTitular;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class TiempoServicioReconocidoEditorWorkFlow extends UIBase {

	interface Binder extends UiBinder<Widget, TiempoServicioReconocidoEditorWorkFlow> {
	}
	
//	@UiField SolicitudTitularEditor solicitudTitularEditor;
	@UiField(provided = true) TablaTiempoServicioReconocido tablaTiempoServicioReconocido;
	@UiField FlowPanel upload;
	@UiField Label resumenUpload;
	@UiField TextArea cuerpoMensaje;
	
	Solicitud solicitud;
	
	Images images = GWT.create(Images.class);
	
	List<Adjunto> adjuntos = new ArrayList<Adjunto>();

	public TiempoServicioReconocidoEditorWorkFlow(Solicitud solicitud) {
		
		this.solicitud = solicitud;
		
		tablaTiempoServicioReconocido = new TablaTiempoServicioReconocido();
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		
		MultiUploader defaultUploader = new MultiUploader();
		defaultUploader.setAvoidRepeatFiles(false);
		defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		defaultUploader.addOnStatusChangedHandler(onStatusChangedHandler);
		upload.add(defaultUploader);
		
	}

	@UiHandler("cancelar")
	void onCancel(ClickEvent event) {
		close();
	}
	
	@UiHandler("enviar")
	void onSave(ClickEvent event) {

		if (adjuntos.isEmpty()) {
			Window.alert("Es obligatorio enviar al menos un adjunto");
			return;
		}
		
		Mensaje mensaje = new Mensaje();
		mensaje.setAsunto(Mensaje.Asunto.ReconocimientoTiempoServicio);
		mensaje.setCuerpo(cuerpoMensaje.getValue());
		mensaje.setReferencia(solicitud.getNumero() + " " + " falta el titular del beneficio");
		mensaje.setSolicitud(solicitud);

		NuevoReconocimientoTiempoServicio n = new NuevoReconocimientoTiempoServicio(solicitud, tablaTiempoServicioReconocido.listaTiempoServicioReconocido, mensaje, adjuntos);

		BeneficiarioService.Util.get().nuevoReconocimientoTiempoServicio(n, new MethodCallback<Void>() {
			@Override
			public void onSuccess(Method method, Void response) {
				close();
				AppUtils.EVENT_BUS.fireEvent(new SolicitudCreatedEvent(null)); //esto refresca el MailList;
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				Window.alert(exception.getMessage());
				new UIDialog("Error",new HTML(method.getResponse().getText()));
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

	public void create() {

	}
	
	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
	    public void onFinish(IUploader uploader) {
	      if (uploader.getStatus() == Status.SUCCESS) {
	    	  String[] archivos = uploader.getServerMessage().getMessage().split("\\|");
	    	  for (int i=0; i< archivos.length; i+=2) {
	    		  Adjunto a = new Adjunto();
	    		  a.setNombreArchivo(archivos[i]);
	    		  a.setRutaArchivo(archivos[i+1]);
	    		  adjuntos.add(a);
	    	  }
	    	  refreshResumenUpload();
	      }
	    }
	};

	private IUploader.OnStatusChangedHandler onStatusChangedHandler = new IUploader.OnStatusChangedHandler() {
		
		@Override
		public void onStatusChanged(IUploader uploader) {
			if (uploader.getStatus() == Status.DELETED) {
				if (uploader.getServerMessage().getMessage() == null) { 
					return;
				}
	    	  String[] archivos = uploader.getServerMessage().getMessage().split("\\|");
	    	  for (int i=0; i< archivos.length; i+=2) {
	    		  for (int ii=0; ii< adjuntos.size(); ii++) {
	    			  if (adjuntos.get(ii).getNombreArchivo().equals(archivos[i])) {
	    				  adjuntos.remove(ii);
	    				  break;
	    			  }
	    		  }
	    		  //adjuntos.remove(archivos[i]);
	    	  }
	    	  refreshResumenUpload();
			}
		}
	};
	
	private void refreshResumenUpload() {
		if (adjuntos.isEmpty()) {
			resumenUpload.setText("");
		} else if (adjuntos.size() == 1) {
			resumenUpload.setText(adjuntos.size() + " archivo listo");
		} else {
			resumenUpload.setText(adjuntos.size() + " archivos listos");
		}
	}

}
