package py.edu.uca.intercajas.client.solicitud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.menumail.Mailboxes.Images;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.UIDialog;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.Solicitud;
import py.edu.uca.intercajas.shared.entity.SolicitudTitular;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class SolicitudTitularEditorWorkFlow extends UIBase {

	interface Binder extends UiBinder<Widget, SolicitudTitularEditorWorkFlow> {
	}
	
	@UiField(provided = true) SolicitudTitularEditor solicitudTitularEditor;
	@UiField(provided = true) TablaTiempoServicioDeclarado tablaTiempoServicioDeclarado;
	@UiField FlowPanel upload;
	@UiField Label resumenUpload;
	
	SolicitudTitular solicitudTitular;
	
	Images images = GWT.create(Images.class);
	
	//Map<String, String> adjuntos = new HashMap<String, String>();
	
	List<Adjunto> adjuntos = new ArrayList<Adjunto>();
	
	public SolicitudTitularEditorWorkFlow(SimpleEventBus eventBus) {
//		title = "Solicitud Titular";
		this.eventBus = eventBus;
		tablaTiempoServicioDeclarado = new TablaTiempoServicioDeclarado(eventBus);
		solicitudTitularEditor = new SolicitudTitularEditor(eventBus);
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		
		MultiUploader defaultUploader = new MultiUploader();
		defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		defaultUploader.addOnStatusChangedHandler(onStatusChangedHandler);
		upload.add(defaultUploader);
		
	}

	@UiHandler("cancelar")
	void onCancel(ClickEvent event) {
		
		for (Adjunto a : adjuntos) {
			Window.alert(a.getNombreArchivo());
		}
//		close();
	}
	
	@UiHandler("enviar")
	void onSave(ClickEvent event) {
		
		
		if (adjuntos.isEmpty()) {
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
		
		try { 
			
			
		solicitudTitular.setEstado(Solicitud.Estado.NuevoSolicitado);	
		solicitudTitular.setNumero(solicitudTitularEditor.numero.getValue());
		solicitudTitular.setFecha(solicitudTitularEditor.fecha.getValue());
		solicitudTitular.setBeneficiario(solicitudTitularEditor.beneficiario.getBeneficiario());
		solicitudTitular.setListaTiempoServicioDeclarado(tablaTiempoServicioDeclarado.listaTiempoServicioDeclarado);

		} catch (Exception e) {
			Window.alert(e.getMessage());
		}
		BeneficiarioService.Util.get().nuevoSolicitudTitular(solicitudTitular, new MethodCallback<Void>() {
			
			@Override
			public void onSuccess(Method method, Void response) {
				close();
				Window.alert("Solicuitud GENERADA! .... Pero faltan las validaciones del formularo, no olvidar...!! ahh y tambien debe ya ENVIAR el formulario de una a todas las cajas declaradas!");
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				Window.alert(exception.getMessage());
				new UIDialog("Error",new HTML(method.getResponse().getText()));
			}
		});

	}

//	@UiHandler("agregarAdjunto")
//	void onAgregarAdjunto(ClickEvent event) {
//		int numRows = adjuntos.getRowCount();
//		adjuntos.setWidget(numRows, 0,  new PushButton(new Image(images.trash())));
//		adjuntos.setWidget(numRows, 1, new Hyperlink("Adjunto", "Adjuntito"));
////		adjuntos.getFlexCellFormatter().setRowSpan(0, 1, 5);
//		
//	}
	
	public void create() {
		try {
			solicitudTitular = new SolicitudTitular();
		} catch (Exception e) {
			Window.alert("este es el error?: " + e.getMessage());
		}

	}
	
	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
	    public void onFinish(IUploader uploader) {
	      if (uploader.getStatus() == Status.SUCCESS) {
	    	  String[] archivos = uploader.getServerMessage().getMessage().split("\\|");
	    	  for (int i=0; i< archivos.length; i+=2) {
	    		  Adjunto a = new Adjunto();
	    		  a.setNombreArchivo(archivos[i] + "|" + archivos[i+1]);
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
	    			  if (adjuntos.get(ii).getNombreArchivo().equals(archivos[i] + "|" + archivos[i+1])) {
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
