package py.edu.uca.intercajas.client.finiquito;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.LoginService;
import py.edu.uca.intercajas.client.UIDialog;
import py.edu.uca.intercajas.client.UIErrorRestDialog;
import py.edu.uca.intercajas.client.UIValidarFormulario;
import py.edu.uca.intercajas.client.menumail.RefreshMailEvent;
import py.edu.uca.intercajas.shared.CalculoTiempo;
import py.edu.uca.intercajas.shared.NuevoConcedido;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.UserDTO;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
import py.edu.uca.intercajas.shared.entity.Destino;
import py.edu.uca.intercajas.shared.entity.SolicitudBeneficiario;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
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

	
	@UiField UploadConceder upload;
	@UiField TextArea cuerpoMensaje;
	
	@UiField TextBox numeroResolucion;
	@UiField Label tx;
	@UiField Label bx;
	@UiField IntegerBox bt;
	@UiField Label tmin;
	@UiField Button enviar;
	@UiField Button calcularBx;

	BigDecimal bxBig;
	
	SolicitudBeneficiario solicitudBeneficiario;
	Destino destino;

	int txInt, tminInt;
	
	public UIConceder(SolicitudBeneficiario solicitudBeneficiario, Destino destino) {
		
		initWidget(uiBinder.createAndBindUi(this));
		
		this.solicitudBeneficiario = solicitudBeneficiario;
		this.destino = destino;
		
		titulo = "Conceder Beneficio";
		
		//Obtenemos el valor de TMIN
		LoginService.Util.getInstance().loginFromSessionServer(new AsyncCallback<UserDTO>() {
			@Override
			public void onSuccess(UserDTO result) {
				tmin.setText(CalculoTiempo.leeMeses(result.getCaja().getT_min()));
				tminInt = result.getCaja().getT_min();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
		});
		
		
		//Obtenemos la cajadeclarada, y asignamos el TX
		BeneficiarioService.Util.get().findCajaDeclaraadBySolicitudIdAndCurrentUser(solicitudBeneficiario.getSolicitud().getId(), new MethodCallback<CajaDeclarada>() {
			@Override
			public void onSuccess(Method method, CajaDeclarada response) {
				tx.setText(CalculoTiempo.leeMeses(response.getTxNeto()));
				txInt = response.getTxNeto();
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				new UIErrorRestDialog(method, exception);
			}
		});

		

		
		
	}
	
	@UiHandler("cancelar")
	void onCancel(ClickEvent event){
		close();
	}

	
	@UiHandler("enviar")
	void onSave(ClickEvent event) {

		if (!formularioValido()) return;

		NuevoConcedido nuevoConcedido = new NuevoConcedido();

		nuevoConcedido.setAdjuntos(upload.adjuntos);
		nuevoConcedido.setCuerpoMensaje(cuerpoMensaje.getValue());
		nuevoConcedido.setNumeroResolucion(numeroResolucion.getValue());
		nuevoConcedido.setSolicitudBeneficiarioId(solicitudBeneficiario.getId());
		nuevoConcedido.setTx(txInt);
		nuevoConcedido.setTmin(tminInt);
		nuevoConcedido.setBt(new BigDecimal(bt.getValue()));
		nuevoConcedido.setBx(bxBig);
		nuevoConcedido.setDestino_id(destino.getId());

		BeneficiarioService.Util.get().conceder(nuevoConcedido, new MethodCallback<Void>() {
			@Override
			public void onSuccess(Method method, Void response) {
				AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent());
				close();
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				new UIErrorRestDialog(method, exception);
			}
		});
		
	}
	
	@UiHandler("calcularBx")
	void onCalcularBx(ClickEvent event) {

		BigDecimal btBig = null;
//		BigDecimal bxBig = null;
		BigDecimal txBig = null;
		BigDecimal tmin = null;
		
		try {
			btBig = new BigDecimal(bt.getValue());
		} catch (Exception e) {
			Window.alert("Favor ingrese un monto correcto en BT");
		}

		BigDecimal x = new BigDecimal((float)txInt/(float)tminInt);
		
		
		bxBig = btBig.multiply(x).setScale(0, RoundingMode.HALF_UP);
		
		bx.setText(NumberFormat.getFormat("#,##0").format(bxBig));

		
		bt.setEnabled(false);
		enviar.setEnabled(true);
		calcularBx.setEnabled(false);
		
	}
	

	public boolean formularioValido() {
		
		UIValidarFormulario vf = new UIValidarFormulario("Favor complete las siguientes informaciones solicitadas para crear la concesion de beneficio");

		if (upload.adjuntos[0] == null) {
			vf.addError("Es obligatorio enviar adjunto la Resolucion");
		}
		
		if (upload.adjuntos[1] == null) {
			vf.addError("Es obligatorio enviar adjunto la Liquidacion");
		}

		if (cuerpoMensaje.getValue().length() == 0){
			vf.addError("Ingrese texto en el mensaje");
		}
		
		if (numeroResolucion.getValue().length() == 0) {
			vf.addError("Es obligatorio ingresar el numero de resolucion");
		}
		
		return vf.esValido();
		
	}

}
