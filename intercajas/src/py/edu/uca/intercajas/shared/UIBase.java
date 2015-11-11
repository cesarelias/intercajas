package py.edu.uca.intercajas.shared;

import py.edu.uca.intercajas.client.menumail.MenuMail;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class UIBase  extends Composite {

//	public UIBase () {
//		initWidget(this);
//	}
	
	Widget origen;
	
	public void mostrar(Widget origen) {
		
		String nombre1 =this.getClass().getName();
		String nombre2 = MenuMail.getMain().getWidget().getClass().getName();
		if (!nombre1.equals(nombre2)) {
			this.origen = MenuMail.getMain().getWidget();
			MenuMail.getMain().setWidget(this);
		}

	};
	
	public void volver() {
		MenuMail.getMain().setWidget(this.origen);
	}
	
}
