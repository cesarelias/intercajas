package py.edu.uca.intercajas.client.solicitud;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.menumail.Mailboxes.Images;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.UIDialog;
import py.edu.uca.intercajas.shared.entity.Solicitud;
import py.edu.uca.intercajas.shared.entity.SolicitudTitular;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CustomButton;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class SolicitudTitularEditorWorkFlow extends UIBase {

	interface Binder extends UiBinder<Widget, SolicitudTitularEditorWorkFlow> {
	}
	
	@UiField(provided = true) SolicitudTitularEditor solicitudTitularEditor;
	@UiField(provided = true) TablaPeriodoAporteDeclarado tablaPeriodoAporteDeclarado;
	@UiField FlexTable adjuntos;
	
	SolicitudTitular solicitudTitular;
	
	Images images = GWT.create(Images.class);
	
	
	public SolicitudTitularEditorWorkFlow(SimpleEventBus eventBus) {
//		title = "Solicitud Titular";
		this.eventBus = eventBus;
	    tablaPeriodoAporteDeclarado = new TablaPeriodoAporteDeclarado(eventBus);
		solicitudTitularEditor = new SolicitudTitularEditor(eventBus);
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		
		//Formateamos la tabla de adjuntos
		FlexCellFormatter cellFormatter = adjuntos.getFlexCellFormatter();
//		adjuntos.addStyleName("cw-FlexTable");
//		adjuntos.setBorderWidth(2);
//		adjuntos.setWidth("2em");
		adjuntos.setCellSpacing(1);
		adjuntos.setCellPadding(1);

		
	}

	@UiHandler("cancelar")
	void onCancel(ClickEvent event) {
		close();
	}
	
	@UiHandler("guardar")
	void onSave(ClickEvent event) {
		
		//TODO falta agregar la validacion del formulario
		/* 1. Beneficiario seleccionado (no nulo)
		 * 2. Fecha de solicitud no puede ser futura, ni menor a la fecha de la ley de intercajas
		 * 3. Periodos declarados no puede estar vacio
		 * 4. Periodos declarados debe contener al menos dos cajas en cuestion
		 * 5. El numero de solicitud debe ser autogenerado? o tenes ambos (agregar numero expediente interno)
		 */
		
		try { 
			
		solicitudTitular.setEstado(Solicitud.Estado.Nuevo);	
		solicitudTitular.setNumero(solicitudTitularEditor.numero.getValue());
		solicitudTitular.setFecha(solicitudTitularEditor.fecha.getValue());
		solicitudTitular.setBeneficiario(solicitudTitularEditor.beneficiario.getBeneficiario());
		solicitudTitular.setListaPeriodoAporteDeclarados(tablaPeriodoAporteDeclarado.listaPeriodoAporteDeclarado);

		} catch (Exception e) {
			Window.alert(e.getMessage());
		}
		BeneficiarioService.Util.get().nuevoSolicitudTitular(solicitudTitular, new MethodCallback<Void>() {
			
			@Override
			public void onSuccess(Method method, Void response) {
				close();
				Window.alert("Solicuitud GENERADA! .... Pero faltan las validaciones del formularo, no olvidar...!!");
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				Window.alert(exception.getMessage());
				new UIDialog("Error",new HTML(method.getResponse().getText()));
			}
		});

	}

	@UiHandler("agregarAdjunto")
	void onAgregarAdjunto(ClickEvent event) {
		int numRows = adjuntos.getRowCount();
		adjuntos.setWidget(numRows, 0,  new PushButton(new Image(images.trash())));
		adjuntos.setWidget(numRows, 1, new Hyperlink("Adjunto", "Adjuntito"));
//		adjuntos.getFlexCellFormatter().setRowSpan(0, 1, 5);
		
	}
	
	public void create() {
		try {
			solicitudTitular = new SolicitudTitular();
		} catch (Exception e) {
			Window.alert("este es el error?: " + e.getMessage());
		}

	}	
}
