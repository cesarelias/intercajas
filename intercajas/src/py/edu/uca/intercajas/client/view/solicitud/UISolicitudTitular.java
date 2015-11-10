package py.edu.uca.intercajas.client.view.solicitud;

import org.apache.bcel.classfile.Field;

import py.edu.uca.intercajas.client.menumail.Mail;
import py.edu.uca.intercajas.client.menumail.MenuMail;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class UISolicitudTitular extends Composite {

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
		
		grid.setWidget(0, 0, new Label("Numero Solicitud"));
		grid.setWidget(0, 1, new TextBox());
		grid.setWidget(1, 0, new Label("Nombre"));
		grid.setWidget(1, 1, new TextBox());
		grid.setWidget(2, 0, cancelar);
		grid.setWidget(2, 1, new Button("Enviar"));
		
		initWidget(grid);
	}
	
	
	public void mostrar() {

		if (MenuMail.getMain().getWidget() == null || !(MenuMail.getMain().getWidget() instanceof UISolicitudTitular)) {
			MenuMail.getMain().setWidget(this);
		}
		
	}

	 
	
}
