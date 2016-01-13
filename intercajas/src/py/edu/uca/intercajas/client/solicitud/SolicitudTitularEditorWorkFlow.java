package py.edu.uca.intercajas.client.solicitud;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;

import py.edu.uca.intercajas.client.requestfactory.BeneficiarioProxy;
import py.edu.uca.intercajas.client.requestfactory.ContextGestionSolicitud;
import py.edu.uca.intercajas.client.requestfactory.FactoryGestion;
import py.edu.uca.intercajas.client.requestfactory.PeriodoAporteDeclaradoProxy;
import py.edu.uca.intercajas.client.requestfactory.SolicitudTitularProxy;
import py.edu.uca.intercajas.shared.UIBase;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class SolicitudTitularEditorWorkFlow extends UIBase {

	interface Binder extends UiBinder<Widget, SolicitudTitularEditorWorkFlow> {
	}
	
	interface Driver extends
		RequestFactoryEditorDriver<SolicitudTitularProxy, SolicitudTitularEditor> {
	}
	
	@UiField(provided = true) SolicitudTitularEditor solicitudTitularEditor;
	@UiField(provided = true) TablaPeriodoAporteDeclarado tablaPeriodoAporteDeclarado;
	
	private Driver editorDriver;
	
	SolicitudTitularProxy solicitudTitular;
	FactoryGestion factoryGestion;
	ContextGestionSolicitud ctx;
	
	public SolicitudTitularEditorWorkFlow(SimpleEventBus eventBus, FactoryGestion factoryGestion, ContextGestionSolicitud context) {
//		title = "Solicitud Titular";
		this.ctx = context;
		this.eventBus = eventBus;
		this.factoryGestion = factoryGestion;
	    tablaPeriodoAporteDeclarado = new TablaPeriodoAporteDeclarado(eventBus, factoryGestion, context);
		solicitudTitularEditor = new SolicitudTitularEditor(eventBus, factoryGestion);
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
	}

	@UiHandler("cancelar")
	void onCancel(ClickEvent event) {
		close();
	}
	
	@UiHandler("guardar")
	void onSave(ClickEvent event) {
		
		// Flush the contents of the UI
		RequestContext context = editorDriver.flush();
		
//		try {
//			solicitudTitular.setListaPeriodoAporteDeclarados(new ArrayList<PeriodoAporteDeclaradoProxy>());
//			for (PeriodoAporteDeclaradoProxy p : tablaPeriodoAporteDeclarado.listaPeriodoAporteDeclarado) {
//				PeriodoAporteDeclaradoProxy pp = ctx.create(PeriodoAporteDeclaradoProxy.class);
//				pp.setLugar(p.getLugar());
//				solicitudTitular.getListaPeriodoAporteDeclarados().add(pp);
//			}
////			solicitudTitular.setListaPeriodoAporteDeclarados(tablaPeriodoAporteDeclarado.listaPeriodoAporteDeclarado);
//		} catch (Exception e)  {
//			Window.alert(e.getMessage());
//		}
		
		solicitudTitular.setListaPeriodoAporteDeclarados(tablaPeriodoAporteDeclarado.listaPeriodoAporteDeclarado);
		
		// Check for errors
		if (editorDriver.hasErrors()) {
			setTitle("Se encontraron errores");
			return;
		}

		// Send the request
//		context.fire();

	}

	public void create(SolicitudTitularProxy solicitudTitularEdit) {
		try {
			
//			this.beneficiario = beneficiario;
			this.solicitudTitular = solicitudTitularEdit;
			setTitle("Nueva Solicitud Titular");
			ctx.nuevoSolicitudTitular(solicitudTitular).to(new Receiver<Void>() {
			@Override
			public void onConstraintViolation(Set<ConstraintViolation<?>> errors) {
				// Otherwise, show ConstraintViolations in the UI
				setTitle("Se encontraron errores");
				for (ConstraintViolation<?>  e : errors) {
					Window.alert(e.getMessage());
				}
				editorDriver.setConstraintViolations(errors);
			}
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert("Error al grabar" + error.getMessage());
			}
			@Override
			public void onSuccess(Void response) {
				// If everything went as planned, just dismiss the dialog box
//				eventBus.fireEvent(new BeneficiarioChangedEvent(getBeneficiario()));
				close();
			}
		});
		
		editorDriver = GWT.create(Driver.class);
		editorDriver.initialize(factoryGestion, solicitudTitularEditor);
		editorDriver.edit(solicitudTitular, ctx);
		
		} catch (Exception e) {
			Window.alert("este es el error?: " + e.getMessage());
		}

	}	
}
