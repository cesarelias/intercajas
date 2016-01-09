package py.edu.uca.intercajas.shared;

import py.edu.uca.intercajas.client.menumail.MenuMail;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class UIBase  extends Composite {

//	public UIBase () {
//		initWidget(this);
//	}
	
	protected DialogBox d;
	boolean modoDialog = false;
	public String title;
	public SimpleEventBus eventBus;
	
	Widget anterior;

	public void mostrar() {
		
				
//		String nombre1 = this.getClass().getName();
//		String nombre2 = MenuMail.getMain().getWidget(0).getClass().getName();
//		if (!nombre1.equals(nombre2)) {
			this.anterior = MenuMail.getMain().getWidget(0);
			MenuMail.getMain().remove(0);
			if (title != null) {
				MenuMail.getMain().addNorth(new HTML("<h2>" + title + "</h2>"), 40);
			} 
			MenuMail.getMain().add(this);
//		}
		

	}
	
	public void mostrarDialog() {
		this.modoDialog = true;
		// ventana tipo dialogBox
		d = new DialogBox();
		d.add(this);
		d.center();
		d.show();
		
		if (title != null) {
			d.setText(title);
		} 
		
		
	};
	
	public void volver() {
		MenuMail.getMain().remove(0);
		MenuMail.getMain().add(this.anterior);
	}
	
	public void close() {
		if (modoDialog) {
			d.hide();
		}	
	}
	
}
