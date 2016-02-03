package py.edu.uca.intercajas.client.finiquito;

import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.IUploadStatus.Status;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.LoginService;
import py.edu.uca.intercajas.shared.NuevoConcedido;
import py.edu.uca.intercajas.shared.NuevoDenegado;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.UserDTO;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
import py.edu.uca.intercajas.shared.entity.Denegado;
import py.edu.uca.intercajas.shared.entity.SolicitudBeneficiario;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class UIConceder extends UIBase {

	private static UIDenegarUiBinder uiBinder = GWT
			.create(UIDenegarUiBinder.class);

	interface UIDenegarUiBinder extends UiBinder<Widget, UIConceder> {
	}

	
	@UiField FlowPanel upload;
	@UiField Label resumenUpload;
	@UiField TextArea cuerpoMensaje;
	
	@UiField DateBox fechaResolucion;
	@UiField TextBox numeroResolucion;
	@UiField Label tx;
	@UiField Label bx;
	@UiField IntegerBox bt;
	@UiField Label tmin;
	@UiField Button enviar;
	
	List<Adjunto> adjuntos = new ArrayList<Adjunto>();
	
	SolicitudBeneficiario solicitudBeneficiario;

	int txInt, tminInt;
	
	public UIConceder(SolicitudBeneficiario solicitudBeneficiario) {
		
		initWidget(uiBinder.createAndBindUi(this));
		
		this.solicitudBeneficiario = solicitudBeneficiario;
		
		MultiUploader defaultUploader = new MultiUploader();
		defaultUploader.setAvoidRepeatFiles(false);
		defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		defaultUploader.addOnStatusChangedHandler(onStatusChangedHandler);
		upload.add(defaultUploader);
		titulo = "Denegar Beneficio";
		
		
		//Obtenemos el valor de TMIN
		LoginService.Util.getInstance().loginFromSessionServer(new AsyncCallback<UserDTO>() {
			@Override
			public void onSuccess(UserDTO result) {
				tmin.setText(result.getCaja().getT_min().toString());
				tminInt = result.getCaja().getT_min();
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
		
		//Obtenemos la cajadeclarada, y asignamos el TX
		BeneficiarioService.Util.get().findCajaDeclaraadBySolicitudIdAndCurrentUser(solicitudBeneficiario.getId(), new MethodCallback<CajaDeclarada>() {
			@Override
			public void onSuccess(Method method, CajaDeclarada response) {
				tx.setText(response.getTxNeto().toString());
				txInt = response.getTxNeto();
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				// TODO mejorar esto
				Window.alert(exception.getMessage());
				close();
				
			}
		});

		

		
		
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

		NuevoConcedido nuevoConcedido = new NuevoConcedido();

		nuevoConcedido.setAdjuntos(adjuntos);
		nuevoConcedido.setCuerpoMensaje(cuerpoMensaje.getValue());
		nuevoConcedido.setNumeroResolucion(numeroResolucion.getValue());
		nuevoConcedido.setFechaResolucion(fechaResolucion.getValue());
		nuevoConcedido.setSolicitudBeneficiarioId(solicitudBeneficiario.getId());
		nuevoConcedido.setTx(Integer.valueOf(tx.getText()));
		nuevoConcedido.setTmin(Integer.valueOf(tmin.getText()));
		nuevoConcedido.setBt(new BigDecimal(bt.getValue()));
		nuevoConcedido.setBx(new BigDecimal(bx.getText()));

		BeneficiarioService.Util.get().conceder(nuevoConcedido, new MethodCallback<Void>() {
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
	
	@UiHandler("calcularBx")
	void onCalcularBx(ClickEvent event) {

		BigDecimal btBig = null;
		BigDecimal bxBig = null;
		BigDecimal txBig = null;
		BigDecimal tmin = null;
		
		try {
			btBig = new BigDecimal(bt.getValue());
		} catch (Exception e) {
			Window.alert("Favor ingrese un monto correcto en BT");
		}

		BigDecimal x = new BigDecimal((float)txInt/(float)tminInt);
		
		Window.alert("x: " + x.toString());
		
		bxBig = btBig.multiply(x).setScale(0, RoundingMode.HALF_UP);
		
		
		bx.setText(bxBig.toString());
				
		bt.setEnabled(false);
		enviar.setEnabled(true);
		
		
	}
	


}
