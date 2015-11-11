package py.edu.uca.intercajas.client.view.solicitud;

import org.apache.bcel.classfile.Field;

import py.edu.uca.intercajas.client.menumail.Mail;
import py.edu.uca.intercajas.client.menumail.MenuMail;
import py.edu.uca.intercajas.client.requestfactory.BeneficiarioProxy;
import py.edu.uca.intercajas.client.requestfactory.ContextGestionBeneficiario;
import py.edu.uca.intercajas.client.requestfactory.FactoryGestion;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.UIDialog;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class UISolicitudTitular extends UIBase {
	
	private static  EventBus EVENTBUS = new SimpleEventBus();
	private static  FactoryGestion FACTORY  = GWT.create(FactoryGestion.class);
	
	
	Grid grid = new Grid(3,2);
	
	
	public UISolicitudTitular() {
		init();
	}

	private void init() {
		
		Button cancelar = new Button("cancelar");
		cancelar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new Mail().mostrar();
			}
		});
		
		Button enviar = new Button("enviar");
		enviar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				opcion1();
			}
		});
		
		
		grid.setWidget(0, 0, new Label("Numero Solicitud"));
		grid.setWidget(0, 1, new TextBox());
		grid.setWidget(1, 0, new Label("Nombre"));
		grid.setWidget(1, 1, new TextBox());
		grid.setWidget(2, 0, cancelar);
		grid.setWidget(2, 1, enviar);
		
		initWidget(grid);
	}
	
	
//	public void mostrar() {
//
//		if (MenuMail.getMain().getWidget() == null || !(MenuMail.getMain().getWidget() instanceof UISolicitudTitular)) {
//			MenuMail.getMain().setWidget(this);
//		}
//		
//	}

	public void opcion1()  {
		
		FACTORY.initialize(EVENTBUS);
		
		ContextGestionBeneficiario context = FACTORY.contextGestionBeneficiario();
		context.find(1L).with("direccion","documento").fire(new Receiver<BeneficiarioProxy>() {
			
			@Override
			public void onSuccess(BeneficiarioProxy response) {
			    // Create a dialog box and set the caption text
				HTML details = new HTML(response.getNombres() + " vive en la calle " + response.getDireccion().getCallePrincipal() + " numero: " + response.getDireccion().getNumeroCasa() + " de sexo:" + response.getSexo());
				new UIDialog("Titulo del mensaje", details);
			}
		});
	}
	
}
