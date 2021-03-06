package py.edu.uca.intercajas.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UIDialog extends DialogBox {

	VerticalPanel dialogContents = new VerticalPanel();
//	HTML details;
	
	public UIDialog(String titulo, HTML mensaje) {
		
		
		
		setGlassEnabled(true);
		setAnimationEnabled(true);
		center();
		
		setText(titulo);
	    
	    // Add a close button at the bottom of the dialog
	    Button closeButton = new Button(
	        "Cerrar", new ClickHandler() {
	          public void onClick(ClickEvent event) {
	            hide();
	          }
	        });
	    
	    dialogContents.setWidth("400px");
	    dialogContents.add(mensaje);
	    dialogContents.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
	    dialogContents.add(closeButton);
	    setWidget(dialogContents);
	    
//	    this.show();
	}

}
