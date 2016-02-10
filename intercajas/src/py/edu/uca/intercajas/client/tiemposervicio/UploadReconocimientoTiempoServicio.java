package py.edu.uca.intercajas.client.tiemposervicio;

import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;

import py.edu.uca.intercajas.client.menumail.Mailboxes.Images;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.Adjunto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

public class UploadReconocimientoTiempoServicio extends UIBase {

	FlexTable uploadTable;
	Label resumenUpload;

	Images images = GWT.create(Images.class);
	
	Adjunto[] adjuntos = new Adjunto[1];
	
	Anchor eliminarReconocimientoTiempoServicio = new Anchor("eliminar");

	SingleUploader reconocimientoTiempoServicio;
	
	public UploadReconocimientoTiempoServicio() {

		uploadTable = new FlexTable();

		reconocimientoTiempoServicio = createUploader();

		uploadTable.setText(0, 0, "Reconocimiento de Tiempo deServicio");
		uploadTable.setWidget(0, 1, reconocimientoTiempoServicio);

		initWidget(uploadTable);
		
		addEliminarHander();

	}

	private IUploader.OnFinishUploaderHandler onFinish = new IUploader.OnFinishUploaderHandler() {
	    public void onFinish(IUploader uploader) {
	      if (uploader.getStatus() == Status.SUCCESS) {
	    	  String[] archivos = uploader.getServerMessage().getMessage().split("\\|");
	    	  for (int i=0; i< archivos.length; i+=2) {
	    		  Adjunto a = new Adjunto();
	    		  a.setNombreArchivo(archivos[i]);
	    		  a.setRutaArchivo(archivos[i+1]);
	    		  a.setTipo(Adjunto.Tipo.ReconocimientoTiempoServicio);
	    		  adjuntos[0] = a;
	    		  
	    	  }
	    	  
	    	  uploadTable.setText(0, 1, adjuntos[0].getNombreArchivo());
	    	  uploadTable.setWidget(0, 2, eliminarReconocimientoTiempoServicio);
	      }
	    }
	};

	SingleUploader createUploader() {
		SingleUploader defaultUploader = new SingleUploader(FileInputType.LABEL);
		defaultUploader.setValidExtensions("pdf");
		defaultUploader.setMultipleSelection(false);
		defaultUploader.setAutoSubmit(true);
		defaultUploader.setAvoidRepeatFiles(false);
		defaultUploader.addOnFinishUploadHandler(onFinish);
		return defaultUploader;
	}

	private void addEliminarHander() {
		eliminarReconocimientoTiempoServicio.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				adjuntos[1] = null;
				uploadTable.getWidget(0, 2).removeFromParent();
				uploadTable.setText(0, 1, "");
				uploadTable.setWidget(0, 2, reconocimientoTiempoServicio);
			}
		});
		
	}

}
