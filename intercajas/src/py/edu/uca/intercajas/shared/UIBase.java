package py.edu.uca.intercajas.shared;

import py.edu.uca.intercajas.client.menumail.MenuMail;

import com.google.gwt.user.client.ui.Composite;

public class UIBase  extends Composite {

//	public UIBase () {
//		initWidget(this);
//	}
	
	public void mostrar() {
		MenuMail.getMain().setWidget(this);
	};
	
}
