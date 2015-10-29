package py.edu.uca.intercajas.client;

import py.edu.uca.intercajas.client.view.login.LoginImpl;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class Intercajas implements EntryPoint {
	
	public void onModuleLoad() {

		LoginImpl login = new LoginImpl();
		RootPanel.get().add(login);

	}
}
