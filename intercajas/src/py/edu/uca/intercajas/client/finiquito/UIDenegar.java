package py.edu.uca.intercajas.client.finiquito;

import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.IUploadStatus.Status;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.menumail.RefreshMailEvent;
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

	
	@UiField UploadDenegar upload;
	@UiField TextArea cuerpoMensaje;
	
	@UiField DateBox fechaResolucion;
	@UiField TextBox numeroResolucion;
	@UiField TextArea motivo;
	
	
	SolicitudBeneficiario solicitudBeneficiario;
	
	
	public UIDenegar(SolicitudBeneficiario solicitudBeneficiario) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.solicitudBeneficiario = solicitudBeneficiario;
		
		titulo = "Denegar Beneficio";
		
	}
	
	@UiHandler("cancelar")
	void onCancel(ClickEvent event){
		close();
	}

	
	@UiHandler("enviar")
	void onSave(ClickEvent event) {

		for(Adjunto a : upload.adjuntos) {
			if (a == null) {
				Window.alert("Es obligatorio enviar adjunto");
				return;
			}
		}


		NuevoDenegado nuevoDenegado = new NuevoDenegado();

		nuevoDenegado.setNumeroResolucion(numeroResolucion.getValue());
		nuevoDenegado.setFechaResolucion(fechaResolucion.getValue());
		nuevoDenegado.setMovito(motivo.getValue());
		nuevoDenegado.setSolicitudBeneficiarioId(solicitudBeneficiario.getId());
		nuevoDenegado.setAdjuntos(upload.adjuntos);
		nuevoDenegado.setCuerpoMensaje(cuerpoMensaje.getValue());

		BeneficiarioService.Util.get().denegar(nuevoDenegado, new MethodCallback<Void>() {
			@Override
			public void onSuccess(Method method, Void response) {
				AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent());
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
