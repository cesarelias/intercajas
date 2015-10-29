package py.edu.uca.intercajas.client.view.menu;

import py.edu.uca.intercajas.client.requestfactory.FactoryGestion;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;



public class UIMenuImpl extends UIMenu{	
	
	private static  EventBus EVENTBUS = new SimpleEventBus();
	private static  FactoryGestion FACTORY  = GWT.create(FactoryGestion.class);
	
	@Override
	public void opcion1() {
		Window.alert("click en pa primera opcion del menu");
	}
	
	
}
