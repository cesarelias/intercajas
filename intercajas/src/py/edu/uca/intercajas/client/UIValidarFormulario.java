package py.edu.uca.intercajas.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;


/*
 * Esta clase la utilizamos para registrar los errores de un formulario y mostrarlo al usuario
 * 
 */
public class UIValidarFormulario extends DialogBox {

	VerticalPanel dialogContents = new VerticalPanel();
	boolean esValido = true;
	String mensaje= "";
	String titulo;
	
	public UIValidarFormulario(String titulo) {
		
		this.titulo = titulo;
		setText("Se encontraton errores");
		esValido = true;
	    
	}
	
	public void addError(String mensaje) {
		esValido = false;
		this.mensaje += "<br><br><b>" + mensaje + "<b>";
	}
	
	public boolean esValido() {
		if (!esValido) {
			setGlassEnabled(true);
			setAnimationEnabled(true);
			center();
		
		    // Add a close button at the bottom of the dialog
		    Button closeButton = new Button(
		        "Cerrar", new ClickHandler() {
		          public void onClick(ClickEvent event) {
		            hide();
		          }
		        });
		    
		    dialogContents.setWidth("400px");
		    dialogContents.add(new HTML("<font color='red' size='4'>" + titulo + "</font><br>" + mensaje));
		    dialogContents.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		    dialogContents.add(closeButton);
		    setWidget(dialogContents);
	    	this.show();
	    }
	    return esValido;
	}

}
