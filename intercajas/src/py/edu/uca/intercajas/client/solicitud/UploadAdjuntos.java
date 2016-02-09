package py.edu.uca.intercajas.client.solicitud;

import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;

import java.util.ArrayList;
import java.util.List;

import py.edu.uca.intercajas.client.menumail.Mailboxes.Images;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.Adjunto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

public class UploadAdjuntos extends UIBase {

	FlexTable uploadTable;
	Label resumenUpload;

	Images images = GWT.create(Images.class);
	
	Adjunto[] adjuntos = new Adjunto[2]; 

	public UploadAdjuntos() {
		
		uploadTable = new FlexTable();
		
		SingleUploader solicitud = createUploaderSolicutud();
		SingleUploader documentoIdentidad = createUploaderDocumentoIdentidad();
				
		uploadTable.setText(0, 0, "Solicitud");
		uploadTable.setWidget(0, 1, solicitud);
		uploadTable.setText(1, 0, "Dicumento de Identidad");
		uploadTable.setWidget(1, 1, documentoIdentidad);
		
		initWidget(uploadTable);
		
	}

	private IUploader.OnFinishUploaderHandler onFinishSolicitud = new IUploader.OnFinishUploaderHandler() {
	    public void onFinish(IUploader uploader) {
	      if (uploader.getStatus() == Status.SUCCESS) {
	    	  String[] archivos = uploader.getServerMessage().getMessage().split("\\|");
	    	  for (int i=0; i< archivos.length; i+=2) {
	    		  Adjunto a = new Adjunto();
	    		  a.setNombreArchivo(archivos[i]);
	    		  a.setRutaArchivo(archivos[i+1]);
	    		  adjuntos[0] = a;
	    		  
	    	  }
	    	  
	    	  uploadTable.setText(0, 1, adjuntos[0].getNombreArchivo());
	    	  Anchor a = new Anchor("eliminar");
	    	  uploadTable.setWidget(0, 2, a);
	      }
	    }
	};

	private IUploader.OnFinishUploaderHandler onFinishDocumentoIdentidad = new IUploader.OnFinishUploaderHandler() {
	    public void onFinish(IUploader uploader) {
	      if (uploader.getStatus() == Status.SUCCESS) {
	    	  String[] archivos = uploader.getServerMessage().getMessage().split("\\|");
	    	  for (int i=0; i< archivos.length; i+=2) {
	    		  Adjunto a = new Adjunto();
	    		  a.setNombreArchivo(archivos[i]);
	    		  a.setRutaArchivo(archivos[i+1]);
	    		  adjuntos[1] = a;
	    		  
	    	  }
	    	  uploadTable.setText(1, 1, adjuntos[1].getNombreArchivo());
	    	  Anchor a = new Anchor("eliminar");
	    	  uploadTable.setWidget(1, 1, a);

	      }
	    }
	};
	

	SingleUploader createUploaderSolicutud() {
		SingleUploader defaultUploader = new SingleUploader(FileInputType.LABEL);
		defaultUploader.setValidExtensions("pdf");
		defaultUploader.setMultipleSelection(false);
		defaultUploader.setAutoSubmit(true);
		defaultUploader.setAvoidRepeatFiles(false);
		defaultUploader.addOnFinishUploadHandler(onFinishSolicitud);
//		defaultUploader.addOnStatusChangedHandler(onStatusChangedHandler);
		return defaultUploader;
	}

	SingleUploader createUploaderDocumentoIdentidad() {
		SingleUploader defaultUploader = new SingleUploader(FileInputType.LABEL);
		defaultUploader.setValidExtensions("pdf");
		defaultUploader.setMultipleSelection(false);
		defaultUploader.setAutoSubmit(true);
		defaultUploader.setAvoidRepeatFiles(false);
		defaultUploader.addOnFinishUploadHandler(onFinishDocumentoIdentidad);
//		defaultUploader.addOnStatusChangedHandler(onStatusChangedHandler);
		return defaultUploader;
	}
	
	
}
