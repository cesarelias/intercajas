package py.edu.uca.intercajas.shared;

import py.edu.uca.intercajas.client.menumail.MenuMail;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class UIBase  extends Composite {

//	public UIBase () {
//		initWidget(this);
//	}
	
	protected DialogBox d;
	protected SimpleEventBus eventBus;
	boolean modoDialog = false;
	
	Widget origen;

	//TODO quiza titulo deberia ser un atributo de la propia clase, para no estar pasando por parametro
	public void mostrar(Widget origen, SimpleEventBus eventBus, String titulo) {
		
		this.eventBus = eventBus;
		
		String nombre1 =this.getClass().getName();
		String nombre2 = MenuMail.getMain().getWidget(0).getClass().getName();
		if (!nombre1.equals(nombre2)) {
			this.origen = MenuMail.getMain().getWidget(0);
			MenuMail.getMain().remove(0);
			if (titulo != null) {
				MenuMail.getMain().addNorth(new HTML("<h2>" + titulo + "</h2>"), 40);
			}
			MenuMail.getMain().add(this);
		}
		
	}
	
	public void mostrarDialog(Widget origen, String titulo, SimpleEventBus eventBus) {
		this.modoDialog = true;
		this.eventBus = eventBus;
		
		// ventana tipo dialogBox
		d = new DialogBox();
		d.setText(titulo);
		d.add(this);
		d.center();
		d.show();

	};
	
	public void volver() {
		MenuMail.getMain().remove(0);
		MenuMail.getMain().add(this.origen);
	}
	
	public void setTitle(String titulo) {
		if (modoDialog) {
			d.setText(titulo);
		}
	}
	
	public void close() {
		if (modoDialog) {
			d.hide();
		}	
	}
	
}
