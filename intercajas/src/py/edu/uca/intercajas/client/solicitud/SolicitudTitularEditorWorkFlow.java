package py.edu.uca.intercajas.client.solicitud;

import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.UIDialog;
import py.edu.uca.intercajas.client.UIErrorRestDialog;
import py.edu.uca.intercajas.client.UIValidarFormulario;
import py.edu.uca.intercajas.client.beneficiario.ListaBeneficiarios;
import py.edu.uca.intercajas.client.menumail.Mailboxes.Images;
import py.edu.uca.intercajas.client.menumail.RefreshMailEvent;
import py.edu.uca.intercajas.shared.NuevaSolicitud;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Beneficiario;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.Solicitud;
import py.edu.uca.intercajas.shared.entity.SolicitudBeneficiario;
import py.edu.uca.intercajas.shared.entity.TiempoServicioDeclarado;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class SolicitudTitularEditorWorkFlow extends UIBase {

	interface Binder extends UiBinder<Widget, SolicitudTitularEditorWorkFlow> {
	}
	
	@UiField(provided = true) SolicitudTitularEditor solicitudTitularEditor;
	@UiField(provided = true) TablaTiempoServicioDeclarado tablaTiempoServicioDeclarado;
	@UiField UploadSolicitud upload;
	@UiField TextArea cuerpoMensaje;
	@UiField TablaSolicitudBeneficiario tablaSolicitudBeneficiario;
	@UiField CheckBox cotizanteSolicitante;
	@UiField TabLayoutPanel tab;
	
	Solicitud solicitudTitular;
	
	Images images = GWT.create(Images.class);
	

	public SolicitudTitularEditorWorkFlow() {
		
		titulo = "Nueva Solicitud";
		tablaTiempoServicioDeclarado = new TablaTiempoServicioDeclarado();
		solicitudTitularEditor = new SolicitudTitularEditor();
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		
		tab.getTabWidget(1).getParent().setVisible(false);
		cotizanteSolicitante.setValue(true);
		
	}

	@UiHandler("cancelar")
	void onCancel(ClickEvent event) {
		close();
	}
	
	@UiHandler("enviar")
	void onSave(ClickEvent event) {
		
		if (!formularioValido()) return;
		
		
		solicitudTitular.setEstado(Solicitud.Estado.Nuevo);	
		solicitudTitular.setExpedienteNumero(solicitudTitularEditor.numero.getValue());
		solicitudTitular.setCotizante(solicitudTitularEditor.beneficiario.getBeneficiario());
		
//		solicitudTitular.setListaTiempoServicioDeclarado(tablaTiempoServicioDeclarado.listaTiempoServicioDeclarado);

		//Creamos el mensaje
//		List<Mensaje> mensajes = new ArrayList<Mensaje>();
		Mensaje mensaje = new Mensaje();
		mensaje.setAsunto(Mensaje.Asunto.NuevaSolicitud);
		mensaje.setCuerpo(cuerpoMensaje.getValue());
		mensaje.setReferencia(solicitudTitular.getExpedienteNumero() + " - " + solicitudTitularEditor.beneficiario.getBeneficiario().toString());
//		mensaje.setAdjuntos(adjuntos);
		mensaje.setSolicitud(solicitudTitular);
//		mensajes.add(mensaje);
//		solicitudTitular.setMensajes(mensajes);

		NuevaSolicitud nuevaSolicitudTitular = new NuevaSolicitud(solicitudTitular, tablaTiempoServicioDeclarado.listaTiempoServicioDeclarado, mensaje, upload.adjuntos);

		if (cotizanteSolicitante.getValue() == true) {
			SolicitudBeneficiario sb = new SolicitudBeneficiario();
			//Como la solicitud es del titular, hacemos SolicitudBenefiario = al cotizante
			sb.setBeneficiario(solicitudTitularEditor.beneficiario.getBeneficiario());
			sb.setTipo(SolicitudBeneficiario.Tipo.Titular);
			nuevaSolicitudTitular.getListaSolicitudBeneficiario().add(sb);
		} else {
			
			SolicitudBeneficiario sb = null;
			for (int i=0; i<tablaSolicitudBeneficiario.listaBeneficiario.size(); i++) {
				sb = new SolicitudBeneficiario();
				sb.setTipo(SolicitudBeneficiario.Tipo.Derechohabiente);
				sb.setParentesco(tablaSolicitudBeneficiario.listaParentescoEditors.get(i).getValue());
				sb.setBeneficiario(tablaSolicitudBeneficiario.listaBeneficiario.get(i));
				nuevaSolicitudTitular.getListaSolicitudBeneficiario().add(sb);
			}
		}

		BeneficiarioService.Util.get().nuevoSolicitud(nuevaSolicitudTitular, new MethodCallback<Void>() {

			@Override
			public void onSuccess(Method method, Void response) {
				AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent());
				Window.alert("Solicuitud GENERADA!");
				close();
				
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				new UIErrorRestDialog(method, exception);
			}
		});

	}

	public void create() {
		solicitudTitular = new Solicitud();
	}
	
	@UiHandler("cotizanteSolicitante")
	public void cotizanteSolicitante(ClickEvent event) {

		if (cotizanteSolicitante.getValue()) {
			tab.getTabWidget(1).getParent().setVisible(false);
		} else {
			tab.getTabWidget(1).getParent().setVisible(true);
		}
		
	}
	
	public boolean formularioValido() {
		
		UIValidarFormulario vf = new UIValidarFormulario("Favor complete las siguientes informaciones solicitadas para crear la solicitud");
		
		if (upload.adjuntos[0] == null) {
			vf.addError("Es obligatorio enviar adjunto de la solicitud");
		}

		if (upload.adjuntos[1] == null) {
			vf.addError("Es obligatorio enviar adjunto del documento de identidad");
		}
		
		if (solicitudTitularEditor.beneficiario.getBeneficiario() == null) {
			vf.addError("Favor seleccione beneficiario cotizante");
		}

		Set<String> cajasDiferentes = new HashSet<String>();
		for (TiempoServicioDeclarado tsd : tablaTiempoServicioDeclarado.listaTiempoServicioDeclarado) {
			cajasDiferentes.add(tsd.getCaja().getSiglas());
		}
		if (cajasDiferentes.size() < 2) {
			vf.addError("Debe declarar al menos dos cajas de jubilaciones diferentes");
		}
		
		if (solicitudTitularEditor.numero.getValue().length() < 3) {
			vf.addError("El numero de Expediente debe contener al menos tres digitos");
		}
		
		if (cuerpoMensaje.getValue().length() == 0){
			vf.addError("Ingrese texto en el mensaje");
		}
		
		if (!cotizanteSolicitante.getValue()) {
			if (tablaSolicitudBeneficiario.listaBeneficiario.size() == 0) {
				vf.addError("Seleccione a los herederos");
			} else {
				int cantConyuges = 0;
				int cantAscendientes = 0;
				Set<Long> listaUnica = new HashSet<Long>();
				for (int i=0; i< tablaSolicitudBeneficiario.listaBeneficiario.size(); i++) {
					Beneficiario b = tablaSolicitudBeneficiario.listaBeneficiario.get(i);
					listaUnica.add(b.getId());
					if (solicitudTitularEditor.beneficiario.getBeneficiario() != null && b.getId() == solicitudTitularEditor.beneficiario.getBeneficiario().getId()) {
						vf.addError("El beneficiario cotizante " +  b.toString() + " no puede ser heredero al mismo tiempo");
					}
					
					if (tablaSolicitudBeneficiario.listaParentescoEditors.get(i).getValue() == SolicitudBeneficiario.Parentesco.Conyuge) {
						cantConyuges++;
					} else if (tablaSolicitudBeneficiario.listaParentescoEditors.get(i).getValue() == SolicitudBeneficiario.Parentesco.Ascendiente) {
						cantAscendientes++;
					}
				}
				
				if (tablaSolicitudBeneficiario.listaBeneficiario.size() != listaUnica.size()){
					vf.addError("Declaro mas una vez el mismo derechohabiente, favor verifique");
				}
				
				if (cantConyuges>1) {
					vf.addError("Declaro mas un derechohabiente con parentesco Conyuge, favor verifique");
				}
				
				if (cantAscendientes>2) {
					vf.addError("Declaro mas dos derechohabiente con parentesco Ascendiente, favor verifique");
				}

				
			}
		}
		
		return vf.esValido();
		
	}
	
	
}
