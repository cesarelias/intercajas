package py.edu.uca.intercajas.client.solicitud;

import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;
import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.UIErrorRestDialog;
import py.edu.uca.intercajas.client.UIValidarFormulario;
import py.edu.uca.intercajas.client.menumail.Mailboxes.Images;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.Adjunto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UploadSolicitud extends UIBase {

	FlexTable uploadTable;
	Label resumenUpload;

	Images images = GWT.create(Images.class);
	
	Adjunto[] adjuntos = new Adjunto[2];
	
	Anchor eliminarSolicitud = new Anchor("eliminar");
	Anchor eliminarDocumentoIdentidad = new Anchor("eliminar");

	SingleUploader solicitud;
	SingleUploader documentoIdentidad;
	
	public UploadSolicitud() {

		uploadTable = new FlexTable();

		solicitud = createUploaderSolicutud();
		documentoIdentidad = createUploaderDocumentoIdentidad();

		
		uploadTable.setText(0, 0, "Solicitud");
		uploadTable.setWidget(0, 1, solicitud);
		uploadTable.setText(1, 0, "Dicumento de Identidad");
		uploadTable.setWidget(1, 1, documentoIdentidad);

		VerticalPanel v = new VerticalPanel();
		DecoratorPanel dp = new DecoratorPanel();
		dp.add(uploadTable);
		v.add(new Label("Adjuntos"));
		v.add(dp);
		
		initWidget(v);
		
		addEliminarHander();
		
	}

	private IUploader.OnFinishUploaderHandler onFinishSolicitud = new IUploader.OnFinishUploaderHandler() {
	    public void onFinish(IUploader uploader) {
	      if (uploader.getStatus() == Status.SUCCESS) {
	    	  
	    	  if (uploader.getServerMessage().getMessage() == "ErrorFirma") {
	    		  UIValidarFormulario vf = new UIValidarFormulario("Firma no valida:");
	    		  vf.addError("Verifique la firma digital del documento adjunto");
	    		  vf.esValido();
	    		  return;
	    	  }
	    	  
	    	  String[] archivos = uploader.getServerMessage().getMessage().split("\\|");
    		  Adjunto a = new Adjunto();
    		  a.setRutaArchivo(archivos[0]);
    		  a.setNombreArchivo(archivos[1]);
    		  a.setTipo(Adjunto.Tipo.Solicitud);
    		  adjuntos[0] = a;
	    		  
	    	  
	    	  uploadTable.setText(0, 1, archivos[2]);
	    	  uploadTable.setWidget(0, 2, eliminarSolicitud);
	    	  
	      }
	    }
	};

	private IUploader.OnFinishUploaderHandler onFinishDocumentoIdentidad = new IUploader.OnFinishUploaderHandler() {
	    public void onFinish(IUploader uploader) {
	      if (uploader.getStatus() == Status.SUCCESS) {
	    	  
	    	  if (uploader.getServerMessage().getMessage() == "ErrorFirma") {
	    		  UIValidarFormulario vf = new UIValidarFormulario("Firma no valida:");
	    		  vf.addError("Verifique la firma digital del documento adjunto");
	    		  vf.esValido();
	    		  return;
	    	  }
	    	  
	    	  
	    	  String[] archivos = uploader.getServerMessage().getMessage().split("\\|");
    		  Adjunto a = new Adjunto();
    		  a.setRutaArchivo(archivos[0]);
    		  a.setNombreArchivo(archivos[1]);
    		  a.setTipo(Adjunto.Tipo.DocumentoIdentidad);
    		  adjuntos[1] = a;
    		  
	    	  uploadTable.setText(1, 1, archivos[2]);
	    	  uploadTable.setWidget(1, 2, eliminarDocumentoIdentidad);
	      }
	    }
	};
	

	SingleUploader createUploaderSolicutud() {
		try {
		SingleUploader defaultUploader = new SingleUploader(FileInputType.LABEL);
		defaultUploader.setValidExtensions("pdf");
		defaultUploader.setMultipleSelection(false);
		defaultUploader.setAutoSubmit(true);
		defaultUploader.setAvoidRepeatFiles(false);
		defaultUploader.addOnFinishUploadHandler(onFinishSolicitud);
		return defaultUploader;
		} catch (Exception e) {
			UIValidarFormulario vf = new UIValidarFormulario("Favor verifique");
			vf.addError(e.getMessage());
			vf.esValido();
			return null;
		}
		
	}

	SingleUploader createUploaderDocumentoIdentidad() {
		SingleUploader defaultUploader = new SingleUploader(FileInputType.LABEL);
		defaultUploader.setValidExtensions("pdf");
		defaultUploader.setMultipleSelection(false);
		defaultUploader.setAutoSubmit(true);
		defaultUploader.setAvoidRepeatFiles(false);
		defaultUploader.addOnFinishUploadHandler(onFinishDocumentoIdentidad);
		return defaultUploader;
	}
	
	private void addEliminarHander() {
		eliminarDocumentoIdentidad.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				adjuntos[1] = null;
				uploadTable.getWidget(1, 2).removeFromParent();
				uploadTable.setText(1, 1, "");
				uploadTable.setWidget(1, 2, documentoIdentidad);
			}
		});
		
		eliminarSolicitud.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				adjuntos[0] = null;
				uploadTable.getWidget(0, 2).removeFromParent();
				uploadTable.setText(0, 1, "");
				uploadTable.setWidget(0, 2, solicitud);
			}
		});
	}

	
	
}
