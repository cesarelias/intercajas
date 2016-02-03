package py.edu.uca.intercajas.client.finiquito;

import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.IUploadStatus.Status;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.shared.NuevoDenegado;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Denegado;
import py.edu.uca.intercajas.shared.entity.SolicitudBeneficiario;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class UIDenegar extends UIBase {

	private static UIDenegarUiBinder uiBinder = GWT
			.create(UIDenegarUiBinder.class);

	interface UIDenegarUiBinder extends UiBinder<Widget, UIDenegar> {
	}

	
	@UiField FlowPanel upload;
	@UiField Label resumenUpload;
	@UiField TextArea cuerpoMensaje;
	
	@UiField DateBox fechaResolucion;
	@UiField TextBox numeroResolucion;
	@UiField TextArea motivo;
	
	
	List<Adjunto> adjuntos = new ArrayList<Adjunto>();
	
	SolicitudBeneficiario solicitudBeneficiario;
	
	
	public UIDenegar(SolicitudBeneficiario solicitudBeneficiario) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.solicitudBeneficiario = solicitudBeneficiario;
		
		MultiUploader defaultUploader = new MultiUploader();
		defaultUploader.setAvoidRepeatFiles(false);
		defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		defaultUploader.addOnStatusChangedHandler(onStatusChangedHandler);
		upload.add(defaultUploader);
		titulo = "Denegar Beneficio";
		
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
	
	@UiHandler("cancelar")
	void onCancel(ClickEvent event){
		close();
	}

	
	@UiHandler("enviar")
	void onSave(ClickEvent event) {

		if (adjuntos.isEmpty()) {
			Window.alert("Es obligatorio enviar al menos un adjunto");
			return;
		}

		NuevoDenegado nuevoDenegado = new NuevoDenegado();

		nuevoDenegado.setNumeroResolucion(numeroResolucion.getValue());
		nuevoDenegado.setFechaResolucion(fechaResolucion.getValue());
		nuevoDenegado.setMovito(motivo.getValue());
		nuevoDenegado.setSolicitudBeneficiarioId(solicitudBeneficiario.getId());
		nuevoDenegado.setAdjuntos(adjuntos);
		nuevoDenegado.setCuerpoMensaje(cuerpoMensaje.getValue());

		BeneficiarioService.Util.get().denegar(nuevoDenegado, new MethodCallback<Void>() {
			@Override
			public void onSuccess(Method method, Void response) {
				Window.alert("Enviado!");
				close();
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				// TODO mejorar error
				Window.alert(exception.getMessage());
			}
		});
		
	}

}
