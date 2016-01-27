package py.edu.uca.intercajas.shared;

import py.edu.uca.intercajas.client.menumail.MenuMail;

import com.google.gwt.user.client.Window;
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
	public String titulo;
	
	Widget anterior;

	public void mostrar() {

		
		if (MenuMail.getMain().getWidget(0).getClass().getName().equals(this.getClass().getName())) {
			return;
		}
		
		
		if (titulo != null) {
			MenuMail.getMain().addNorth(new HTML("<h2>" + titulo + "</h2>"), 40);
		} 

		MenuMail.getMain().remove(0);
//		String nombre1 = this.getClass().getName();
//		String nombre2 = MenuMail.getMain().getWidget(0).getClass().getName();
//		if (!nombre1.equals(nombre2)) {
//			this.anterior = MenuMail.getMain().getWidget(0);
		MenuMail.getMain().add(this);
//		}			MenuMail.getMain().remove(0);
			
		if (titulo != null) {
			MenuMail.getMain().addNorth(new HTML("<h2>" + titulo + "</h2>"), 40);
		} 
	}
	
	public void mostrarDialog() {
		this.modoDialog = true;
		// ventana tipo dialogBox
		d = new DialogBox();
		d.add(this);
		d.center();
		d.show();
		
		if (titulo != null) {
			d.setText(titulo);
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
