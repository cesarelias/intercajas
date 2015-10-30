package py.edu.uca.intercajas.client.view.menu;

import py.edu.uca.intercajas.client.requestfactory.BeneficiarioProxy;
import py.edu.uca.intercajas.client.requestfactory.ContextGestionBeneficiario;
import py.edu.uca.intercajas.client.requestfactory.FactoryGestion;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dev.shell.remoteui.RemoteMessageProto.Message.Request;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;



public class UIMenuImpl extends UIMenu {	
	
	private static  EventBus EVENTBUS = new SimpleEventBus();
	private static  FactoryGestion FACTORY  = GWT.create(FactoryGestion.class);
	
	@Override
	public void opcion1()  {
		
		FACTORY.initialize(EVENTBUS);
		
		ContextGestionBeneficiario context = FACTORY.contextGestionBeneficiario();
		
		context.find(1L).with("direccion").fire(new Receiver<BeneficiarioProxy>() {

			@Override
			public void onSuccess(BeneficiarioProxy response) {
				Window.alert(response.getNombres());
				Window.alert(response.getDireccion().getCallePrincipal());
				
			}
		});
		
		
//		VerticalPanel vp = new VerticalPanel();
//		
//		Double  r = new java.util.Random().nextDouble();
//		
//		Label lb = new Label("me hiciste click! random: " + r.toString());
//		
//		RadioButton rb = new RadioButton(r.toString(), "Radio uno");
//		RadioButton rb2 = new RadioButton(r.toString(), "Radio dos");
//		RadioButton rb3 = new RadioButton(r.toString(), "Radio tres");
//		
//		vp.add(rb);
//		vp.add(rb2);
//		vp.add(rb3);
//		
//		RootPanel.get().add(lb);
//		RootPanel.get().add(vp);
		
	}
	
}
