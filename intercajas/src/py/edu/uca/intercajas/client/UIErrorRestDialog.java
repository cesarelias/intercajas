package py.edu.uca.intercajas.client;

import org.fusesource.restygwt.client.Method;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UIErrorRestDialog extends DialogBox {

	VerticalPanel dialogContents = new VerticalPanel();
//	HTML details;
	
	public UIErrorRestDialog(Method method, Throwable exception) {
		
		//MOstramos error del server
		if (method.getResponse().getStatusCode() == 401) {
			AppUtils.logout();
			Window.Location.reload();
		} 
		
		setGlassEnabled(true);
		setAnimationEnabled(true);
		center();
		
		setText("Error " + String.valueOf(method.getResponse().getStatusCode()));
		
		// Add a close button at the bottom of the dialog
		Button closeButton = new Button(
				"Cerrar", new ClickHandler() {
					public void onClick(ClickEvent event) {
						hide();
					}
				});
		
		dialogContents.setWidth("400px");
		dialogContents.add(new HTML(method.getResponse().getText()));
		dialogContents.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogContents.add(closeButton);
		setWidget(dialogContents);
		
		this.show();
	    
	}

}
