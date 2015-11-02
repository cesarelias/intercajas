package py.edu.uca.intercajas.client;

import py.edu.uca.intercajas.client.view.login.UILoginImpl;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class Intercajas implements EntryPoint {
	
	public void onModuleLoad() {

		DecoratorPanel panel = new DecoratorPanel();
		UILoginImpl login = new UILoginImpl();
		panel.add(login);
		RootPanel.get().add(panel);

	}
}
