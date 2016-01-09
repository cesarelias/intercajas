package py.edu.uca.intercajas.client.beneficiario;

import java.util.HashMap;
import java.util.Map;

import py.edu.uca.intercajas.client.menumail.MenuMail;
import py.edu.uca.intercajas.client.requestfactory.BeneficiarioProxy;
import py.edu.uca.intercajas.client.requestfactory.ContextGestionBeneficiario;
import py.edu.uca.intercajas.client.requestfactory.DireccionProxy;
import py.edu.uca.intercajas.client.requestfactory.DocumentoIdentidadProxy;
import py.edu.uca.intercajas.client.requestfactory.FactoryGestion;
import py.edu.uca.intercajas.server.entity.enums.Sexo;
import py.edu.uca.intercajas.server.entity.enums.TipoDocumentoIdentidad;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.UIDialog;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DefaultDateTimeFormatInfo;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.impl.cldr.DateTimeFormatInfoImpl_es;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class UIBeneficiario extends UIBase  {

	private static UIBeneficiarioUiBinder uiBinder = GWT
			.create(UIBeneficiarioUiBinder.class);

	interface UIBeneficiarioUiBinder extends UiBinder<Widget, UIBeneficiario> {
	}
	
	private static  EventBus EVENTBUS = new SimpleEventBus();
	private static  FactoryGestion FACTORY  = GWT.create(FactoryGestion.class);


	@UiField Button guardar;
	@UiField Button volver;
	@UiField RadioButton tipoDocumento0;
	@UiField RadioButton tipoDocumento1;
	@UiField TextBox numeroDocumento;
	@UiField TextBox nombres;
	@UiField DateBox fechaNacimiento;
	@UiField TextBox apellidos;
	@UiField RadioButton sexo0;
	@UiField RadioButton sexo1;
	@UiField TextBox callePrincipal;
	@UiField TextBox numeroCasa;
	@UiField TextBox barrio;
	@UiField TextBox ciudad;
	@UiField TextBox departamento;
	

	public UIBeneficiario() {

		initWidget(uiBinder.createAndBindUi(this));
		//TODO la fecha trae con hora, falta sacar la hora!
	    DateTimeFormat dateFormat=DateTimeFormat.getFormat("dd/MM/yyyy");
		fechaNacimiento.setFormat(new DateBox.DefaultFormat(dateFormat));
	}
	
	public UIBeneficiario(EventBus eventBus) {

		this.EVENTBUS = eventBus;
		initWidget(uiBinder.createAndBindUi(this));
		//TODO la fecha trae con hora, falta sacar la hora!
	    DateTimeFormat dateFormat=DateTimeFormat.getFormat("dd/MM/yyyy");
		fechaNacimiento.setFormat(new DateBox.DefaultFormat(dateFormat));
	}

	@UiHandler("volver")
	void volver(ClickEvent e) {
		volver();
	}
	
	@UiHandler("guardar")
	void guardar(ClickEvent e) {
		
		FACTORY.initialize(EVENTBUS);
		
		ContextGestionBeneficiario context = FACTORY.contextGestionBeneficiario();
		
		BeneficiarioProxy beneficiarioProxy = context.create(BeneficiarioProxy.class);
		DocumentoIdentidadProxy docProxy = context.create(DocumentoIdentidadProxy.class);
		DireccionProxy direccionProxy = context.create(DireccionProxy.class);
		
		docProxy.setTipoDocumento((tipoDocumento0.isChecked())?TipoDocumentoIdentidad.CEDULA:TipoDocumentoIdentidad.PASAPORTE);
		docProxy.setNumeroDocumento(numeroDocumento.getValue());
		
		direccionProxy.setNumeroCasa(numeroCasa.getValue());
		direccionProxy.setCallePrincipal(callePrincipal.getValue());
		direccionProxy.setBarrio(barrio.getValue());
		direccionProxy.setCiudad(ciudad.getValue());
		direccionProxy.setDepartamento(departamento.getValue());
		
		beneficiarioProxy.setDocumento(docProxy);
		beneficiarioProxy.setNombres(nombres.getValue());
		beneficiarioProxy.setApellidos(apellidos.getValue());
		beneficiarioProxy.setFechaNacimiento(fechaNacimiento.getValue());
		beneficiarioProxy.setSexo((sexo0.isChecked()?Sexo.MASCULINO:Sexo.FEMENINO));
		beneficiarioProxy.setDireccion(direccionProxy);
		
		
//		ValidatorFactory factory = Validation.byDefaultProvider().configure().buildValidatorFactory();
//		Validator validator = factory.getValidator();
//		Set<ConstraintViolation<BeneficiarioProxy>> violations = validator.validate(beneficiarioProxy);
//	
//		for (ConstraintViolation<BeneficiarioProxy> a : violations) {
//			Window.alert(a.getMessage());
//		}
		
		context.insertarBeneficiario(beneficiarioProxy).fire(new Receiver<Long>() {

			@Override
			public void onSuccess(Long response) {
//				new UIDialog("Aviso!", new HTML("Grabado con exito"));
				Window.alert("guardado");
			}
			
			public void onFailure(ServerFailure error) {
//				error.getMessage().
				new UIDialog("Error!", new HTML(error.getMessage()));
//				Window.alert("guardado");
			}
			
		});
		
//		context.find(1L).with("direccion","documento").fire(new Receiver<BeneficiarioProxy>() {
//			
//			@Override
//			public void onSuccess(BeneficiarioProxy response) {
//			    // Create a dialog box and set the caption text
//				HTML details = new HTML(response.getNombres() + " vive en la calle " + response.getDireccion().getCallePrincipal() + " numero: " + response.getDireccion().getNumeroCasa() + " de sexo:" + response.getSexo());
//				new UIDialog("Titulo del mensaje", details);
//			}
//		});
	}


//	public static final native String getLanguage() /*-{
//    	return navigator.language;
//	}-*/;
	
	
}
